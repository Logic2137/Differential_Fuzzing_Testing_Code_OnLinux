















import java.util.Random;

public class TestAllocObjectArrays {

    static final long TARGET_MB = Long.getLong("target", 10_000); 

    static volatile Object sink;

    public static void main(String[] args) throws Exception {
        final int min = 0;
        final int max = 384 * 1024;
        long count = TARGET_MB * 1024 * 1024 / (16 + 4 * (min + (max - min) / 2));

        Random r = new Random();
        for (long c = 0; c < count; c++) {
            sink = new Object[min + r.nextInt(max - min)];
        }
    }

}
