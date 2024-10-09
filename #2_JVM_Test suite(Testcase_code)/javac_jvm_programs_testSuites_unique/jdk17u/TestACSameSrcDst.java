public class TestACSameSrcDst {

    static int test1(int[] src, int[] dst) {
        System.arraycopy(src, 5, dst, 0, 10);
        return dst[0];
    }

    static int test2(int[] src) {
        System.arraycopy(src, 0, src, 0, 10);
        return src[0];
    }

    static int test3() {
        int[] src = new int[15];
        src[5] = 0x42;
        System.arraycopy(src, 5, src, 0, 10);
        return src[0];
    }

    static int test4() {
        int[] src = new int[15];
        System.arraycopy(src, 0, src, 5, 10);
        return src[0];
    }

    static int test5(Object src, int l, boolean flag) {
        int[] dst = new int[10];
        if (flag) {
            dst[0] = 0x42;
            System.arraycopy(src, 0, dst, 0, l);
            return dst[0];
        }
        return 0;
    }

    public static void main(String[] args) {
        int[] array = new int[15];
        for (int i = 0; i < 20000; i++) {
            int res;
            for (int j = 0; j < array.length; j++) {
                array[j] = j;
            }
            int expected = array[5];
            res = test1(array, array);
            if (res != expected) {
                throw new RuntimeException("bad result: " + res + " != " + expected);
            }
            test2(array);
            res = test3();
            if (res != 0x42) {
                throw new RuntimeException("bad result: " + res + " != " + 0x42);
            }
            test4();
            for (int j = 0; j < array.length; j++) {
                array[j] = j;
            }
            res = test5(array, 10, (i % 2) == 0);
            if (res != 0) {
                throw new RuntimeException("bad result: " + res + " != " + 0);
            }
        }
    }
}
