
package compiler.loopopts.superword;

public class TestSearchAlignment {

    public static final int N = 400;

    public static byte[] bArr = new byte[N];

    public static int[] iArr = new int[N];

    public static long[] lArr = new long[N];

    public static void vMeth(byte bArg, int iArg, long lArg) {
        int i = N - 1;
        do {
            iArr[i - 1] += iArg;
            iArr[i] += iArg;
            lArr[i - 1] = lArg;
            bArr[i - 1] = bArg;
        } while (--i > 0);
    }

    public void mainTest() {
        byte b = 0;
        int i = 0;
        long l = 0;
        for (int j = 0; j < N; ++j) {
            vMeth(b, i, l);
        }
    }

    public static void main(String[] args) {
        TestSearchAlignment _instance = new TestSearchAlignment();
        for (int i = 0; i < 10; i++) {
            _instance.mainTest();
        }
        System.out.println("Test passed.");
    }
}
