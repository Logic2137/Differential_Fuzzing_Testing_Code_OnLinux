
package compiler.vectorization;


public class TestMemoryVectorMismatched {
    public static void main(String[] g) {
        int a = 400;
        long expected = -35984L;
        for (int i = 0; i < 10; i++) {
            long v = test(a);
            if (v != expected) {
                throw new AssertionError("Wrong result: " + v + " != " + expected);
            }
        }
    }

    static long test(int a) {
        int i16, d = 5, e = -56973;
        long f[] = new long[a];
        init(f, 5);
        for (i16 = 2; i16 < 92; i16++) {
            f[i16 - 1] *= d;
            f[i16 + 1] *= d;
        }
        while (++e < 0) {
        }
        return checkSum(f);
    }

    public static void init(long[] a, long seed) {
        for (int j = 0; j < a.length; j++) {
            a[j] = (j % 2 == 0) ? seed + j : seed - j;
        }
    }


    public static long checkSum(long[] a) {
        long sum = 0;
        for (int j = 0; j < a.length; j++) {
            sum += (a[j] / (j + 1) + a[j] % (j + 1));
        }
        return sum;
    }
}
