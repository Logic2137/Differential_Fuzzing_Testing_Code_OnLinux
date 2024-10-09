



public class TestBadRawMemoryAfterCall {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();
        C c = new C();
        for (int i = 0; i < 20_000; i++) {
            test(a);
            test(b);
            test(c);
        }
    }

    private static Object test(A a) {
        if (a.getClass() == A.class) {
        }

        Object o = null;
        try {
            a.m();
            o = a.getClass();
        } catch (Exception e) {

        }
        return o;
    }

    private static class A {
        void m() throws Exception {
            throw new Exception();
        }
    }
    private static class B extends A {
        void m() {
        }
    }
    private static class C extends B {
        void m() {
        }
    }
}
