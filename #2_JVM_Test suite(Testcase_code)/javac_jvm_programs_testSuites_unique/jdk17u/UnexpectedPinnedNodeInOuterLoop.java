public class UnexpectedPinnedNodeInOuterLoop {

    public static final int N = 400;

    public static volatile float fFld = 0.488F;

    public static volatile int iFld = 143;

    public static void lMeth(int i2) {
        int i20 = 95, i21 = -163, i22 = -11, iArr[] = new int[N], iArr2[] = new int[N];
        byte by1 = -97;
        for (i20 = 15; 253 > i20; ++i20) {
            iFld += i21;
            for (i22 = 1; 7 > i22; i22++) {
                iArr[i20 + 1] >>= i20;
            }
            fFld = i2;
            iArr2[i20] -= (int) 2.302F;
        }
    }

    public static void main(String[] strArr) {
        lMeth(0);
    }
}
