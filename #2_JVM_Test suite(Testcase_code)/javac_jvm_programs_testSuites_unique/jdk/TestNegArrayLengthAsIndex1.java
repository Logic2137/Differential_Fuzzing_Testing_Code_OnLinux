



package compiler.arraycopy;
public class TestNegArrayLengthAsIndex1 {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10000; i++) {
            foo();
        }
    }

    static int foo() {
        int minusOne = -1;
        int[] a = null;
        try {
            a = new int[minusOne];
        } catch (NegativeArraySizeException e) {
           return 0;
        }
        return a[a.length - 1];
    }
}
