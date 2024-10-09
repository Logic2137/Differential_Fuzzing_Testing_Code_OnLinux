import java.util.Arrays;
import java.security.Key;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.*;
import javax.crypto.spec.*;

public class TestGeneral {

    private static final byte[] DATA_128 = Arrays.copyOf("1234567890123456789012345678901234".getBytes(), 128);

    private static final SecretKey KEY = new SecretKeySpec(DATA_128, 0, 16, "AES");

    private static final int KW_IV_LEN = 8;

    private static final int KWP_IV_LEN = 4;

    private static final int MAX_KW_PKCS5PAD_LEN = 16;

    private static final int MAX_KWP_PAD_LEN = 7;

    public static void testEnc(Cipher c, byte[] in, int inLen, int ivLen, int maxPadLen) throws Exception {
        System.out.println("input len: " + inLen);
        c.init(Cipher.ENCRYPT_MODE, KEY, new IvParameterSpec(in, 0, ivLen));
        int estOutLen = c.getOutputSize(inLen);
        byte[] out = c.doFinal(in, 0, inLen);
        if (estOutLen != out.length) {
            System.out.println("=> estimated: " + estOutLen);
            System.out.println("=> actual enc out length: " + out.length);
            throw new RuntimeException("Failed enc output len check");
        }
        if ((out.length % 8 != 0) || (out.length - inLen < 8)) {
            throw new RuntimeException("Invalid length of encrypted data: " + out.length);
        }
        c.init(Cipher.DECRYPT_MODE, KEY, new IvParameterSpec(in, 0, ivLen));
        estOutLen = c.getOutputSize(out.length);
        byte[] in2 = c.doFinal(out);
        if (estOutLen < in2.length || (estOutLen - in2.length) > maxPadLen) {
            System.out.println("=> estimated: " + estOutLen);
            System.out.println("=> actual dec out length: " + in2.length);
            throw new RuntimeException("Failed dec output len check");
        }
        if (!Arrays.equals(in, 0, inLen, in2, 0, inLen)) {
            throw new RuntimeException("Failed decrypted data check");
        }
    }

    public static void testWrap(Cipher c, byte[] in, int inLen, int ivLen, int maxPadLen) throws Exception {
        System.out.println("key len: " + inLen);
        c.init(Cipher.WRAP_MODE, KEY, new IvParameterSpec(in, 0, ivLen));
        int estOutLen = c.getOutputSize(inLen);
        byte[] out = c.wrap(new SecretKeySpec(in, 0, inLen, "Any"));
        if (estOutLen != out.length) {
            System.out.println("=> estimated: " + estOutLen);
            System.out.println("=> actual wrap out length: " + out.length);
            throw new RuntimeException("Failed wrap output len check");
        }
        if ((out.length % 8 != 0) || (out.length - inLen < 8)) {
            throw new RuntimeException("Invalid length of encrypted data: " + out.length);
        }
        c.init(Cipher.UNWRAP_MODE, KEY, new IvParameterSpec(in, 0, ivLen));
        estOutLen = c.getOutputSize(out.length);
        Key key2 = c.unwrap(out, "Any", Cipher.SECRET_KEY);
        if (!(key2 instanceof SecretKey)) {
            throw new RuntimeException("Failed unwrap output type check");
        }
        byte[] in2 = key2.getEncoded();
        if (estOutLen < in2.length || (estOutLen - in2.length) > maxPadLen) {
            System.out.println("=> estimated: " + estOutLen);
            System.out.println("=> actual unwrap out length: " + in2.length);
            throw new RuntimeException("Failed unwrap output len check");
        }
        if (inLen != in2.length || !Arrays.equals(in, 0, inLen, in2, 0, inLen)) {
            throw new RuntimeException("Failed unwrap data check");
        }
    }

    public static void testIv(Cipher c) throws Exception {
        Cipher c2 = Cipher.getInstance(c.getAlgorithm(), c.getProvider());
        if (c2.getIV() != null) {
            throw new RuntimeException("Expects null iv");
        }
        if (c2.getParameters() == null) {
            throw new RuntimeException("Expects non-null default parameters");
        }
        c2.init(Cipher.ENCRYPT_MODE, KEY);
        byte[] defIv2 = c2.getIV();
        c.init(Cipher.ENCRYPT_MODE, KEY);
        byte[] defIv = c.getIV();
        if (!Arrays.equals(defIv, defIv2)) {
            throw new RuntimeException("Failed default iv check");
        }
        try {
            c.init(Cipher.ENCRYPT_MODE, KEY, new IvParameterSpec(defIv, 0, defIv.length / 2));
            throw new RuntimeException("Invalid iv accepted");
        } catch (InvalidAlgorithmParameterException iape) {
            System.out.println("Invalid IV rejected as expected");
        }
        Arrays.fill(defIv, (byte) 0xFF);
        c.init(Cipher.ENCRYPT_MODE, KEY, new IvParameterSpec(defIv));
        byte[] newIv = c.getIV();
        if (!Arrays.equals(newIv, defIv)) {
            throw new RuntimeException("Failed set iv check");
        }
        byte[] newIv2 = c.getIV();
        if (newIv == newIv2) {
            throw new RuntimeException("Failed getIV copy check");
        }
    }

    public static void main(String[] argv) throws Exception {
        byte[] data = DATA_128;
        String ALGO = "AES/KW/PKCS5Padding";
        System.out.println("Testing " + ALGO);
        Cipher c = Cipher.getInstance(ALGO, "SunJCE");
        for (int i = 1; i <= MAX_KW_PKCS5PAD_LEN; i++) {
            testEnc(c, data, data.length - i, KW_IV_LEN, MAX_KW_PKCS5PAD_LEN);
            testWrap(c, data, data.length - i, KW_IV_LEN, MAX_KW_PKCS5PAD_LEN);
        }
        testIv(c);
        ALGO = "AES/KW/NoPadding";
        System.out.println("Testing " + ALGO);
        c = Cipher.getInstance(ALGO, "SunJCE");
        testEnc(c, data, data.length, KW_IV_LEN, 0);
        testEnc(c, data, data.length >> 1, KW_IV_LEN, 0);
        testWrap(c, data, data.length, KW_IV_LEN, 0);
        testWrap(c, data, data.length >> 1, KW_IV_LEN, 0);
        testIv(c);
        ALGO = "AES/KWP/NoPadding";
        System.out.println("Testing " + ALGO);
        c = Cipher.getInstance(ALGO, "SunJCE");
        for (int i = 0; i <= MAX_KWP_PAD_LEN; i++) {
            testEnc(c, data, data.length - i, KWP_IV_LEN, MAX_KWP_PAD_LEN);
            testWrap(c, data, data.length - i, KWP_IV_LEN, MAX_KWP_PAD_LEN);
        }
        testIv(c);
        System.out.println("All Tests Passed");
    }
}
