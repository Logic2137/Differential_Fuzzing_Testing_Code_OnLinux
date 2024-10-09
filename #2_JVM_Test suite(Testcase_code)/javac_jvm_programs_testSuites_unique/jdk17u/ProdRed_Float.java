
package compiler.loopopts.superword;

public class ProdRed_Float {

    public static void main(String[] args) throws Exception {
        float[] a = new float[256 * 1024];
        float[] b = new float[256 * 1024];
        prodReductionInit(a, b);
        float valid = 2000;
        float total = 0;
        for (int j = 0; j < 2000; j++) {
            total = j + 1;
            total = prodReductionImplement(a, b, total);
        }
        if (total == valid) {
            System.out.println("Success");
        } else {
            System.out.println("Invalid sum of elements variable in total: " + total);
            System.out.println("Expected value = " + valid);
            throw new Exception("Failed");
        }
    }

    public static void prodReductionInit(float[] a, float[] b) {
        for (int i = 0; i < a.length; i++) {
            a[i] = i + 2;
            b[i] = i + 1;
        }
    }

    public static float prodReductionImplement(float[] a, float[] b, float total) {
        for (int i = 0; i < a.length; i++) {
            total *= a[i] - b[i];
        }
        return total;
    }
}
