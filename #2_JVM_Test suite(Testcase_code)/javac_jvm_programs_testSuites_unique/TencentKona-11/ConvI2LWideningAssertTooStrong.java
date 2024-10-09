



public class ConvI2LWideningAssertTooStrong {

    public static final int N = 400;

    public static long instanceCount=708L;
    public static volatile int iFld1=30517;
    public static int iArrFld[]=new int[N];

    public static void vMeth(short s) {
        int i9=29117, i11=-6;

        for (i9 = 11; i9 < 377; i9++) {
            switch ((i9 % 8) + 22) {
            case 24:
                instanceCount = i9;
                instanceCount += instanceCount;
                break;
            case 25:
                try {
                    i11 = (20705 % i11);
                    iArrFld[i9 - 1] = (55094 / iFld1);
                } catch (ArithmeticException a_e) {}
                break;
            default:
            }
        }
    }

    public static void main(String[] strArr) {
        ConvI2LWideningAssertTooStrong _instance = new ConvI2LWideningAssertTooStrong();
        for (int i = 0; i < 10 * 202 * 8; i++ ) {
            _instance.vMeth((short)20806);
        }
    }
}
