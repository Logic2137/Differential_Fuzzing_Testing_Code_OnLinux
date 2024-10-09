public class TestCastIIAfterUnrollingInOuterLoop {

    public static final int N = 400;

    public static long instanceCount = 727275458L;

    public static int iFld = -10;

    public static volatile short sFld = -2966;

    public static float fFld = 1.682F;

    public static int[] iArrFld = new int[N];

    public static void vMeth1(int i1) {
        int i3 = 4;
        long[] lArr = new long[N], lArr1 = new long[N];
        boolean b = (Integer.reverseBytes(i1 << 5) < (instanceCount++));
        for (int i2 = 1; i2 < 146; i2++) {
            iFld >>= (++i3);
        }
        if (b) {
            for (int i4 = 4; i4 < 218; ++i4) {
                instanceCount = iArrFld[i4 - 1];
                int i10 = 1;
                while (++i10 < 8) {
                    lArr1[i4] += 61384L;
                }
                lArr[i4 + 1] = i4;
                i3 += sFld;
            }
        }
    }

    public void mainTest(String[] strArr1) {
        vMeth1(iFld);
        for (int i19 = 2; i19 < 190; i19++) {
            int i20 = (int) instanceCount;
            instanceCount += (((i19 * i20) + i20) - fFld);
        }
    }

    public static void main(String[] strArr) {
        TestCastIIAfterUnrollingInOuterLoop _instance = new TestCastIIAfterUnrollingInOuterLoop();
        for (int i = 0; i < 10; i++) {
            _instance.mainTest(strArr);
        }
    }
}
