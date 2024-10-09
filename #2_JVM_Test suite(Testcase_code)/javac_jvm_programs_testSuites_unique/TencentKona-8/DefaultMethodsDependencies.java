



public class DefaultMethodsDependencies {

    interface I1 {
        void m1();
        
        default void m2() {
        }
    }

    interface I2 extends I1 {
        
        default void m1() {
        }
    }

    static abstract class C1 implements I1 {
    }

    static class C2 extends C1 implements I2 {
    }

    static void test(C1 obj) {
        obj.m1();
    }

    static public void main(String[] args) {
        C2 obj = new C2();
        for (int i = 0; i < 20000; i++) {
            test(obj);
        }
    }
}
