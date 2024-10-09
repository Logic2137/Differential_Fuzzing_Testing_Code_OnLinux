



public class BarrierInInfiniteLoop {
    private static Object field1 = new Object();
    private static Object field2 = new Object();
    private static int field3;

    public static void main(String[] args) {
        test1(false);
        test2(false, false);
        test3(false);
    }

    private static void test1(boolean flag) {
        if (flag) {
            for (;;) {
                field1 = field2;
            }
        }
    }

    private static void test2(boolean flag1, boolean flag2) {
        if (flag1) {
            for (;;) {
                for (;;) {
                    if (flag2) {
                        break;
                    }
                    field1 = field2;
                }
            }
        }
    }

    private static void test3(boolean flag) {
        if (flag) {
            for (;;) {
                for (;;) {
                    field3 = 42;
                    if (field1 == field2) {
                        break;
                    }
                }
            }
        }
    }
}
