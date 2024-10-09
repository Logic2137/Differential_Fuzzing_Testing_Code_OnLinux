



import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class PBEKeyCleanupTest {

    private final static String SunJCEProvider = "SunJCE";

    private static final String PASS_PHRASE = "some hidden string";
    private static final int ITERATION_COUNT = 1000;
    private static final int KEY_SIZE = 128;

    public static void main(String[] args) throws Exception {
        testPBESecret("PBEWithMD5AndDES");
        testPBKSecret("PBKDF2WithHmacSHA1");
    }

    private static void testPBESecret(String algorithm) throws Exception {
        char[] password = new char[] {'f', 'o', 'o'};
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
        SecretKeyFactory keyFac =
                SecretKeyFactory.getInstance(algorithm, SunJCEProvider);

        testCleanupSecret(algorithm, keyFac.generateSecret(pbeKeySpec));
    }

    private static void testPBKSecret(String algorithm) throws Exception {
        byte[] salt = new byte[8];
        new Random().nextBytes(salt);
        char[] password = new char[] {'f', 'o', 'o'};
        PBEKeySpec pbeKeySpec = new PBEKeySpec(PASS_PHRASE.toCharArray(), salt,
                ITERATION_COUNT, KEY_SIZE);
        SecretKeyFactory keyFac =
                SecretKeyFactory.getInstance(algorithm, SunJCEProvider);

        testCleanupSecret(algorithm, keyFac.generateSecret(pbeKeySpec));
    }

    static void testCleanupSecret(String algorithm, SecretKey key) throws Exception {

        
        Class<?> keyClass = key.getClass();
        Field keyField = keyClass.getDeclaredField("key");
        keyField.setAccessible(true);
        byte[] array = (byte[])keyField.get(key);

        byte[] zeros = new byte[array.length];
        do {
            
            System.out.printf("%s array: %s%n", algorithm, Arrays.toString(array));
            key = null;
            System.gc();        
        } while (Arrays.compare(zeros, array) != 0);
        System.out.printf("%s array: %s%n", algorithm, Arrays.toString(array));

        Reference.reachabilityFence(key); 
        Reference.reachabilityFence(array); 
    }
}



