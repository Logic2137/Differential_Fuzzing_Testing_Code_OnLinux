



package compiler.loopopts;

public class TestDivZeroDominatedBy {

    public static int iFld = 1;
    public static int iFld1 = 2;
    public static int iFld2 = 3;
    public static int iArrFld[] = new int[10];
    public static double dFld = 1.0;

    public static void test() {
        int x = 1;
        int y = 2;
        int z = 3;

        iFld = y;
        iArrFld[5] += iFld1;
        int i = 1;
        do {
            for (int j = 0; j < 10; j++) {
                iFld2 += iFld2;
                iFld1 = iFld2;
                int k = 1;
                do {
                    iArrFld[k] = y;
                    z = iFld2;
                    dFld = x;
                    try {
                        y = iArrFld[k];
                        iArrFld[8] = 5;
                        iFld = (100 / z);
                    } catch (ArithmeticException a_e) {}
                } while (++k < 2);
            }
        } while (++i < 10);
    }

    public static void main(String[] strArr) {
        test();
    }
}

