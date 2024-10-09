


package compiler.loopopts;

public class TestSplitIfPinnedLoadInStripMinedLoop {

    public boolean bFld = false;
    public short sFld = 4;
    public static int iFld = 5;
    public static float fFld= 6.0f;
    public static int iArrFld[] = new int[400];

    public void test() {
        int x = 7;
        int y = 8;
        int a = 9;
        float f = 10.0f;
        double d = 11.0f;
        double dArr[] = new double[400];

        for (int i = 16; i < 350; i++) {
            for (int j = 1; j < 75; j++) {
                for (int k = 1; k < 3; k++) {
                }
                f = j * 6;
                y = j;
                try {
                    x = (y / 148);
                } catch (ArithmeticException a_e) {}
                if (bFld) {
                    break;
                }
                dArr[1] = 4;
            }
            for (int k = 75; k > i; k--) {
                iArrFld[k] = 5;
            }
            for (int k = 4; k < 75; k++) {
                f -= fFld;
                
                
                
                
                
                
                
                
                
                iFld = sFld;
            }
            switch ((i % 8) + 27) {
            case 27:
                if (bFld) {
                    for (a = 1; a < 75; a++) {
                        iFld += 6; 
                    }
                } else {
                    d -= x;
                }
                break;
            case 28:
                iFld = y;
                
            case 33:
            case 34:
                iFld -= (int)d; 
                break;
            }
        }
    }
    public static void main(String[] strArr) {
        TestSplitIfPinnedLoadInStripMinedLoop t = new TestSplitIfPinnedLoadInStripMinedLoop();
        for (int i = 0; i < 10; i++) {
            t.test();
        }
    }
}
