public class Test7196199 {

    private static final int ARRLEN = 97;

    private static final int ITERS = 5000;

    private static final int INI_ITERS = 1000;

    private static final int SFP_ITERS = 10000;

    private static final float SFP_ITERS_F = 10000.f;

    private static final float VALUE = 15.f;

    public static void main(String[] args) {
        int errn = test();
        if (errn > 0) {
            System.err.println("FAILED: " + errn + " errors");
            System.exit(97);
        }
        System.out.println("PASSED");
    }

    static int test() {
        float[] a0 = new float[ARRLEN];
        float[] a1 = new float[ARRLEN];
        for (int i = 0; i < ARRLEN; i++) {
            a0[i] = 0.f;
            a1[i] = (float) i;
        }
        System.out.println("Warmup");
        for (int i = 0; i < INI_ITERS; i++) {
            test_incrc(a0);
            test_incrv(a0, VALUE);
            test_addc(a0, a1);
            test_addv(a0, a1, VALUE);
        }
        System.out.println("Verification");
        int errn = 0;
        for (int i = 0; i < ARRLEN; i++) a0[i] = 0.f;
        System.out.println("  test_incrc");
        for (int j = 0; j < ITERS; j++) {
            test_incrc(a0);
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_incrc: ", i, a0[i], VALUE * SFP_ITERS_F);
                a0[i] = 0.f;
            }
        }
        System.out.println("  test_incrv");
        for (int j = 0; j < ITERS; j++) {
            test_incrv(a0, VALUE);
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_incrv: ", i, a0[i], VALUE * SFP_ITERS_F);
                a0[i] = 0.f;
            }
        }
        System.out.println("  test_addc");
        for (int j = 0; j < ITERS; j++) {
            test_addc(a0, a1);
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_addc: ", i, a0[i], ((float) i + VALUE) * SFP_ITERS_F);
                a0[i] = 0.f;
            }
        }
        System.out.println("  test_addv");
        for (int j = 0; j < ITERS; j++) {
            test_addv(a0, a1, VALUE);
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_addv: ", i, a0[i], ((float) i + VALUE) * SFP_ITERS_F);
                a0[i] = 0.f;
            }
        }
        if (errn > 0)
            return errn;
        System.out.println("Time");
        long start, end;
        start = System.currentTimeMillis();
        for (int i = 0; i < INI_ITERS; i++) {
            test_incrc(a0);
        }
        end = System.currentTimeMillis();
        System.out.println("test_incrc: " + (end - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < INI_ITERS; i++) {
            test_incrv(a0, VALUE);
        }
        end = System.currentTimeMillis();
        System.out.println("test_incrv: " + (end - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < INI_ITERS; i++) {
            test_addc(a0, a1);
        }
        end = System.currentTimeMillis();
        System.out.println("test_addc: " + (end - start));
        start = System.currentTimeMillis();
        for (int i = 0; i < INI_ITERS; i++) {
            test_addv(a0, a1, VALUE);
        }
        end = System.currentTimeMillis();
        System.out.println("test_addv: " + (end - start));
        return errn;
    }

    static void test_incrc(float[] a0) {
        for (long l = 0; l < SFP_ITERS; l++) {
            for (int i = 0; i < a0.length; i += 1) {
                a0[i] += VALUE;
            }
        }
    }

    static void test_incrv(float[] a0, float b) {
        for (long l = 0; l < SFP_ITERS; l++) {
            for (int i = 0; i < a0.length; i += 1) {
                a0[i] += b;
            }
        }
    }

    static void test_addc(float[] a0, float[] a1) {
        for (long l = 0; l < SFP_ITERS; l++) {
            for (int i = 0; i < a0.length; i += 1) {
                a0[i] += a1[i] + VALUE;
            }
        }
    }

    static void test_addv(float[] a0, float[] a1, float b) {
        for (long l = 0; l < SFP_ITERS; l++) {
            for (int i = 0; i < a0.length; i += 1) {
                a0[i] += a1[i] + b;
            }
        }
    }

    static int verify(String text, int i, float elem, float val) {
        if (elem != val) {
            System.err.println(text + "[" + i + "] = " + elem + " != " + val);
            return 1;
        }
        return 0;
    }
}
