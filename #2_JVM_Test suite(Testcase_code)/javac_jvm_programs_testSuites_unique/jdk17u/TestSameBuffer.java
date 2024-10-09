import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;

public class TestSameBuffer {

    private static final String ALGORITHM = "AES";

    private static final String PROVIDER = "SunJCE";

    private static final String[] MODES = { "ECb", "CbC", "OFB", "CFB150", "cFB", "CFB7", " cFB8", "cFB16", "cFB24", "cFB32", "Cfb40", "cfB48", " cfB56", "cfB64", "cfB72", "cfB80", "cfB88", "cfB96", "cfb104", "cfB112", "cfB120" };

    private static final String PADDING = "NoPadding";

    private static final int KEY_LENGTH = 128;

    public static void main(String[] argv) throws Exception {
        TestSameBuffer test = new TestSameBuffer();
        for (String mode : MODES) {
            test.runTest(ALGORITHM, mode, PADDING);
        }
    }

    public void runTest(String algo, String mo, String pad) throws Exception {
        Cipher ci = null;
        byte[] iv = null;
        AlgorithmParameterSpec aps = null;
        SecretKey key = null;
        try {
            Random rdm = new Random();
            byte[] plainText = new byte[128];
            rdm.nextBytes(plainText);
            byte[] tmpText = new byte[plainText.length];
            for (int i = 0; i < plainText.length; i++) {
                tmpText[i] = plainText[i];
            }
            ci = Cipher.getInstance(algo + "/" + mo + "/" + pad, PROVIDER);
            KeyGenerator kg = KeyGenerator.getInstance(algo, PROVIDER);
            kg.init(KEY_LENGTH);
            key = kg.generateKey();
            ci.init(Cipher.ENCRYPT_MODE, key);
            int offset = ci.update(plainText, 0, plainText.length, plainText, 0);
            ci.doFinal(plainText, offset);
            if (!mo.equalsIgnoreCase("ECB")) {
                iv = ci.getIV();
                aps = new IvParameterSpec(iv);
            } else {
                aps = null;
            }
            ci.init(Cipher.DECRYPT_MODE, key, aps);
            byte[] recoveredText = new byte[ci.getOutputSize(plainText.length)];
            ci.doFinal(plainText, 0, plainText.length, recoveredText);
            if (!java.util.Arrays.equals(tmpText, recoveredText)) {
                System.out.println("Original: ");
                dumpBytes(plainText);
                System.out.println("Recovered: ");
                dumpBytes(recoveredText);
                throw new RuntimeException("Original text is not equal with recovered text, with mode:" + mo);
            }
        } catch (NoSuchAlgorithmException e) {
            if (!mo.equalsIgnoreCase("CFB7") && !mo.equalsIgnoreCase("CFB150")) {
                System.out.println("Unexpected NoSuchAlgorithmException with mode: " + mo);
                throw new RuntimeException("Test failed!");
            }
        } catch (NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println("Test failed!");
            throw e;
        }
    }

    private void dumpBytes(byte[] bytes) {
        for (byte b : bytes) {
            System.out.print(Integer.toHexString(b));
        }
        System.out.println();
    }
}
