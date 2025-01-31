
package compiler.runtime.criticalnatives.argumentcorruption;

public class CheckLongArgs {

    static {
        System.loadLibrary("CNCheckLongArgs");
    }

    static native void m1(long a1, long a2, long a3, long a4, long a5, long a6, long a7, long a8, byte[] result);

    static native void m2(long a1, int[] a2, long a3, int[] a4, long a5, int[] a6, long a7, int[] a8, long a9, byte[] result);

    public static void main(String[] args) throws Exception {
        test();
    }

    private static void test() throws Exception {
        int[] l1 = { 1111, 2222, 3333 };
        int[] l2 = { 4444, 5555, 6666 };
        int[] l3 = { 7777, 8888, 9999 };
        int[] l4 = { 1010, 2020, 3030 };
        byte[] result = { -1 };
        m1(1111111122222222L, 3333333344444444L, 5555555566666666L, 7777777788888888L, 9999999900000000L, 1212121234343434L, 5656565678787878L, 9090909012121212L, result);
        check(result[0]);
        result[0] = -1;
        m2(1111111122222222L, l1, 3333333344444444L, l2, 5555555566666666L, l3, 7777777788888888L, l4, 9999999900000000L, result);
        check(result[0]);
    }

    private static void check(byte result) throws Exception {
        if (result != 2) {
            if (result == 1) {
                throw new Exception("critical native arguments mismatch");
            }
            throw new Exception("critical native lookup failed");
        }
    }
}
