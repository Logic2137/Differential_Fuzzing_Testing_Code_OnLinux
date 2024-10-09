



package compiler.vectorization;

public class TestAbsCharVector {

    private static int SIZE = 60000;

    public static void main(String args[]) {
        char[] a = new char[SIZE];
        char[] b = new char[SIZE];

        for (int i = 0; i < SIZE; i++) {
            a[i] = b[i] = (char) i;
        }

        for (int i = 0; i < 20000; i++) {
            arrayAbs(a);
        }

        for (int i = 0; i < SIZE; i++) {
            if (a[i] != b[i]) {
                throw new RuntimeException("Broken!");
            }
        }
    }

    private static void arrayAbs(char[] arr) {
        for (int i = 0; i < SIZE; i++) {
            arr[i] = (char) Math.abs(arr[i]);
        }
    }
}

