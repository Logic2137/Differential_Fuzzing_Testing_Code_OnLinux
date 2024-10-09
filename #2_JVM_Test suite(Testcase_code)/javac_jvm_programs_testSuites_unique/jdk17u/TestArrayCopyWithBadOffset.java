
package compiler.arraycopy;

public class TestArrayCopyWithBadOffset {

    public static byte[] getSrc() {
        return new byte[5];
    }

    public static void test1(byte[] dst) {
        byte[] src = getSrc();
        try {
            System.arraycopy(src, Integer.MAX_VALUE - 1, dst, 0, src.length);
        } catch (Exception e) {
        }
    }

    public static byte[] getDst() {
        return new byte[5];
    }

    public static void test2(byte[] src) {
        byte[] dst = getDst();
        try {
            System.arraycopy(src, 0, dst, Integer.MAX_VALUE - 1, dst.length);
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        byte[] array = new byte[5];
        for (int i = 0; i < 10_000; ++i) {
            test1(array);
            test2(array);
        }
    }
}
