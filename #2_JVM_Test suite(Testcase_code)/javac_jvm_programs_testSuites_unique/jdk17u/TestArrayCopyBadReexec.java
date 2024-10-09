
package compiler.arraycopy;

public class TestArrayCopyBadReexec {

    static int val;

    static int[] m1(int[] src, int l) {
        if (src == null) {
            return null;
        }
        int[] dest = new int[10];
        val++;
        try {
            System.arraycopy(src, 0, dest, 0, l);
        } catch (IndexOutOfBoundsException npe) {
        }
        return dest;
    }

    static public void main(String[] args) {
        int[] src = new int[10];
        int[] res = null;
        boolean success = true;
        for (int i = 0; i < 20000; i++) {
            m1(src, 10);
        }
        int val_before = val;
        m1(src, -1);
        if (val - val_before != 1) {
            System.out.println("Bad increment: " + (val - val_before));
            throw new RuntimeException("Test failed");
        }
    }
}
