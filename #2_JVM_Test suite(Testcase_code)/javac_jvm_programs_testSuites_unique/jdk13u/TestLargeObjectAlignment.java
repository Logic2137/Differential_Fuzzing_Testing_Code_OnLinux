import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestLargeObjectAlignment {

    static final int SLABS_COUNT = Integer.getInteger("slabs", 10000);

    static final int NODE_COUNT = Integer.getInteger("nodes", 10000);

    static final long TIME_NS = 1000L * 1000L * Integer.getInteger("timeMs", 5000);

    static Object[] objects;

    public static void main(String[] args) throws Exception {
        objects = new Object[SLABS_COUNT];
        long start = System.nanoTime();
        while (System.nanoTime() - start < TIME_NS) {
            objects[ThreadLocalRandom.current().nextInt(SLABS_COUNT)] = createSome();
        }
    }

    public static Object createSome() {
        List<Integer> result = new ArrayList<Integer>();
        for (int c = 0; c < NODE_COUNT; c++) {
            result.add(new Integer(c));
        }
        return result;
    }
}
