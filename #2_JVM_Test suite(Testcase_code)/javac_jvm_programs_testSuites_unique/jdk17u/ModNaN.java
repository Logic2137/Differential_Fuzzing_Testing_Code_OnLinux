
package compiler.floatingpoint;

public class ModNaN {

    static double[][] bad = new double[][] { new double[] { Double.longBitsToDouble(0x7FE0000000000000L), Double.longBitsToDouble(0x0000000000040000L) }, new double[] { Double.longBitsToDouble(0x7FEFFFFFFFFFFFFFL), Double.longBitsToDouble(0x000000000007FFFFL) }, new double[] { Double.longBitsToDouble(0x7FEFFFFFFFFFFFFFL), Double.longBitsToDouble(0x000000007FFFFFFFL) }, new double[] { Double.longBitsToDouble(0x7FEFFFFFFFFFFFFFL), 6.767486E-317 }, new double[] { Double.longBitsToDouble(0x7FEFFFFFFFFFFFFFL), 7.528725E-318 } };

    static double[][] good = new double[][] { new double[] { Double.longBitsToDouble(0x7FE0000000000000L), Double.longBitsToDouble(0x000000000003FFFFL) }, new double[] { Double.longBitsToDouble(0x7FEFFFFFFFFFFFFFL), Double.longBitsToDouble(0x0000000000080000L) }, new double[] { Double.longBitsToDouble(0x7FEFFFFFFFFFFFFFL), Double.longBitsToDouble(0x000000007FFEFFFFL) } };

    public static void main(String[] args) throws InterruptedException {
        int N = 10000;
        testWithPrint();
        for (int i = 0; i < N; i++) testStrict();
        for (int i = 0; i < N; i++) test();
        Thread.sleep(1000);
        for (int i = 0; i < 10; i++) testStrict();
        for (int i = 0; i < 10; i++) test();
    }

    public strictfp static void testWithPrint() {
        for (double[] ab : bad) {
            double a = ab[0];
            double b = ab[1];
            double mod = a % b;
            System.out.println("" + a + "(" + toHexRep(a) + ") mod " + b + "(" + toHexRep(b) + ") yields " + mod + "(" + toHexRep(mod) + ")");
        }
        for (double[] ab : good) {
            double a = ab[0];
            double b = ab[1];
            double mod = a % b;
            System.out.println("" + a + "(" + toHexRep(a) + ") mod " + b + "(" + toHexRep(b) + ") yields " + mod + "(" + toHexRep(mod) + ")");
        }
    }

    public strictfp static void testStrict() {
        for (double[] ab : bad) {
            double a = ab[0];
            double b = ab[1];
            double mod = a % b;
            check(mod);
        }
        for (double[] ab : good) {
            double a = ab[0];
            double b = ab[1];
            double mod = a % b;
            check(mod);
        }
    }

    public static void test() {
        for (double[] ab : bad) {
            double a = ab[0];
            double b = ab[1];
            double mod = a % b;
            check(mod);
        }
        for (double[] ab : good) {
            double a = ab[0];
            double b = ab[1];
            double mod = a % b;
            check(mod);
        }
    }

    static String toHexRep(double d) {
        return "0x" + Long.toHexString(Double.doubleToRawLongBits(d)) + "L";
    }

    static void check(double mod) {
        if (Double.isNaN(mod)) {
            throw new Error("Saw a NaN, fail");
        }
    }
}
