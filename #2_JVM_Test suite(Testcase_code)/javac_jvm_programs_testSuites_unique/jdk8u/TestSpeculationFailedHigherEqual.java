public class TestSpeculationFailedHigherEqual {

    static class A {

        void m() {
        }

        int i;
    }

    static class C extends A {
    }

    static C c;

    static A m1(A a, boolean cond) {
        if (cond) {
            a = c;
        }
        int i = a.i;
        return a;
    }

    static public void main(String[] args) {
        C c = new C();
        TestSpeculationFailedHigherEqual.c = c;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 20000; i++) {
            m1(c, i % 2 == 0);
        }
        System.out.println("TEST PASSED");
    }
}
