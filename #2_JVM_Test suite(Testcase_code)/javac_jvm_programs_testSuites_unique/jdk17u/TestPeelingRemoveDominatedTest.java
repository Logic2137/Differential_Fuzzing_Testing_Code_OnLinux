
package compiler.loopopts;

public class TestPeelingRemoveDominatedTest {

    public static int N = 400;

    static boolean bFld = true;

    static int[] iArrFld = new int[N];

    public static void main(String[] strArr) {
        TestPeelingRemoveDominatedTest _instance = new TestPeelingRemoveDominatedTest();
        for (int i = 0; i < 10; i++) {
            _instance.mainTest();
        }
    }

    public void mainTest() {
        vMeth();
    }

    static void vMeth() {
        iArrFld[1] = 2;
        int i6 = 2;
        while (--i6 > 0) {
            try {
                int i3 = (iArrFld[i6 - 1] / 56);
                iArrFld[1] = (-139 % i3);
            } catch (ArithmeticException a_e) {
            }
            if (bFld) {
            }
        }
    }
}
