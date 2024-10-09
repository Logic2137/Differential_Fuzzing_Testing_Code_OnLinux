import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class DESKeyCleanupTest {

    private final static String SunJCEProvider = "SunJCE";

    public static void main(String[] args) throws Exception {
        testCleanupSecret("DES");
        testCleanupSecret("DESede");
    }

    static void testCleanupSecret(String algorithm) throws Exception {
        KeyGenerator desGen = KeyGenerator.getInstance(algorithm, SunJCEProvider);
        SecretKey key = desGen.generateKey();
        Class<?> keyClass = key.getClass();
        Field keyField = keyClass.getDeclaredField("key");
        keyField.setAccessible(true);
        byte[] array = (byte[]) keyField.get(key);
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
