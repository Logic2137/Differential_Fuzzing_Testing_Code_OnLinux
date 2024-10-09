
package compiler.c2;

public class Test6958485 {

    public static void init(Object[] src, boolean[] dst) {
        for (int i = 0; i < src.length; i++) {
            dst[i] = src[i] != null ? false : true;
        }
    }

    public static void test() {
        Object[] src = new Object[34];
        boolean[] dst = new boolean[34];
        init(src, dst);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 2000; i++) {
            test();
        }
    }
}
