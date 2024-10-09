



package compiler.loopopts;

public class TestUnswitchOverunrolling {

    public static int v = 1;
    public static int w = 2;
    public static int x = 3;
    public static int y = 4;
    public static int z = 5;

    
    
    
    public static int test(int[] array) {
        int result = 0;
        int[] iArr = new int[8];
        for (int i = 0; i < array.length; i++) {
            for (int j = 5; j < i; j++) {
                if (x == 42) {
                    y = 34;
                }
                iArr[j] += array[j];
                result += array[j];
            }
        }
        return result;
    }

    
    public static int test2(int[] array, A a, A a2) {
        int result = 0;
        int[] iArr = new int[8];
        for (int i = 0; i < array.length; i++) {
            for (int j = 5; j < i; j++) {
                y = a2.iFld;
                if (x == 42) {
                    y = 34;
                }
                z = a.iFld;
                iArr[j] += array[j];
                result += array[j];
            }
        }
        return result;
    }

    
    public static int testUnswitchTwice(int[] array, A a, A a2, A a3) {
        int result = 0;
        int[] iArr = new int[8];
        for (int i = 0; i < array.length; i++) {
            for (int j = 5; j < i; j++) {
                y = a2.iFld;
                if (x == 42) {
                    y = 34;
                }
                z = a.iFld;
                if (w == 22) {
                    y = 55;
                }
                v = a3.iFld;
                iArr[j] += array[j];
                result += array[j];
            }
        }
        return result;
    }

    
    public static int testUnswitchThreeTimes(int[] array, A a, A a2, A a3) {
        int result = 0;
        int[] iArr = new int[8];
        for (int i = 0; i < array.length; i++) {
            for (int j = 5; j < i; j++) {
                y += a2.iFld;
                if (x == 42) {
                    y = 34;
                }
                if (w == 22) {
                    y = 55;
                }
                v = a3.iFld;
                if (z == 75) {
                    y = 66;
                }
                y += a.iFld + a2.iFld + a3.iFld;
                iArr[j] += array[j];
                result += array[j];
            }
        }
        return result;
    }

    public static void main(String args[]) {
        int[] array = new int[8];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }

        check(test(array), 1, 2, 3, 4, 5);
        check(test2(array, new A(), new A()), 1, 2, 3, 6, 6);
        check(testUnswitchTwice(array, new A(), new A(), new A()), 6, 2, 3, 6, 6);
        check(testUnswitchThreeTimes(array, new A(), new A(), new A()), 6, 2, 3, 78, 6);

        for (int i = 0; i < array.length; i++) {
            if (array[i] != i + 1) {
                throw new RuntimeException("array values should not change");
            }
        }
    }

    public static void check(int result, int expV, int expW, int expX, int expY, int expZ) {
        if (result != 19 || expV != v || expW != w || expX != x || expY != y || expZ != z) {
            throw new RuntimeException("wrong execution");
        }
    }
}

class A {
    int iFld = 6;
}

