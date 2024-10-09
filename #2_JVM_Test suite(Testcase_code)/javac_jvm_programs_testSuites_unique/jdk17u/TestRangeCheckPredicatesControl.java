
package compiler.loopopts;

public class TestRangeCheckPredicatesControl {

    static Wrapper w1 = new Wrapper();

    static Wrapper w2 = new Wrapper();

    static Wrapper w3 = new Wrapper();

    public static void main(String[] args) {
        for (int x = 0; x < 10000000; x++) {
            test(x % 2 == 0);
            test2(x % 2 == 0, x % 3 == 0);
            test3(x % 2 == 0);
            test4(x % 2 == 0);
        }
    }

    private static class Wrapper {

        long longs;

        int a;

        public void maybeMaskBits(boolean b) {
            if (b) {
                longs &= 0x1F1F1F1F;
            }
        }

        public void maybeMaskBits2(boolean b, boolean c) {
            if (b) {
                longs &= 0x1F1F1F1F;
            }
            if (c) {
                a += 344;
            }
        }
    }

    private static void test(boolean flag) {
        Wrapper[] wrappers_array;
        if (flag) {
            wrappers_array = new Wrapper[] { w1, w2 };
        } else {
            wrappers_array = new Wrapper[] { w1, w2, w3 };
        }
        for (int i = 0; i < wrappers_array.length; i++) {
            wrappers_array[i].maybeMaskBits(flag);
        }
    }

    private static void test2(boolean flag, boolean flag2) {
        Wrapper[] wrappers_array;
        Wrapper[] wrappers_array2;
        if (flag) {
            wrappers_array = new Wrapper[] { w1, w2 };
            wrappers_array2 = new Wrapper[] { w1, w2 };
        } else {
            wrappers_array = new Wrapper[] { w1, w2, w3 };
            wrappers_array2 = new Wrapper[] { w1, w2, w3 };
        }
        for (int i = 0; i < wrappers_array.length; i++) {
            wrappers_array[i].maybeMaskBits(flag);
            wrappers_array2[i].maybeMaskBits2(flag, flag2);
        }
    }

    private static void test3(boolean flag) {
        Wrapper[] wrappers_array;
        if (flag) {
            wrappers_array = new Wrapper[] { w1, w2 };
        } else {
            wrappers_array = new Wrapper[] { w1, w2, w3 };
        }
        for (int i = 0; i < wrappers_array.length; i++) {
            wrappers_array[i].longs &= 0x1F1F1F1F;
        }
    }

    private static void test4(boolean flag) {
        Wrapper[] wrappers_array;
        Wrapper[] wrappers_array2;
        if (flag) {
            wrappers_array = new Wrapper[] { w1, w2 };
            wrappers_array2 = new Wrapper[] { w1, w2 };
        } else {
            wrappers_array = new Wrapper[] { w1, w2, w3 };
            wrappers_array2 = new Wrapper[] { w1, w2, w3 };
        }
        for (int i = 0; i < wrappers_array.length; i++) {
            wrappers_array[i].longs &= 0x1F1F1F1F;
            wrappers_array2[i].longs &= 0x1F1F1F1F;
        }
    }
}
