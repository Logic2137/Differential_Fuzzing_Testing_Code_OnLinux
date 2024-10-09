import javax.crypto.*;
import java.security.*;

public class TestExemption {

    public static void main(String[] args) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);
        SecretKey key128 = kg.generateKey();
        kg.init(192);
        SecretKey key192 = kg.generateKey();
        kg.init(256);
        SecretKey key256 = kg.generateKey();
        Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
        System.out.println("Testing 128-bit");
        c.init(Cipher.ENCRYPT_MODE, key128);
        System.out.println("Testing 192-bit");
        c.init(Cipher.ENCRYPT_MODE, key192);
        try {
            System.out.println("Testing 256-bit");
            c.init(Cipher.ENCRYPT_MODE, key256);
        } catch (InvalidKeyException e) {
            System.out.println("Caught the right exception");
        }
        System.out.println("DONE!");
    }
}
