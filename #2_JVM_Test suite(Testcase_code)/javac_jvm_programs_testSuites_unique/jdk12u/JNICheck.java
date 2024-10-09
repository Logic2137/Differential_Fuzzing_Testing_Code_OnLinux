



import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import java.security.NoSuchProviderException;

public class JNICheck {

    
    static class SealedObjectTest {
        Cipher c;

        SealedObjectTest() throws Exception {
            try {
                c = Cipher.getInstance("AES", "SunPKCS11-Solaris");
            } catch (NoSuchProviderException nspe) {
                System.out.println("No SunPKCS11-Solaris provider.  Test skipped");
                return;
            }

            String s = "Test string";
            SealedObject so;
            SecretKey key = KeyGenerator.getInstance("AES").generateKey();

            c.init(Cipher.ENCRYPT_MODE, key);
            so = new SealedObject(s, c);

            so.getObject(key, "SunPKCS11-Solaris");
        }
    }

    public static void main(String args[]) throws Exception {
        new SealedObjectTest();
    }
}
