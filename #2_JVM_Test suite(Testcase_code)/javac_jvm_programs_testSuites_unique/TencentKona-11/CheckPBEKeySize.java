



import java.lang.reflect.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import com.sun.crypto.provider.*;

public class CheckPBEKeySize {

    private static final String ALGO = "PBEWithSHA1AndDESede";
    private static final int KEYSIZE = 112; 

    public static final void main(String[] args) throws Exception {

        
        SecretKeyFactory skFac = SecretKeyFactory.getInstance("PBE");
        SecretKey skey =
            skFac.generateSecret(new PBEKeySpec("test123".toCharArray()));

        
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, skey);

        
        Field spi = Cipher.class.getDeclaredField("spi");
        spi.setAccessible(true);
        Object value = spi.get(cipher);

        
        Method engineGetKeySize =
            PKCS12PBECipherCore$PBEWithSHA1AndDESede.class
                .getDeclaredMethod("engineGetKeySize", Key.class);
        engineGetKeySize.setAccessible(true);

        
        int keySize = (int) engineGetKeySize.invoke(value, skey);
        if (keySize == KEYSIZE) {
            System.out.println(ALGO + ".engineGetKeySize returns " + keySize +
                " bits, as expected");
            System.out.println("OK");
        } else {
            throw new Exception("ERROR: " + ALGO + " key size is incorrect");
        }
    }
}
