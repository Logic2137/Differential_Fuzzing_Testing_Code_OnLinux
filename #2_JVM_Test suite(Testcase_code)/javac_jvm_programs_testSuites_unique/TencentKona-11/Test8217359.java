



package compiler.c2;

public class Test8217359 {

    public static int ival = 0;
    public static long lsum = 0;
    public static long lval = 0;

    public static void test() {
        short s = -25632;
        float f = 0.512F, f1 = 2.556F;
        int i6 = 32547, i7 = 9, i8 = -9, i9 = 36, i10 = -223;

        for (i6 = 4; i6 < 182; i6++) {
            i8 = 1;
            while (++i8 < 17) {
                f1 = 1;
                do {
                    i7 += (182 + (f1 * f1));
                } while (++f1 < 1);

                Test8217359.ival += (i8 | Test8217359.ival);
            }
        }

        for (i9 = 9; i9 < 100; ++i9) {
            i10 -= i6;
            i10 >>= s;
            i7 += (((i9 * i10) + i6) - Test8217359.lval);
        }

        lsum += i6 + i7 + i8;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 16000; i++) {
            test();
        }
    }

}
