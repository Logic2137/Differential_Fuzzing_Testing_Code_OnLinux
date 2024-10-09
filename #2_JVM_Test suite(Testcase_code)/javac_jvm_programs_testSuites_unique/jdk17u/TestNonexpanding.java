import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.GCMParameterSpec;

public class TestNonexpanding {

    private static final String ALGORITHM = "AES";

    private static final String PROVIDER = "SunJCE";

    private static final String[] MODES = { "ECb", "CbC", "OFB", "OFB150", "cFB", "CFB7", "cFB8", "cFB16", "cFB24", "cFB32", "Cfb40", "cfB48", "cfB56", "cfB64", "cfB72", "cfB80", "cfB88", "cfB96", "cfb104", "cfB112", "cfB120", "GCM" };

    private static final String PADDING = "NoPadding";

    private static final int KEY_LENGTH = 128;

    public static void main(String[] argv) throws Exception {
        TestNonexpanding test = new TestNonexpanding();
        for (String mode : MODES) {
            test.runTest(ALGORITHM, mode, PADDING);
        }
    }

    public void runTest(String algo, String mo, String pad) throws Exception {
        Cipher ci = null;
        SecretKey key = null;
        try {
            Random rdm = new Random();
            byte[] plainText = new byte[128];
            rdm.nextBytes(plainText);
            ci = Cipher.getInstance(algo + "/" + mo + "/" + pad, PROVIDER);
            KeyGenerator kg = KeyGenerator.getInstance(algo, PROVIDER);
            kg.init(KEY_LENGTH);
            key = kg.generateKey();
            ci.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = new byte[ci.getOutputSize(plainText.length)];
            int offset = ci.update(plainText, 0, plainText.length, cipherText, 0);
            ci.doFinal(cipherText, offset);
            if (!(plainText.length == cipherText.length)) {
                if (mo.equalsIgnoreCase("GCM")) {
                    GCMParameterSpec spec = ci.getParameters().getParameterSpec(GCMParameterSpec.class);
                    int cipherTextLength = cipherText.length - spec.getTLen() / 8;
                    if (plainText.length == cipherTextLength) {
                        return;
                    }
                }
                System.out.println("Original length: " + plainText.length);
                System.out.println("Cipher text length: " + cipherText.length);
                throw new RuntimeException("Test failed!");
            }
        } catch (NoSuchAlgorithmException e) {
            if (!mo.equalsIgnoreCase("CFB7") && !mo.equalsIgnoreCase("OFB150")) {
                System.out.println("Unexpected NoSuchAlgorithmException with mode: " + mo);
                throw new RuntimeException("Test failed!");
            }
        } catch (NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | InvalidParameterSpecException | ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println("Test failed!");
            throw e;
        }
    }
}
