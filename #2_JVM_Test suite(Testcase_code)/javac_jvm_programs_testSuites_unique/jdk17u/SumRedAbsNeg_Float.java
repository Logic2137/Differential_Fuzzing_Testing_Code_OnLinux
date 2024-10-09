
package compiler.loopopts.superword;

public class SumRedAbsNeg_Float {

    public static void main(String[] args) throws Exception {
        float[] a = new float[256 * 1024];
        float[] b = new float[256 * 1024];
        float[] c = new float[256 * 1024];
        float[] d = new float[256 * 1024];
        sumReductionInit(a, b, c);
        float total = 0;
        float valid = (float) 4.611686E18;
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

    public static void sumReductionInit(float[] a, float[] b, float[] c) {
        for (int j = 0; j < 1; j++) {
            for (int i = 0; i < a.length; i++) {
                a[i] = i * 1 + j;
                b[i] = i * 1 - j;
                c[i] = i + j;
            }
        }
    }

    public static float sumReductionImplement(float[] a, float[] b, float[] c, float[] d, float total) {
        for (int i = 0; i < a.length; i++) {
            d[i] = Math.abs(-a[i] * -b[i]) + Math.abs(-a[i] * -c[i]) + Math.abs(-b[i] * -c[i]);
            total += d[i];
        }
        return total;
    }
}
