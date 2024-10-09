public class Test7052494 {

    static int test1(int i, int limit) {
        int result = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (i++ != 0) {
            if (result >= limit)
                break;
            result = i * 2;
        }
        return result;
    }

    static int test2(int i, int limit) {
        int result = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (i-- != 0) {
            if (result <= limit)
                break;
            result = i * 2;
        }
        return result;
    }

    static void test3(int i, int limit, int[] arr) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (i++ != 0) {
            if (arr[i - 1] >= limit)
                break;
            arr[i] = i * 2;
        }
    }

    static void test4(int i, int limit, int[] arr) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (i-- != 0) {
            if (arr[arr.length + i + 1] <= limit)
                break;
            arr[arr.length + i] = i * 2;
        }
    }

    static final int limit5 = Integer.MIN_VALUE + 10000;

    static int test5(int i) {
        int result = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (i++ != limit5) {
            result = i * 2;
        }
        return result;
    }

    static final int limit6 = Integer.MAX_VALUE - 10000;

    static int test6(int i) {
        int result = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (i-- != limit6) {
            result = i * 2;
        }
        return result;
    }

    public static void main(String[] args) {
        boolean failed = false;
        int[] arr = new int[8];
        int[] ar3 = { 0, 0, 4, 6, 8, 10, 0, 0 };
        int[] ar4 = { 0, 0, 0, -10, -8, -6, -4, 0 };
        System.out.println("test1");
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 11000; i++) {
            int k = test1(1, 10);
            if (k != 10) {
                System.out.println("FAILED: " + k + " != 10");
                failed = true;
                break;
            }
        }
        System.out.println("test2");
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 11000; i++) {
            int k = test2(-1, -10);
            if (k != -10) {
                System.out.println("FAILED: " + k + " != -10");
                failed = true;
                break;
            }
        }
        System.out.println("test3");
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 11000; i++) {
            java.util.Arrays.fill(arr, 0);
            test3(1, 10, arr);
            if (!java.util.Arrays.equals(arr, ar3)) {
                System.out.println("FAILED: arr = { " + arr[0] + ", " + arr[1] + ", " + arr[2] + ", " + arr[3] + ", " + arr[4] + ", " + arr[5] + ", " + arr[6] + ", " + arr[7] + " }");
                failed = true;
                break;
            }
        }
        System.out.println("test4");
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 11000; i++) {
            java.util.Arrays.fill(arr, 0);
            test4(-1, -10, arr);
            if (!java.util.Arrays.equals(arr, ar4)) {
                System.out.println("FAILED: arr = { " + arr[0] + ", " + arr[1] + ", " + arr[2] + ", " + arr[3] + ", " + arr[4] + ", " + arr[5] + ", " + arr[6] + ", " + arr[7] + " }");
                failed = true;
                break;
            }
        }
        System.out.println("test5");
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 11000; i++) {
            int k = test5(limit6);
            if (k != limit5 * 2) {
                System.out.println("FAILED: " + k + " != " + limit5 * 2);
                failed = true;
                break;
            }
        }
        System.out.println("test6");
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 11000; i++) {
            int k = test6(limit5);
            if (k != limit6 * 2) {
                System.out.println("FAILED: " + k + " != " + limit6 * 2);
                failed = true;
                break;
            }
        }
        System.out.println("finish");
        if (failed)
            System.exit(97);
    }
}
