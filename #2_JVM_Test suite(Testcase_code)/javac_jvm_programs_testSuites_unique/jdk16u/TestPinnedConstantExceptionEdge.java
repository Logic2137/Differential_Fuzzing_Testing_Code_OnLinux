


package compiler.c1;

public class TestPinnedConstantExceptionEdge {

    public static long iFld = 0;
    public static boolean b1;
    public static boolean b2;

    public static void test() {
        int x = 5;
        int y = 11;
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 2; ++j) {
                if (b1) {
                    try {
                        y = (x / x);
                        y = (500 / i);
                        y = (-214 / i);
                    } catch (ArithmeticException a_e) {}
                    
                    iFld += (b1 ? 1 : 0) + (b2 ? 1 : 0) + 5 + 7 + 6 + 5 + y
                            + dontInline(7) + dontInline(5) + 8 + 8 + 9
                            + dontInline(3) + dontInline(3) + dontInline(4)
                            + dontInline(dontInline(5)) + dontInline(2);
                    return;
                }
            }
        }
    }

    
    public static int dontInline(int a) {
        return 0;
    }

    public static void main(String[] strArr) {
        test();
    }
}

