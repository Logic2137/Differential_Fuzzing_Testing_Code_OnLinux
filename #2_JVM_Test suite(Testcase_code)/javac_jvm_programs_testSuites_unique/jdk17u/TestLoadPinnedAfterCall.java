public class TestLoadPinnedAfterCall {

    private A staticField1;

    private static Object staticField2 = new Object();

    private static volatile int staticField3;

    private static int staticField4;

    static TestLoadPinnedAfterCall object = new TestLoadPinnedAfterCall();

    public static void main(String[] args) {
        final A a = new A();
        try {
            throw new Exception();
        } catch (Exception ex) {
        }
        for (int i = 0; i < 20_000; i++) {
            inlined(0, 0, 0);
            inlined(2, 0, 0);
            inlined(2, 2, 2);
            object.staticField1 = new A();
            test(true, a, a, false, 2, 2);
            test(false, a, a, true, 2, 2);
            test(false, a, a, false, 2, 2);
            object.staticField1 = a;
            test(true, a, a, false, 2, 2);
            test(false, a, a, true, 2, 2);
            test(false, a, a, false, 2, 2);
        }
    }

    private static void test(boolean flag, A a, A a2, boolean flag2, int i1, int i2) {
        int ii = 1;
        for (; ii < 2; ii *= 2) {
        }
        ii = ii / 2;
        i1 = 0;
        for (; i1 < 2; i1 += ii) {
            for (int i = 0; i < 2; i += ii) {
            }
        }
        i2 = 0;
        for (; i2 < 2; i2 += ii) {
            for (int i = 0; i < 2; i += ii) {
                for (int j = 0; j < 2; j += ii) {
                }
            }
        }
        TestLoadPinnedAfterCall obj = object;
        if (obj == null) {
        }
        counter = 10;
        for (; ; ) {
            synchronized (staticField2) {
            }
            int i = 0;
            for (; i < 2; i += ii) {
            }
            inlined(i, i1, i2);
            if (flag) {
                staticField3 = 0x42;
                break;
            }
            try {
                not_inlined();
                if (flag2) {
                    break;
                }
            } catch (Throwable throwable) {
                if (a == obj.staticField1) {
                    staticField4 = 0x42;
                }
                break;
            }
        }
        if (a2 == obj.staticField1) {
            staticField4 = 0x42;
        }
    }

    private static void inlined(int i, int j, int k) {
        if (i == 2) {
            if (j == 2) {
                staticField3 = 0x42;
            }
            if (k == 2) {
                staticField3 = 0x42;
            }
        }
    }

    static int counter = 0;

    private static void not_inlined() {
        counter--;
        if (counter <= 0) {
            throw new RuntimeException();
        }
    }

    private static class A {
    }
}
