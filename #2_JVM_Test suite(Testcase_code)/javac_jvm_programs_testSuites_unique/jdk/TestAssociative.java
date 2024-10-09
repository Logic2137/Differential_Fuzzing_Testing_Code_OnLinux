



public class TestAssociative {
    private static class IntParams {
        int a;
        int b;
        int c;
        public IntParams(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    private static class LongParams {
        long a;
        long b;
        long c;
        public LongParams(long a, long b, long c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    private static final IntParams[]  intParamsArray = {
        new IntParams(17, 34, 10),
        new IntParams(Integer.MAX_VALUE - 4, 34, 10),
        new IntParams(7, Integer.MAX_VALUE, 10),
        new IntParams(7, Integer.MAX_VALUE, Integer.MAX_VALUE),
        new IntParams(10, Integer.MIN_VALUE, Integer.MIN_VALUE),
        new IntParams(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE)
    };

    private static final LongParams[] longParamsArray = {
        new LongParams(17, 34, 10),
        new LongParams(Long.MAX_VALUE - 4, 34, 10),
        new LongParams(7, Long.MAX_VALUE, 10),
        new LongParams(7, Long.MAX_VALUE, Long.MAX_VALUE),
        new LongParams(10, Long.MIN_VALUE, Long.MIN_VALUE),
        new LongParams(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE)
    };

    
    private static interface IntAssociativeTest {
        public int test(int a, int b, int c);
    }

    
    private static class IntAssociativeTest0 implements IntAssociativeTest {
        public int test(int a, int b, int c) {
            return a * b + a * c;
        }
    }

    private static class IntAssociativeTest1 implements IntAssociativeTest {
        public int test(int a, int b, int c) {
            return a * b + b * c;
        }
    }

    private static class IntAssociativeTest2 implements IntAssociativeTest {
        public int test(int a, int b, int c) {
            return a * c +  b * c;
        }
    }

    private static class IntAssociativeTest3 implements IntAssociativeTest {
        public int test(int a, int b, int c) {
            return a * b + c * a;
        }
    }

    
    private static class IntAssociativeTest4 implements IntAssociativeTest {
        public int test(int a, int b, int c) {
            return a * b - a * c;
        }
    }

    private static class IntAssociativeTest5 implements IntAssociativeTest {
        public int test(int a, int b, int c) {
            return a * b - b * c;
        }
    }

    private static class IntAssociativeTest6 implements IntAssociativeTest {
        public int test(int a, int b, int c) {
            return a * c -  b * c;
        }
    }

    private static class IntAssociativeTest7 implements IntAssociativeTest {
        public int test(int a, int b, int c) {
            return a * b - c * a;
        }
    }


    
    private static interface LongAssociativeTest {
        public long test(long a, long b, long c);
    }

    
    private static class LongAssociativeTest0 implements LongAssociativeTest {
        public long test(long a, long b, long c) {
            return a * b + a * c;
        }
    }

    private static class LongAssociativeTest1 implements LongAssociativeTest {
        public long test(long a, long b, long c) {
            return a * b + b * c;
        }
    }

    private static class LongAssociativeTest2 implements LongAssociativeTest {
        public long test(long a, long b, long c) {
            return a * c +  b * c;
        }
    }

    private static class LongAssociativeTest3 implements LongAssociativeTest {
        public long test(long a, long b, long c) {
            return a * b + c * a;
        }
    }

    
    private static class LongAssociativeTest4 implements LongAssociativeTest {
        public long test(long a, long b, long c) {
            return a * b - a * c;
        }
    }

    private static class LongAssociativeTest5 implements LongAssociativeTest {
        public long test(long a, long b, long c) {
            return a * b - b * c;
        }
    }

    private static class LongAssociativeTest6 implements LongAssociativeTest {
        public long test(long a, long b, long c) {
            return a * c -  b * c;
        }
    }

    private static class LongAssociativeTest7 implements LongAssociativeTest {
        public long test(long a, long b, long c) {
            return a * b - c * a;
        }
    }

    private static void runIntTest(IntAssociativeTest t) {
        for (IntParams p : intParamsArray) {
            int result = t.test(p.a, p.b, p.c);
            for (int i = 0; i < 20_000; i++) {
                if (result != t.test(p.a, p.b, p.c)) {
                    throw new RuntimeException("incorrect result");
                }
            }
        }
    }

    private static void runLongTest(LongAssociativeTest t) {
        for (LongParams p : longParamsArray) {
            long result = t.test(p.a, p.b, p.c);
            for (int i = 0; i < 20_000; i++) {
                if (result != t.test(p.a, p.b, p.c)) {
                    throw new RuntimeException("incorrect result");
                }
            }
        }
    }

    private static final IntAssociativeTest[] intTests = {
        new IntAssociativeTest0(),
        new IntAssociativeTest1(),
        new IntAssociativeTest2(),
        new IntAssociativeTest3(),
        new IntAssociativeTest4(),
        new IntAssociativeTest5(),
        new IntAssociativeTest6(),
        new IntAssociativeTest7()
    };

    private static final LongAssociativeTest[] longTests = {
        new LongAssociativeTest0(),
        new LongAssociativeTest1(),
        new LongAssociativeTest2(),
        new LongAssociativeTest3(),
        new LongAssociativeTest4(),
        new LongAssociativeTest5(),
        new LongAssociativeTest6(),
        new LongAssociativeTest7()
    };

    public static void main(String[] args) {
        for (IntAssociativeTest t: intTests) {
            runIntTest(t);
        }

        for (LongAssociativeTest t: longTests) {
            runLongTest(t);
        }
    }
}
