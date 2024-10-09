import java.util.Arrays;

public class TestLoadBypassACWithWrongMem {

    static int test1(int[] src) {
        int[] dst = new int[10];
        System.arraycopy(src, 0, dst, 0, 10);
        src[1] = 0x42;
        return dst[1];
    }

    static int test2(int[] src) {
        int[] dst = (int[]) src.clone();
        src[1] = 0x42;
        return dst[1];
    }

    static Object test5_src = null;

    static int test3() {
        int[] dst = new int[10];
        System.arraycopy(test5_src, 0, dst, 0, 10);
        ((int[]) test5_src)[1] = 0x42;
        System.arraycopy(test5_src, 0, dst, 0, 10);
        return dst[1];
    }

    static public void main(String[] args) {
        int[] src = new int[10];
        for (int i = 0; i < 20000; i++) {
            Arrays.fill(src, 0);
            int res = test1(src);
            if (res != 0) {
                throw new RuntimeException("bad result: " + res + " != " + 0);
            }
            Arrays.fill(src, 0);
            res = test2(src);
            if (res != 0) {
                throw new RuntimeException("bad result: " + res + " != " + 0);
            }
            Arrays.fill(src, 0);
            test5_src = src;
            res = test3();
            if (res != 0x42) {
                throw new RuntimeException("bad result: " + res + " != " + 0x42);
            }
        }
    }
}
