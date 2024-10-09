



public class LRBRightAfterMemBar {
    private static A field1;
    private static Object field2;
    static volatile int barrier;

    public static void main(String[] args) {
        for (int i = 0; i < 20_000; i++) {
            test1(true, true, new Object());
            test1(false, false, new Object());
            test2(new Object(), 0, 10);
        }
    }

    private static Object test1(boolean flag, boolean flag2, Object o2) {
        for (int i = 0; i < 10; i++) {
            barrier = 0x42; 
            if (o2 == null) { 
            }
            
            
            
            
            Object o = flag ? field1 : field2;
            if (flag2) {
                return o;
            }
        }

        return null;
    }

    private static int test2(Object o2, int start, int stop) {
        A a1 = null;
        A a2 = null;
        int v = 0;
        for (int i = start; i < stop; i++) {
            a2 = new A();
            a1 = new A();
            a1.a = a2;
            barrier = 0x42; 
            if (o2 == null) { 
            }
            A a3 = a1.a;
            v = a3.f; 
        }

        a1.f = 0x42;
        a2.f = 0x42;

        return v;
    }

    static class A {
        A a;
        int f;
    }
}
