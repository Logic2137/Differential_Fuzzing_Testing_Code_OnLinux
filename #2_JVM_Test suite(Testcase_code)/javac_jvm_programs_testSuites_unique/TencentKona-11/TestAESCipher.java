

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


public class TestAESCipher {

    private static final String ALGORITHM = "AES";
    private static final String PROVIDER = "SunJCE";
    private static final String[] MODES = { "ECb", "CbC", "CTR", "PCBC", "OFB",
        "OFB150", "cFB", "CFB7", "cFB8", "cFB16", "cFB24", "cFB32",
        "Cfb40", "cfB48", "cfB56", "cfB64", "cfB72", "cfB80", "cfB88",
        "cfB96", "cfb104", "cfB112", "cfB120", "OFB8", "OFB16", "OFB24",
        "OFB32", "OFB40", "OFB48", "OFB56", "OFB64", "OFB72", "OFB80",
        "OFB88", "OFB96", "OFB104", "OFB112", "OFB120", "GCM" };
    private static final String[] PADDING = { "NoPadding", "PKCS5Padding" };
    private static final int KEY_LENGTH = 128;

    public static void main(String argv[]) throws Exception {
        TestAESCipher test = new TestAESCipher();
        for (String mode : MODES) {
            int padKinds = 1;
            if (mode.equalsIgnoreCase("ECB") || mode.equalsIgnoreCase("PCBC")
                    || mode.equalsIgnoreCase("CBC")) {
                padKinds = PADDING.length;
            }

            for (int k = 0; k < padKinds; k++) {
                test.runTest(ALGORITHM, mode, PADDING[k]);
            }
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

            ci = Cipher.getInstance(algo + "/" + mo + "/" + pad, PROVIDER);
            KeyGenerator kg = KeyGenerator.getInstance(algo, PROVIDER);
            kg.init(KEY_LENGTH);
            key = kg.generateKey();

            
            if (!mo.equalsIgnoreCase("GCM")) {
                ci.init(Cipher.ENCRYPT_MODE, key, aps);
            } else {
                ci.init(Cipher.ENCRYPT_MODE, key);
            }

            byte[] cipherText = new byte[ci.getOutputSize(plainText.length)];
            int offset = ci.update(plainText, 0, plainText.length, cipherText,
                    0);
            ci.doFinal(cipherText, offset);

            if (!mo.equalsIgnoreCase("ECB")) {
                iv = ci.getIV();
                aps = new IvParameterSpec(iv);
            } else {
                aps = null;
            }

            if (!mo.equalsIgnoreCase("GCM")) {
                ci.init(Cipher.DECRYPT_MODE, key, aps);
            } else {
                ci.init(Cipher.DECRYPT_MODE, key, ci.getParameters());
            }

            byte[] recoveredText = new byte[ci.getOutputSize(cipherText.length)];
            int len = ci.doFinal(cipherText, 0, cipherText.length,
                    recoveredText);
            byte[] tmp = new byte[len];
            System.arraycopy(recoveredText, 0, tmp, 0, len);

            
            if (!java.util.Arrays.equals(plainText, tmp)) {
                System.out.println("Original: ");
                dumpBytes(plainText);
                System.out.println("Recovered: ");
                dumpBytes(tmp);
                throw new RuntimeException(
                        "Original text is not equal with recovered text, with mode:"
                                + mo);
            }

        } catch (NoSuchAlgorithmException e) {
            
            if (!mo.equalsIgnoreCase("CFB7") && !mo.equalsIgnoreCase("OFB150")) {
                System.out.println("Unexpected NoSuchAlgorithmException with mode: "
                        + mo);
                throw new RuntimeException("Test failed!");
            }
        }  catch ( NoSuchProviderException | NoSuchPaddingException
                | InvalidKeyException | InvalidAlgorithmParameterException
                | ShortBufferException | IllegalBlockSizeException
                | BadPaddingException e) {
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
