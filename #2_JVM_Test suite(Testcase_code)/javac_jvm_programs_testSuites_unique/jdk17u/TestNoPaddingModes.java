import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.*;

public class TestNoPaddingModes {

    private static final String[] MODES = { "CTR", "CTS", "GCM" };

    private static final String[] PADDINGS = { "PKCS5Padding", "ISO10126Padding" };

    public static void main(String[] args) throws Exception {
        Provider p = Security.getProvider("SunJCE");
        String transformation;
        for (String mode : MODES) {
            for (String padding : PADDINGS) {
                transformation = "AES/" + mode + "/" + padding;
                System.out.println("Test using " + transformation);
                try {
                    Cipher c = Cipher.getInstance(transformation, "SunJCE");
                    throw new RuntimeException("=> Fail, no exception thrown");
                } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
                    System.out.println("=> Expected ex: " + ex);
                }
                try {
                    Cipher c = Cipher.getInstance(transformation, p);
                    throw new RuntimeException("=> Fail, no exception thrown");
                } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
                    System.out.println("=> Expected ex: " + ex);
                }
            }
        }
        System.out.println("Test Passed");
    }
}
