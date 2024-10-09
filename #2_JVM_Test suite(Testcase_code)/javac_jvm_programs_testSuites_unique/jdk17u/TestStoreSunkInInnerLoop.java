public class TestStoreSunkInInnerLoop {

    public static final int N = 400;

    public static int iFld = -10622;

    public static long[] lArrFld = new long[N];

    public float[][] fArrFld = new float[N][N];

    public void mainTest() {
        int i8 = -10584, i10 = 37284, i11 = 38, i13 = -238, i14 = -18473, i15 = -53564;
        boolean b1 = false;
        TestStoreSunkInInnerLoop.iFld -= TestStoreSunkInInnerLoop.iFld;
        for (i8 = 224; i8 > 7; i8 -= 2) {
            i10 = 1;
            while (++i10 < 232) {
                TestStoreSunkInInnerLoop.iFld += i8;
            }
            for (i11 = 8; i11 < 232; ++i11) {
                if (b1)
                    continue;
                TestStoreSunkInInnerLoop.lArrFld[i11] += i10;
            }
        }
        i13 = 1;
        do {
            switch((i13 % 2) + 126) {
                case 126:
                    for (i14 = 102; i14 > 2; i14 -= 3) {
                        fArrFld[i13][(-126 >>> 1) % N] -= i15;
                    }
                    break;
                case 127:
                    i15 = (i13 % i10);
                    break;
                default:
            }
        } while (++i13 < 247);
    }

    public static void main(String[] strArr) {
        TestStoreSunkInInnerLoop _instance = new TestStoreSunkInInnerLoop();
        for (int i = 0; i < 10; i++) {
            _instance.mainTest();
        }
    }
}
