



public class AntiDependentLoadInOuterStripMinedLoop {
    private static int field;
    private static volatile int barrier;

    public static void main(String[] args) {
        int[] array = new int[1];
        A a = new A();
        for (int i = 0; i < 20_000; i++) {
            test1(array);
            test2(a, array);
            test2_helper(array, 0, 0);
        }
    }

    private static int test1(int[] array) {
        int res = 1;

        for (int i = 0; i < 10; i++) {
            barrier = 1;
            

            for (int j = 0; j < 2000; j++) {
                array[0] = j;  
                res *= j;
            }
        }

        return field + res + field * 2;
    }

    private static int test2(A a, int[] array) {
        int ignore = a.field;
        int res = 1;

        int k = 0;
        for (k = 0; k < 2; k++) {
        }

        for (int i = 0; i < 10; i++) {
            barrier = 1;

            for (int j = 0; j < 2000; j++) {
                test2_helper(array, k, j);
                res *= j;
            }
        }

        return a.field + res + a.field * 2;
    }

    private static void test2_helper(int[] array, int k, int j) {
        if (k == 2) {
            array[0] = j;
        }
    }

    private static class A {
        public int field;
    }
}
