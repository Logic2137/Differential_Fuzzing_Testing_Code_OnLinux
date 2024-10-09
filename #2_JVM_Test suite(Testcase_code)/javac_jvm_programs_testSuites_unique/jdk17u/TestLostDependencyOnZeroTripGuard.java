public class TestLostDependencyOnZeroTripGuard {

    public static final int N = 400;

    public int[] iArrFld = new int[N];

    public void mainTest(String[] strArr1) {
        int i = 57657, i1 = 577, i2 = 6, i3 = 157, i4 = 12, i23 = 61271;
        boolean[] bArr = new boolean[N];
        for (i = 9; 379 > i; i++) {
            i2 = 1;
            do {
                i1 <<= i3;
            } while (++i2 < 68);
            for (i23 = 68; i23 > 3; i23--) {
                bArr[i23 + 1] = true;
                try {
                    i1 = (-42360 / i23);
                    iArrFld[i + 1] = (i4 % 15384);
                } catch (ArithmeticException a_e) {
                }
            }
        }
    }

    public static void main(String[] strArr) {
        TestLostDependencyOnZeroTripGuard _instance = new TestLostDependencyOnZeroTripGuard();
        for (int i = 0; i < 10; i++) {
            _instance.mainTest(strArr);
        }
    }
}
