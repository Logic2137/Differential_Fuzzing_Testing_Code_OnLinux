import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.lang.reflect.*;

public class NegativeLength {

    public static void main(String[] args) throws Exception {
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1", "SunJCE");
        PBEKeySpec pbeks = new PBEKeySpec(new char['p'], new byte[1], 1024, 8);
        Class c = pbeks.getClass();
        Field f = c.getDeclaredField("keyLength");
        f.setAccessible(true);
        f.setInt(pbeks, -8);
        System.out.println("pbeks.getKeyLength(): " + pbeks.getKeyLength());
        try {
            skf.generateSecret(pbeks);
            throw new Exception("We shouldn't get here.");
        } catch (InvalidKeySpecException ike) {
            System.out.println("Test Passed.");
        }
    }
}
