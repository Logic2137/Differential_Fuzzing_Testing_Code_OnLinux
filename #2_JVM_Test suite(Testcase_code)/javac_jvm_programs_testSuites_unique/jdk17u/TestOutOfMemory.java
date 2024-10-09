public class TestOutOfMemory {

    public static void main(java.lang.String[] unused) {
        final int BIG = 0x100000;
        try {
            int[][] X = new int[BIG][];
            for (int i = 0; i < BIG; i++) {
                X[i] = new int[BIG];
                System.out.println("length = " + X.length);
            }
        } catch (OutOfMemoryError oom) {
            System.out.println("OOM expected");
        }
    }
}
