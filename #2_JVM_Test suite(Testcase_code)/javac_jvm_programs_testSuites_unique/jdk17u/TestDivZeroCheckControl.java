
package compiler.loopopts;

public class TestDivZeroCheckControl {

    public static int test1(int div, int[] array) {
        int res = 0;
        for (int i = 0; i < 256; i++) {
            int j = 0;
            do {
                array[i] = i;
                try {
                    res = 1 % div;
                } catch (ArithmeticException ex) {
                }
            } while (++j < 9);
        }
        return res;
    }

    public static int test2(int div, int[] array) {
        int res = 0;
        for (int i = 0; i < 256; i++) {
            int j = 0;
            do {
                array[i] = i;
                try {
                    res = 1 / div;
                } catch (ArithmeticException ex) {
                }
            } while (++j < 9);
        }
        return res;
    }

    public static long test3(long div, int[] array) {
        long res = 0;
        for (int i = 0; i < 256; i++) {
            int j = 0;
            do {
                array[i] = i;
                try {
                    res = 1L % div;
                } catch (ArithmeticException ex) {
                }
            } while (++j < 9);
        }
        return res;
    }

    public static long test4(long div, int[] array) {
        long res = 0;
        for (int i = 0; i < 256; i++) {
            int j = 0;
            do {
                array[i] = i;
                try {
                    res = 1L / div;
                } catch (ArithmeticException ex) {
                }
            } while (++j < 9);
        }
        return res;
    }

    public static void main(String[] args) {
        int[] array = new int[256];
        for (int i = 0; i < 50_000; ++i) {
            test1(0, array);
            test2(0, array);
            test3(0, array);
            test4(0, array);
        }
    }
}
