public class TestArrayMeetNotSymmetrical {

    private static final Object field = new Object[0];

    private static final Object field2 = new A[0];

    public static void main(String[] args) {
        Object array = new A[10];
        for (int i = 0; i < 20_000; i++) {
            test1(true, 10);
            test1(false, 10);
            test2(true);
            test2(false);
        }
    }

    private static Object test1(boolean flag, int len) {
        Object o;
        if (flag) {
            o = field;
        } else {
            o = new A[len];
        }
        return o;
    }

    private static Object test2(boolean flag) {
        Object o;
        if (flag) {
            o = field;
        } else {
            o = field2;
        }
        return o;
    }

    private static class A {
    }
}
