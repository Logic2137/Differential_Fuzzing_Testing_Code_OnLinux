


package compiler.c2;

public class TestMatcherLargeOffset {
    public static final int N = 400;
    public static int iArrFld[] = new int[N];

    public static void m(int i4) {
        i4 |= -104;
        iArrFld[(i4 >>> 1) % N] >>= i4;
    }

    public static void test() {
        int i2 = 1, i24 = 65;
        for (int i1 = 7; i1 < 384; ++i1) {
            for (long l = 2; l < 67; l++) {
                m(i2);
                for (i24 = 1; 2 > i24; ++i24) {
                    iArrFld = iArrFld;
                }
            }
            i2 = (-229 / i24);
        }
    }
    public static void main(String[] strArr) {
        for (int i = 0; i < 5; i++ ) {
            test();
        }
    }
}
