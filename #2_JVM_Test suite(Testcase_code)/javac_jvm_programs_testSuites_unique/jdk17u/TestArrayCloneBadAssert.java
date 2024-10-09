
package compiler.arraycopy;

public class TestArrayCloneBadAssert {

    static final int[] array = new int[5];

    static int[] m(int[] arr) {
        int i = 0;
        for (; i < 2; i++) {
        }
        if (i == 2) {
            arr = array;
        }
        return arr.clone();
    }

    static public void main(String[] args) {
        int[] arr = new int[5];
        m(arr);
    }
}
