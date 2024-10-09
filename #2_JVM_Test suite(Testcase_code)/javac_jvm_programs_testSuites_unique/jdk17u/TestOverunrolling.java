
package compiler.loopopts;

public class TestOverunrolling {

    public static Object test1(int arg) {
        Object[] arr = new Object[3];
        int lim = (arg & 3);
        for (int i = 0; i < lim; ++i) {
            arr[i] = new Object();
        }
        return arr;
    }

    public static long lFld = 0;

    public static volatile double dFld = 0;

    public static void test2() {
        int[] iArr = new int[10];
        for (int i = 6; i < 10; i++) {
            for (int j = 8; j > i; j--) {
                int k = 1;
                do {
                    iArr[j] = 0;
                    switch(k) {
                        case 1:
                            lFld = 0;
                            break;
                        case 10:
                            dFld = 0;
                            break;
                    }
                } while (++k < 1);
            }
        }
    }

    public static void test3(int[] array) {
        int[] iArr = new int[8];
        for (int i = 0; i < array.length; i++) {
            for (int j = 5; j < i; j++) {
                int k = 1;
                do {
                    iArr[j] = 0;
                    switch(k) {
                        case 1:
                            lFld = 0;
                            break;
                        case 10:
                            dFld = 0;
                            break;
                    }
                } while (++k < 1);
            }
        }
    }

    public static void test4(int[] array, boolean store) {
        int[] iArr = new int[8];
        for (int i = -8; i < 8; i++) {
            for (int j = 5; j > i; j--) {
                int k = 1;
                do {
                    if (store) {
                        iArr[j] = 0;
                    }
                    switch(k) {
                        case 1:
                            lFld = 0;
                            break;
                        case 10:
                            dFld = 0;
                            break;
                    }
                } while (++k < 1);
            }
        }
    }

    public static int test5(int[] array) {
        int result = 0;
        int[] iArr = new int[8];
        for (int i = 0; i < array.length; i++) {
            for (int j = 5; j < i; j++) {
                iArr[j] += array[j];
                result += array[j];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 42; ++i) {
            test1(i);
        }
        test2();
        int[] array = new int[8];
        test3(array);
        test4(array, false);
        test5(array);
    }
}
