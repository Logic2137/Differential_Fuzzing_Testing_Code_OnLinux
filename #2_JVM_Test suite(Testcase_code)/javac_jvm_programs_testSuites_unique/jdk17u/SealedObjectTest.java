import java.security.AlgorithmParameters;
import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;

public class SealedObjectTest {

    private static final String AES = "AES";

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";

    private static final String PROVIDER = "SunJCE";

    private static final int KEY_LENGTH = 128;

    public static void main(String[] args) throws Exception {
        doTest();
    }

    static void doTest() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(AES, PROVIDER);
        kg.init(KEY_LENGTH);
        SecretKey key = kg.generateKey();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters params = cipher.getParameters();
        SealedObject so = new SealedObject(key, cipher);
        try {
            so = new SealedObject(key, cipher);
            throw new RuntimeException("FAILED: expected IllegalStateException hasn't " + "been thrown");
        } catch (IllegalStateException ise) {
            System.out.println("Expected exception when seal it again with" + " the same key/IV: " + ise);
        }
        cipher.init(Cipher.DECRYPT_MODE, key, params);
        SecretKey unsealedKey = (SecretKey) so.getObject(cipher);
        assertKeysSame(unsealedKey, key, "SealedObject.getObject(Cipher)");
        unsealedKey = (SecretKey) so.getObject(key);
        assertKeysSame(unsealedKey, key, "SealedObject.getObject(Key)");
        unsealedKey = (SecretKey) so.getObject(key, PROVIDER);
        assertKeysSame(unsealedKey, key, "SealedObject.getObject(Key, String)");
    }

    static void assertKeysSame(SecretKey key1, SecretKey key2, String meth) {
        if (!Arrays.equals(key1.getEncoded(), key2.getEncoded())) {
            throw new RuntimeException("FAILED: original and unsealed objects aren't the same for " + meth);
        }
    }
}
