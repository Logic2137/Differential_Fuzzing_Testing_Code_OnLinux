
package compiler.arraycopy;

public class TestEliminatedArrayCopyPhi {

    static int[] escaped;

    static void test(int[] src, boolean flag1, boolean flag2) {
        int[] array = new int[10];
        if (flag1) {
            System.arraycopy(src, 0, array, 0, src.length);
        } else {
        }
        if (flag2) {
            escaped = array;
        }
    }

    public static void main(String[] args) {
        int[] src = new int[10];
        for (int i = 0; i < 20000; i++) {
            test(src, (i % 2) == 0, false);
        }
    }
}
