



package compiler.loopopts.superword;

public class SumRed_Int {
    public static void main(String[] args) throws Exception {
        int[] a = new int[256 * 1024];
        int[] b = new int[256 * 1024];
        int[] c = new int[256 * 1024];
        int[] d = new int[256 * 1024];
        sumReductionInit(a, b, c);
        int total = 0;
        int valid = 262144000;
        for (int j = 0; j < 2000; j++) {
            total = sumReductionImplement(a, b, c, d, total);
        }
        if (total == valid) {
            System.out.println("Success");
        } else {
            System.out.println("Invalid sum of elements variable in total: " + total);
            System.out.println("Expected value = " + valid);
            throw new Exception("Failed");
        }
    }

    public static void sumReductionInit(
            int[] a,
            int[] b,
            int[] c) {
        for (int j = 0; j < 1; j++) {
            for (int i = 0; i < a.length; i++) {
                a[i] = i * 1 + j;
                b[i] = i * 1 - j;
                c[i] = i + j;
            }
        }
    }

    public static int sumReductionImplement(
            int[] a,
            int[] b,
            int[] c,
            int[] d,
            int total) {
        for (int i = 0; i < a.length; i++) {
            d[i] = (a[i] * b[i]) + (a[i] * c[i]) + (b[i] * c[i]);
            total += d[i];
        }
        return total;
    }

}
