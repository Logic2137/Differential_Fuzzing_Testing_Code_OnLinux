



package compiler.runtime;

public class Test6892265 {
    static final int NCOPY = 1;
    static final int OVERFLOW = 1;
    static int[] src2 = new int[NCOPY];
    static int[] dst2;

    static void test() {
        int N;
        int SIZE;

        N = Integer.MAX_VALUE / 4 + OVERFLOW;
        System.arraycopy(src2, 0, dst2, N, NCOPY);
        System.arraycopy(dst2, N, src2, 0, NCOPY);
    }

    public static void main(String[] args) {
        try {
            dst2 = new int[NCOPY + Integer.MAX_VALUE / 4 + OVERFLOW];
        } catch (OutOfMemoryError e) {
            System.exit(95); 
        }
        System.out.println("warmup");
        for (int i = 0; i < 11000; i++) {
            test();
        }
        System.out.println("start");
        for (int i = 0; i < 1000; i++) {
            test();
        }
        System.out.println("finish");
    }

}
