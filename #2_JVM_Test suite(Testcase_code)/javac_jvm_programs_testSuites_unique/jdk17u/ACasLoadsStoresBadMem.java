public class ACasLoadsStoresBadMem {

    public static void main(String[] args) {
        int[] dst = new int[5];
        for (int i = 0; i < 20_000; i++) {
            test1(dst, 1);
            for (int j = 1; j < 5; j++) {
                if (dst[j] != j) {
                    throw new RuntimeException("Bad copy ");
                }
            }
        }
    }

    private static void test1(int[] dst, int dstPos) {
        int[] src = new int[4];
        not_inlined();
        src[0] = 1;
        src[1] = 2;
        src[2] = 3;
        src[3] = 4;
        System.arraycopy(src, 0, dst, dstPos, 4);
    }

    private static void not_inlined() {
    }
}
