



public class TestMeetTopArrayExactConstantArray {

    static class A {
    }

    static class B {
    }

    static class C extends A {
    }

    static class D extends C {
    }

    final static B[] b = new B[10];

    static void m0(Object[] o) {
        if (o.getClass() ==  Object[].class) {
        }
    }

    static void m1(Object[] o, boolean cond) {
        if (cond) {
            o = b;
        }
        m0(o);
    }

    static void m2(Object[] o, boolean cond1, boolean cond2) {
        if (cond1) {
            m1(o, cond2);
        }
    }

    static void m3(C[] o, boolean cond1, boolean cond2, boolean cond3) {
        if (cond1) {
            m2(o, cond2, cond3);
        }
    }

    static public void main(String[] args) {
        A[] a = new A[10];
        D[] d = new D[10];
        Object[] o = new Object[10];
        for (int i = 0; i < 5000; i++) {
            
            m0(o);
            
            m2(a, true, (i%2) == 0);
            
            m3(d, true, false, (i%2) == 0);
        }

        
        C[] c = new C[10];
        for (int i = 0; i < 20000; i++) {
            m3(c, true, false, (i%2) == 0);
        }
        
        m3(c, true, true, false);
        m3(c, true, true, false);
        m3(c, true, true, false);
        m3(c, true, true, false);

        
        
        
        
        
        for (int i = 0; i < 20000; i++) {
            m3(c, true, false, (i%2) == 0);
        }

        System.out.println("TEST PASSED");
    }
}
