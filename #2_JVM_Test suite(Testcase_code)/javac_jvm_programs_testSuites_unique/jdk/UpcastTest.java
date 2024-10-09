



package compiler.c2;

public class UpcastTest {
    static class Test1 {
        interface I { void m(); }

        static abstract class AC implements I {
            public void m() {}
        }

        static class T extends AC {
            int i = 0;

            static {
                
                for (int i = 0; i < 20_000; i++) {
                    test(new T(){});
                    test(new T(){});
                    test(new T(){});
                }
            }
        }

        static void test(T t) {
            
            

            ((I)t).m(); 

            
            
            t.i = 1;
        }
    }

    static class Test2 {
        interface I { void m(); }
        interface J extends I {
            default void m() {}
        }

        static abstract class AC implements I {
        }

        static abstract class T extends AC {
            int i = 0;

            static {
                
                for (int i = 0; i < 20_000; i++) {
                    test(new T1(){});
                    test(new T2(){});
                    test(new T3(){});
                }
            }
        }

        static class T1 extends T implements J {}
        static class T2 extends T implements J {}
        static class T3 extends T implements J {}

        static void test(T t) {
            
            

            ((I)t).m(); 

            
            
            t.i = 1;
        }
    }

    static class Test3 {
        interface I {
            default void m1() { m2(); }
            void m2();
        }
        interface J extends I {
            default void m2() {}
        }

        static abstract class AC implements I {}

        static class T extends AC implements J {
            int i = 0;
        }

        static void test(T t) {
            t.m1(); 

            
            
            t.i = 1;
        }

        static void run() {
            for (int i = 0; i < 20_000; i++) {
                test(new T() {});
                test(new T() {});
                test(new T() {});
            }
        }
    }

    static class Test4 {
        interface I { default void m() {}}

        static class T {
            int i = 0;
        }

        static class D extends T implements I {}

        static void test(T t) {
            if (t instanceof I) {
                ((I)t).m();

                
                

                t.i = 1;
            } else {
                throw new InternalError();
            }
        }

        static void run() {
            for (int i = 0; i < 20_000; i++) {
                test(new D() {});
                test(new D() {});
                test(new D() {});
            }
        }
    }

    public static void main(String[] args) {
        new Test1.T();  
        new Test2.T1(); 
        Test3.run();
        Test4.run();
    }
}
