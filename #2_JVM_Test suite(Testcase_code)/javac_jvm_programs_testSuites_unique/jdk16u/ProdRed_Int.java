



package compiler.loopopts.superword;

public class ProdRed_Int {
    public static void main(String[] args) throws Exception {
        int[] a = new int[256 * 1024];
        int[] b = new int[256 * 1024];
        prodReductionInit(a, b);
        int valid = 419430401;
        int total = 1;
        for (int j = 0; j < 2000; j++) {
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

    public static void prodReductionInit(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            a[i] = i + 2;
            b[i] = i + 1;
        }
    }

    public static int prodReductionImplement(int[] a, int[] b, int total) {
        for (int i = 0; i < a.length; i++) {
            total *= a[i] + b[i];
        }
        return total;
    }

}
