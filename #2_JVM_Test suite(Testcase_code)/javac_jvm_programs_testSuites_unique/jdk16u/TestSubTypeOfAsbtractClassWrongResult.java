




public class TestSubTypeOfAsbtractClassWrongResult {
    public static void main(String[] args) {
        for (int i = 0; i < 20_000; i++) {
            if (!test1(A.class)) {
                throw new RuntimeException("Wrong result");
            }
            test2(new Object());
            test3(new Exception());
        }
    }

    private static boolean test1(Class c) {
        return A.class.isAssignableFrom(c);
    }

    private static boolean test2(Object o) {
        return o instanceof A;
    }

    private static void test3(Exception e) {
        try {
            throw e;
        } catch (A ex1) {
        } catch (Exception ex2) {
        }
    }

    static abstract class A extends Exception {
    }
}
