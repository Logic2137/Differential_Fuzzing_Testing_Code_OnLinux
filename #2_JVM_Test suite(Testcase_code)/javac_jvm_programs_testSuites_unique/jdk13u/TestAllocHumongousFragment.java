import java.util.*;
import java.util.concurrent.*;

public class TestAllocHumongousFragment {

    static final long TARGET_MB = Long.getLong("target", 30_000);

    static final long LIVE_MB = Long.getLong("occupancy", 700);

    static volatile Object sink;

    static List<int[]> objects;

    public static void main(String[] args) throws Exception {
        final int min = 128 * 1024;
        final int max = 16 * 1024 * 1024;
        final long count = TARGET_MB * 1024 * 1024 / (16 + 4 * (min + (max - min) / 2));
        objects = new ArrayList<>();
        long current = 0;
        Random r = new Random();
        for (long c = 0; c < count; c++) {
            while (current > LIVE_MB * 1024 * 1024) {
                int idx = ThreadLocalRandom.current().nextInt(objects.size());
                int[] remove = objects.remove(idx);
                current -= remove.length * 4 + 16;
            }
            int[] newObj = new int[min + r.nextInt(max - min)];
            current += newObj.length * 4 + 16;
            objects.add(newObj);
            sink = new Object();
            System.out.println("Allocated: " + (current / 1024 / 1024) + " Mb");
        }
    }
}
