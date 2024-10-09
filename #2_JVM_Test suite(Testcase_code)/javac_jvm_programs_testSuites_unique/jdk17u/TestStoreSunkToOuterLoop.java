public class TestStoreSunkToOuterLoop {

    public static final int N = 400;

    public static long instanceCount = -62761L;

    public static boolean bFld = false;

    public static int[] iArrFld = new int[N];

    public void mainTest() {
        int i15 = 226, i16 = 54621, i19 = 780;
        float f3 = 0.671F, f4 = -101.846F;
        i15 = 1;
        do {
            if (bFld)
                continue;
            for (i16 = 1; i16 < 101; ++i16) {
                iArrFld[i16 - 1] = i15;
                instanceCount = i16;
            }
        } while (++i15 < 248);
        f3 += -2061721519L;
        for (f4 = 324; f4 > 3; f4--) {
            for (i19 = 4; i19 < 78; ++i19) {
                f3 -= -11;
            }
        }
        System.out.println(instanceCount);
    }

    public static void main(String[] strArr) {
        TestStoreSunkToOuterLoop _instance = new TestStoreSunkToOuterLoop();
        for (int i = 0; i < 10; i++) {
            _instance.mainTest();
        }
    }
}
