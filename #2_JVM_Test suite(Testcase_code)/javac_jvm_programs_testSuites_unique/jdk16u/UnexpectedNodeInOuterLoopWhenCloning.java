



public class UnexpectedNodeInOuterLoopWhenCloning {

    public static final int N = 400;

    public static double dFld=0.37026;
    public static int iArrFld[]=new int[N];

    public static void vMeth() {

        int i5=6, i6=-42538, i7=-209, i8=163, i10=-4, i11=191;
        boolean b=true;
        double dArr[]=new double[N];

        for (i5 = 3; i5 < 245; i5++) {
            i7 = 7;
            while (--i7 > 0) {
                iArrFld[i7] = -11995;
                if (b) continue;
            }
            for (i8 = 1; i8 < 7; ++i8) {
                for (i10 = 1; i10 < 2; i10++) {
                    dFld -= i6;
                    i11 += i7;
                }
            }
        }
    }

    public static void main(String[] strArr) {
        UnexpectedNodeInOuterLoopWhenCloning _instance = new UnexpectedNodeInOuterLoopWhenCloning();
        _instance.vMeth();
    }
}
