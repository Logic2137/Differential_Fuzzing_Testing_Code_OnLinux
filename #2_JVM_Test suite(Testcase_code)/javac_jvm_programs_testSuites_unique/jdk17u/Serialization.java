import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Serialization {

    private static final int NUM_SETS = 43;

    private static final int MAX_CAPACITY = 257;

    private static final float MAX_LOAD_FACTOR = 100.0F;

    private static final Random rnd = ThreadLocalRandom.current();

    private static HashSet<Integer> createHashSet() {
        int capacity = rnd.nextInt(MAX_CAPACITY);
        float loadFactor = Float.MIN_VALUE + rnd.nextFloat() * MAX_LOAD_FACTOR;
        HashSet<Integer> hashSet = new HashSet<Integer>(capacity, loadFactor);
        float multiplier = 2 * rnd.nextFloat();
        int size = (int) (capacity * loadFactor * multiplier);
        for (int i = 0; i < size; i++) {
            hashSet.add(rnd.nextInt());
        }
        return hashSet;
    }

    private static HashSet<Integer> serDeser(HashSet<Integer> hashSet) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(hashSet);
        oos.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        HashSet<Integer> result = (HashSet<Integer>) ois.readObject();
        oos.close();
        ois.close();
        return result;
    }

    private static void printHashSet(HashSet<Integer> hashSet) {
        System.err.println("Size: " + hashSet.size());
        for (Object o : hashSet) {
            System.err.println(o);
        }
    }

    public static void main(String[] args) {
        int failures = 0;
        for (int i = 0; i < NUM_SETS; i++) {
            HashSet<Integer> hashSet = createHashSet();
            HashSet<Integer> result = null;
            try {
                result = serDeser(hashSet);
            } catch (IOException ioe) {
                System.err.println(ioe);
                failures++;
            } catch (ClassNotFoundException cnfe) {
                System.err.println(cnfe);
                failures++;
            }
            if (!hashSet.equals(result)) {
                System.err.println("Unequal HashSets!");
                printHashSet(hashSet);
                System.err.println();
                failures++;
            }
        }
        if (failures != 0) {
            throw new RuntimeException("HashSet/Serialzation failed with " + failures + " failures!");
        }
    }
}
