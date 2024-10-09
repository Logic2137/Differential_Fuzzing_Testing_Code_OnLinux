
package compiler.loopopts.superword;

public class AlignmentOnePack {

    static int iFld;

    public static void test(int[] intArr, short[] shortArr) {
        for (int j = 8; j < intArr.length; j++) {
            shortArr[10] = 10;
            shortArr[j] = 30;
            intArr[7] = 260;
            intArr[j - 1] = 400;
            iFld = intArr[j];
        }
    }

    public static void main(String[] args) throws Exception {
        int[] a = new int[16];
        short[] c = new short[16];
        for (int i = 0; i < 10000; i++) {
            test(a, c);
        }
    }
}
