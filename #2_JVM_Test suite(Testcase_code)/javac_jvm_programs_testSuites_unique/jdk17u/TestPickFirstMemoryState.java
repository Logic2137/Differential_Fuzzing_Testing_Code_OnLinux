
package compiler.loopopts.superword;

public class TestPickFirstMemoryState {

    static int[] iArrFld = new int[50];

    static int test() {
        int x = 2;
        for (int i = 50; i > 9; i--) {
            x = 2;
            for (int j = 10; j < 50; j++) {
                x += iArrFld[j];
                iArrFld[j] = j;
            }
            reset();
        }
        return x;
    }

    static int test2() {
        int x = 2;
        int y = 3;
        for (int i = 50; i > 9; i--) {
            x = 2;
            for (int j = 10; j < 50; j++) {
                x += iArrFld[j];
                iArrFld[j] = (y++);
            }
            reset();
        }
        return x;
    }

    static int test3() {
        int x = 2;
        for (int i = 50; i > 9; i--) {
            x = 2;
            int y = i;
            for (int j = 10; j < 50; j++) {
                y++;
                x += iArrFld[j];
                iArrFld[j] = y;
            }
            reset();
        }
        return x;
    }

    static int test4() {
        int x = 2;
        long y = 3L;
        for (int i = 50; i > 9; i--) {
            x = 2;
            for (int j = 10; j < 50; j++) {
                x += iArrFld[j];
                iArrFld[j] = (int) (y++);
            }
            reset();
        }
        return x;
    }

    public static void main(String[] strArr) {
        for (int i = 0; i < 5000; i++) {
            reset();
            int x = test();
            if (x != 202) {
                throw new RuntimeException("test() wrong result: " + x);
            }
            x = test2();
            if (x != 202) {
                throw new RuntimeException("test2() wrong result: " + x);
            }
            x = test3();
            if (x != 202) {
                throw new RuntimeException("test3() wrong result: " + x);
            }
            x = test4();
            if (x != 202) {
                throw new RuntimeException("test4() wrong result: " + x);
            }
        }
    }

    public static void reset() {
        for (int i = 0; i < iArrFld.length; i++) {
            iArrFld[i] = 5;
        }
    }
}
