



package compiler.c1;

public class Test7090976 {

    static interface I1 {
        public void m1();
    };

    static interface I2 {
        public void m2();
    };

    static interface I extends I1,I2 {
    }

    static class A implements I1 {
        int v = 0;
        int v2;

        public void m1() {
            v2 = v;
        }
    }

    static class B implements I2 {
        Object v = new Object();
        Object v2;

        public void m2() {
            v2 = v;
        }
    }

    private void test(A a)
    {
        if (a instanceof I) {
            I i = (I)a;
            i.m1();
            i.m2();
        }
    }

    public static void main(String[] args)
    {
        Test7090976 t = new Test7090976();
        A a = new A();
        B b = new B();
        for (int i = 0; i < 10000; i++) {
            t.test(a);
        }
    }
}
