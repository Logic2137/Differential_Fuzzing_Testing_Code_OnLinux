



public class TestPeelingNeverEnteredLoop {

    public static final int N = 400;

    public static byte byFld=83;

    public static void lMeth() {

        int iArr1[][]=new int[N][N];
        byte byArr[][]=new byte[N][N];

        int i10 = 1;
        do {
            int i11 = 1;
            do {
                iArr1[i10 - 1][i11] = TestPeelingNeverEnteredLoop.byFld;
                byArr[i10][i11] -= (byte)-20046;
                for (int i12 = 1; 1 > i12; ++i12) {
                }
            } while (++i11 < 8);
        } while (++i10 < 212);
    }

    public static void main(String[] strArr) {
        TestPeelingNeverEnteredLoop _instance = new TestPeelingNeverEnteredLoop();
        for (int i = 0; i < 1500; i++ ) {
            _instance.lMeth();
        }
    }
}
