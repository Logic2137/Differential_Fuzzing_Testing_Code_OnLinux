public class SplitIfSharedFastLockBehindCastPP {

    private static boolean field;

    private static A obj_field;

    public static void main(String[] args) {
        A lock = new A();
        obj_field = lock;
        for (int i = 0; i < 20_000; i++) {
            test1(true, lock);
            test1(false, lock);
            test2(true);
            test2(false);
        }
    }

    private static void test1(boolean flag, Object obj) {
        if (obj == null) {
        }
        boolean flag2;
        if (flag) {
            flag2 = true;
        } else {
            flag2 = false;
            obj = obj_field;
        }
        for (int i = 0; i < 100; i++) {
            if (flag2) {
                field = true;
            } else {
                field = false;
            }
            synchronized (obj) {
                field = true;
            }
        }
    }

    private static Object test2(boolean flag) {
        int integer;
        if (flag) {
            field = true;
            integer = 1;
        } else {
            field = false;
            integer = 2;
        }
        Object obj = integer;
        for (int i = 0; i < 100; i++) {
            if (integer == 1) {
                field = true;
            } else {
                field = false;
            }
            synchronized (obj) {
                field = true;
            }
        }
        return obj;
    }

    private static final class A {
    }
}
