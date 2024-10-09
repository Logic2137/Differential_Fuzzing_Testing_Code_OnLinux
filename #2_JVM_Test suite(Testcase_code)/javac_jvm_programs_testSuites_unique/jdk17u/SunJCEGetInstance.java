import java.security.Security;
import java.security.Provider;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SunJCEGetInstance {

    public static void main(String[] args) throws Exception {
        Cipher jce;
        try {
            Provider prov = Security.getProvider("SunJCE");
            Security.removeProvider("SunJCE");
            jce = Cipher.getInstance("AES/CBC/PKCS5Padding", prov);
            jce.init(Cipher.ENCRYPT_MODE, new SecretKeySpec("1234567890abcedf".getBytes(), "AES"));
            jce.doFinal("PlainText".getBytes());
        } catch (Exception e) {
            System.err.println("Setup failure:  ");
            throw e;
        }
        try {
            jce.getParameters().getEncoded();
        } catch (Exception e) {
            System.err.println("Test Failure");
            throw e;
        }
        System.out.println("Passed");
    }
}
