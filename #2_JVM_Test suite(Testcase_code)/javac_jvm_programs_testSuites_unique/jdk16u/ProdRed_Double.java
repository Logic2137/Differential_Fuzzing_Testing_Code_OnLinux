



package compiler.loopopts.superword;

public class ProdRed_Double {
    public static void main(String[] args) throws Exception {
        double[] a = new double[256 * 1024];
        double[] b = new double[256 * 1024];
        prodReductionInit(a, b);
        double valid = 2000;
        double total = 0;
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

    public static void prodReductionInit(double[] a, double[] b) {
        for (int i = 0; i < a.length; i++) {
            a[i] = i + 2;
            b[i] = i + 1;
        }
    }

    public static double prodReductionImplement(double[] a, double[] b, double total) {
        for (int i = 0; i < a.length; i++) {
            total *= a[i] - b[i];
        }
        return total;
    }

}
