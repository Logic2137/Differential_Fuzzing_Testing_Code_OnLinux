import java.lang.String;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import static java.lang.System.out;

public class Empty {

    public static void main(String[] args) throws Exception {
        try {
            byte[] master = { 0, 1, 2, 3, 4 };
            SecretKey key = new SecretKeySpec(master, "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            throw new RuntimeException("InvalidKeyException not thrown");
        } catch (java.security.InvalidKeyException ike) {
            ike.printStackTrace();
            if (ike.getMessage() != null) {
                out.println("Status -- Passed");
            } else {
                throw new RuntimeException("Error message is not expected when" + " InvalidKeyException is thrown");
            }
        }
    }
}
