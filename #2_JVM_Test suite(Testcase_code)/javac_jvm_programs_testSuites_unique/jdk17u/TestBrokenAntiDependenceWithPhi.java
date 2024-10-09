public class TestBrokenAntiDependenceWithPhi {

    int a;

    int b;

    byte c;

    long e(int f, int g, long h) {
        int[] i = new int[a];
        double j = 2.74886;
        long[][] k = new long[a][a];
        long l = checkSum(k);
        return l;
    }

    void m() {
        int s, o, p[] = new int[a];
        double d;
        for (d = 5; d < 388; d++) {
            e(b, b, 40418347472393L);
            for (s = 3; s < 66; ++s) int1array(a, 9);
        }
        for (o = 6; o > 2; o--) p[o] = c;
    }

    public static void main(String[] q) {
        TestBrokenAntiDependenceWithPhi r = new TestBrokenAntiDependenceWithPhi();
        try {
            r.m();
        } catch (ArrayIndexOutOfBoundsException aioobe) {
        }
    }

    public static long checkSum(long[] a) {
        long sum = 0;
        for (int j = 0; j < a.length; j++) {
            sum += (a[j] / (j + 1) + a[j] % (j + 1));
        }
        return sum;
    }

    public static long checkSum(long[][] a) {
        long sum = 0;
        for (int j = 0; j < a.length; j++) {
            sum += checkSum(a[j]);
        }
        return sum;
    }

    public static int[] int1array(int sz, int seed) {
        int[] ret = new int[sz];
        init(ret, seed);
        return ret;
    }

    public static void init(int[] a, int seed) {
        for (int j = 0; j < a.length; j++) {
            a[j] = (j % 2 == 0) ? seed + j : seed - j;
        }
    }
}
