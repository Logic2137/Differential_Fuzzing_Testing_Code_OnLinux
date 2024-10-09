
package compiler.types;

public class TestPhiElimination {

    public Object test(TestPhiElimination obj) {
        if (obj instanceof A) {
            return ((A) obj).get();
        }
        return null;
    }

    static public void main(String[] args) {
        TestPhiElimination t = new TestPhiElimination();
        B b = new B();
        for (int i = 0; i < 1_000; ++i) {
            t.test(b);
        }
        A a = new A();
        for (int i = 0; i < 20_000; ++i) {
            if (i % 2 == 0) {
                a.f = null;
            }
            t.test(a);
        }
    }

    static class A extends TestPhiElimination {

        public Object f;

        public A create() {
            return new A();
        }

        public synchronized Object get() {
            if (f == null) {
                f = create();
            }
            return f;
        }
    }

    static class B extends A {
    }
}
