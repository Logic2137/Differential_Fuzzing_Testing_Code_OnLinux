



public class TestSubTypeCheckMacroTrichotomy {
    public static void main(String[] args) {
        for (int i = 0; i < 20_000; i++) {
            final int res1 = test(A.class, B.class);
            final int res2 = test(B.class, A.class);
            final int res3 = test(A.class, C.class);
            if (res1 != 0 || res2 != 1 || res3 != 0) {
                throw new RuntimeException("test(A, B) = " + res1 + " test(B, A) = " + res2 + " test(A, C) = " + res3);
            }
        }
    }

    private static int test(Class<?> c1, Class<?> c2) {
        if (c1 == null) {
        }
        if (c2 == null) {
        }
        int res = 0;
        if (!c1.isAssignableFrom(c2)) {
            if (c2.isAssignableFrom(c1)) {
                res = 1;
            }
        }
        return res;
    }

    private static class A {
    }

    private static class B extends A {
    }

    private static class C {
    }
}
