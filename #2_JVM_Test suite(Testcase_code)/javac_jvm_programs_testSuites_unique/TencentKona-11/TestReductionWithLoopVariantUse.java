



package compiler.loopopts.superword;

public class TestReductionWithLoopVariantUse {
    static int m(int[] array) {
        int c = 0;
        for (int i = 0; i < 256; i++) {
            c += array[i];
            array[i] = c;
        }
        return c;
    }

    static public void main(String[] args) {
        int[] array = new int[256];
        int[] array2 = new int[256];
        for (int j = 0; j < 256; j++) {
            array2[j] = j;
        }
        for (int i = 0; i < 20000; i++) {
            System.arraycopy(array2, 0, array, 0, 256);
            int res = m(array);
            boolean success = true;
            int c = 0;
            for (int j = 0; j < 256; j++) {
                c += array2[j];
                if (array[j] != c) {
                    System.out.println("Failed for " + j + " : " + array[j] + " != " + c);
                    success = false;
                }
            }
            if (c != res) {
                System.out.println("Failed for sum: " + c + " != " + res);
            }
            if (!success) {
                throw new RuntimeException("Test failed");
            }
        }
    }
}
