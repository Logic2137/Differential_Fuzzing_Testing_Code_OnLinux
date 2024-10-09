public class Test7020614 {

    private static final int ITERATIONS = 1000;

    private static int doNotOptimizeOut = 0;

    public static long bitCountShort() {
        long t0 = System.currentTimeMillis();
        int sum = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int it = 0; it < ITERATIONS; ++it) {
            short value = 0;
            do {
                sum += Integer.bitCount(value);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            } while (++value != 0);
        }
        doNotOptimizeOut += sum;
        return System.currentTimeMillis() - t0;
    }

    public static void main(String[] args) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 4; ++i) {
            System.out.println((i + 1) + ": " + bitCountShort());
        }
        System.out.println("doNotOptimizeOut value: " + doNotOptimizeOut);
    }
}
