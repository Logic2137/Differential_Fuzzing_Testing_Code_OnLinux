



public class LRBRightAfterMemBar {
    private static Object field1;
    private static Object field2;
    static volatile int barrier;

    public static void main(String[] args) {
        for (int i = 0; i < 20_000; i++) {
            test(true, true, new Object());
            test(false, false, new Object());
        }
    }

    private static Object test(boolean flag, boolean flag2, Object o2) {
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
}
