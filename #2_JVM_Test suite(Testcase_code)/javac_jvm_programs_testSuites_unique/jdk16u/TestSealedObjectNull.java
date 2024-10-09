

import java.io.IOException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NullCipher;
import javax.crypto.SealedObject;


public class TestSealedObjectNull {

    private static final String SEAL_STR = "Any String!@#$%^";

    public static void main(String[] args) throws IOException,
            IllegalBlockSizeException, ClassNotFoundException,
            BadPaddingException {
        Cipher nullCipher = new NullCipher();

        
        SealedObject so = new SealedObject(SEAL_STR, nullCipher);

        
        if (!(SEAL_STR.equals(so.getObject(nullCipher)))) {
            throw new RuntimeException("Unseal and compare failed.");
        }

        System.out.println("Test passed.");
    }
}
