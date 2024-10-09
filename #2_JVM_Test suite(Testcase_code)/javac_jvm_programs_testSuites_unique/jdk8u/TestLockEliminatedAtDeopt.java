public class TestLockEliminatedAtDeopt {

    static class A {

        void m() {
        }

        synchronized void m2(boolean trap) {
            if (trap) {
                new B();
            }
        }
    }

    static class B extends A {

        void m() {
        }
    }

    static void m1(boolean trap) {
        A a = new A();
        synchronized (a) {
            a.m2(trap);
            a.m();
        }
    }

    public static void main(String[] args) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 20000; i++) {
            m1(false);
        }
        m1(true);
    }
}
