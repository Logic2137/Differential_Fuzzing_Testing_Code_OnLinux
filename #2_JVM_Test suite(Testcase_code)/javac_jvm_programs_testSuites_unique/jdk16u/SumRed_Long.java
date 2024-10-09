



package compiler.loopopts.superword;

public class SumRed_Long {
    public static void main(String[] args) throws Exception {
        long[] a = new long[256 * 1024];
        long[] b = new long[256 * 1024];
        long[] c = new long[256 * 1024];
        long[] d = new long[256 * 1024];
        sumReductionInit(a, b, c);
        long total = 0;
        long valid = 262144000;
        for (int j = 0; j < 2000; j++) {
            total = sumReductionImplement(a, b, c, d, total);
        }
        total = (int) total;
        if (total == valid) {
            System.out.println("Success");
        } else {
            System.out.println("Invalid sum of elements variable in total: " + total);
            System.out.println("Expected value = " + valid);
            throw new Exception("Failed");
        }
    }

    public static void sumReductionInit(
            long[] a,
            long[] b,
            long[] c) {
        for (int j = 0; j < 1; j++) {
            for (int i = 0; i < a.length; i++) {
                a[i] = i * 1 + j;
                b[i] = i * 1 - j;
                c[i] = i + j;
            }
        }
    }

    public static long sumReductionImplement(
            long[] a,
            long[] b,
            long[] c,
            long[] d,
            long total) {
        for (int i = 0; i < a.length; i++) {
            d[i] = (a[i] * b[i]) + (a[i] * c[i]) + (b[i] * c[i]);
            total += d[i];
        }
        return total;
    }

}
