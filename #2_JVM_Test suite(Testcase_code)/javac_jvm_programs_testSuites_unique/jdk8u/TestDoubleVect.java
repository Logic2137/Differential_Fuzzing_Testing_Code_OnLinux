public class TestDoubleVect {

    private static final int ARRLEN = 997;

    private static final int ITERS = 11000;

    private static final int OFFSET = 3;

    private static final int SCALE = 2;

    private static final int ALIGN_OFF = 8;

    private static final int UNALIGN_OFF = 5;

    public static void main(String[] args) {
        System.out.println("Testing Double vectors");
        int errn = test();
        if (errn > 0) {
            System.err.println("FAILED: " + errn + " errors");
            System.exit(97);
        }
        System.out.println("PASSED");
    }

    static int test() {
        double[] a1 = new double[ARRLEN];
        double[] a2 = new double[ARRLEN];
        System.out.println("Warmup");
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_ci(a1);
            test_vi(a2, 123.);
            test_cp(a1, a2);
            test_2ci(a1, a2);
            test_2vi(a1, a2, 123., 103.);
            test_ci_neg(a1);
            test_vi_neg(a2, 123.);
            test_cp_neg(a1, a2);
            test_2ci_neg(a1, a2);
            test_2vi_neg(a1, a2, 123., 103.);
            test_ci_oppos(a1);
            test_vi_oppos(a2, 123.);
            test_cp_oppos(a1, a2);
            test_2ci_oppos(a1, a2);
            test_2vi_oppos(a1, a2, 123., 103.);
            test_ci_off(a1);
            test_vi_off(a2, 123.);
            test_cp_off(a1, a2);
            test_2ci_off(a1, a2);
            test_2vi_off(a1, a2, 123., 103.);
            test_ci_inv(a1, OFFSET);
            test_vi_inv(a2, 123., OFFSET);
            test_cp_inv(a1, a2, OFFSET);
            test_2ci_inv(a1, a2, OFFSET);
            test_2vi_inv(a1, a2, 123., 103., OFFSET);
            test_ci_scl(a1);
            test_vi_scl(a2, 123.);
            test_cp_scl(a1, a2);
            test_2ci_scl(a1, a2);
            test_2vi_scl(a1, a2, 123., 103.);
            test_cp_alndst(a1, a2);
            test_cp_alnsrc(a1, a2);
            test_2ci_aln(a1, a2);
            test_2vi_aln(a1, a2, 123., 103.);
            test_cp_unalndst(a1, a2);
            test_cp_unalnsrc(a1, a2);
            test_2ci_unaln(a1, a2);
            test_2vi_unaln(a1, a2, 123., 103.);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ARRLEN; i++) {
            a1[i] = -1;
            a2[i] = -1;
        }
        System.out.println("Verification");
        int errn = 0;
        {
            test_ci(a1);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_ci: a1", i, a1[i], -123.);
            }
            test_vi(a2, 123.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_vi: a2", i, a2[i], 123.);
            }
            test_cp(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_cp: a1", i, a1[i], 123.);
            }
            test_2ci(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_2ci: a1", i, a1[i], -123.);
                errn += verify("test_2ci: a2", i, a2[i], -103.);
            }
            test_2vi(a1, a2, 123., 103.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_2vi: a1", i, a1[i], 123.);
                errn += verify("test_2vi: a2", i, a2[i], 103.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
                a2[i] = -1;
            }
            test_ci_neg(a1);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_ci_neg: a1", i, a1[i], -123.);
            }
            test_vi_neg(a2, 123.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_vi_neg: a2", i, a2[i], 123.);
            }
            test_cp_neg(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_cp_neg: a1", i, a1[i], 123.);
            }
            test_2ci_neg(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_2ci_neg: a1", i, a1[i], -123.);
                errn += verify("test_2ci_neg: a2", i, a2[i], -103.);
            }
            test_2vi_neg(a1, a2, 123., 103.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_2vi_neg: a1", i, a1[i], 123.);
                errn += verify("test_2vi_neg: a2", i, a2[i], 103.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
                a2[i] = -1;
            }
            test_ci_oppos(a1);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_ci_oppos: a1", i, a1[i], -123.);
            }
            test_vi_oppos(a2, 123.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_vi_oppos: a2", i, a2[i], 123.);
            }
            test_cp_oppos(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_cp_oppos: a1", i, a1[i], 123.);
            }
            test_2ci_oppos(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_2ci_oppos: a1", i, a1[i], -123.);
                errn += verify("test_2ci_oppos: a2", i, a2[i], -103.);
            }
            test_2vi_oppos(a1, a2, 123., 103.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                errn += verify("test_2vi_oppos: a1", i, a1[i], 123.);
                errn += verify("test_2vi_oppos: a2", i, a2[i], 103.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
                a2[i] = -1;
            }
            test_ci_off(a1);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = OFFSET; i < ARRLEN; i++) {
                errn += verify("test_ci_off: a1", i, a1[i], -123.);
            }
            test_vi_off(a2, 123.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = OFFSET; i < ARRLEN; i++) {
                errn += verify("test_vi_off: a2", i, a2[i], 123.);
            }
            test_cp_off(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = OFFSET; i < ARRLEN; i++) {
                errn += verify("test_cp_off: a1", i, a1[i], 123.);
            }
            test_2ci_off(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = OFFSET; i < ARRLEN; i++) {
                errn += verify("test_2ci_off: a1", i, a1[i], -123.);
                errn += verify("test_2ci_off: a2", i, a2[i], -103.);
            }
            test_2vi_off(a1, a2, 123., 103.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = OFFSET; i < ARRLEN; i++) {
                errn += verify("test_2vi_off: a1", i, a1[i], 123.);
                errn += verify("test_2vi_off: a2", i, a2[i], 103.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < OFFSET; i++) {
                errn += verify("test_2vi_off: a1", i, a1[i], -1.);
                errn += verify("test_2vi_off: a2", i, a2[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
                a2[i] = -1;
            }
            test_ci_inv(a1, OFFSET);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = OFFSET; i < ARRLEN; i++) {
                errn += verify("test_ci_inv: a1", i, a1[i], -123.);
            }
            test_vi_inv(a2, 123., OFFSET);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = OFFSET; i < ARRLEN; i++) {
                errn += verify("test_vi_inv: a2", i, a2[i], 123.);
            }
            test_cp_inv(a1, a2, OFFSET);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = OFFSET; i < ARRLEN; i++) {
                errn += verify("test_cp_inv: a1", i, a1[i], 123.);
            }
            test_2ci_inv(a1, a2, OFFSET);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = OFFSET; i < ARRLEN; i++) {
                errn += verify("test_2ci_inv: a1", i, a1[i], -123.);
                errn += verify("test_2ci_inv: a2", i, a2[i], -103.);
            }
            test_2vi_inv(a1, a2, 123., 103., OFFSET);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = OFFSET; i < ARRLEN; i++) {
                errn += verify("test_2vi_inv: a1", i, a1[i], 123.);
                errn += verify("test_2vi_inv: a2", i, a2[i], 103.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < OFFSET; i++) {
                errn += verify("test_2vi_inv: a1", i, a1[i], -1.);
                errn += verify("test_2vi_inv: a2", i, a2[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
                a2[i] = -1;
            }
            test_ci_scl(a1);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                int val = (i % SCALE != 0) ? -1 : -123;
                errn += verify("test_ci_scl: a1", i, a1[i], (double) val);
            }
            test_vi_scl(a2, 123.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                int val = (i % SCALE != 0) ? -1 : 123;
                errn += verify("test_vi_scl: a2", i, a2[i], (double) val);
            }
            test_cp_scl(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                int val = (i % SCALE != 0) ? -1 : 123;
                errn += verify("test_cp_scl: a1", i, a1[i], (double) val);
            }
            test_2ci_scl(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                if (i % SCALE != 0) {
                    errn += verify("test_2ci_scl: a1", i, a1[i], -1.);
                } else if (i * SCALE < ARRLEN) {
                    errn += verify("test_2ci_scl: a1", i * SCALE, a1[i * SCALE], -123.);
                }
                if (i % SCALE != 0) {
                    errn += verify("test_2ci_scl: a2", i, a2[i], -1.);
                } else if (i * SCALE < ARRLEN) {
                    errn += verify("test_2ci_scl: a2", i * SCALE, a2[i * SCALE], -103.);
                }
            }
            test_2vi_scl(a1, a2, 123., 103.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                if (i % SCALE != 0) {
                    errn += verify("test_2vi_scl: a1", i, a1[i], -1.);
                } else if (i * SCALE < ARRLEN) {
                    errn += verify("test_2vi_scl: a1", i * SCALE, a1[i * SCALE], 123.);
                }
                if (i % SCALE != 0) {
                    errn += verify("test_2vi_scl: a2", i, a2[i], -1.);
                } else if (i * SCALE < ARRLEN) {
                    errn += verify("test_2vi_scl: a2", i * SCALE, a2[i * SCALE], 103.);
                }
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
                a2[i] = -1;
            }
            test_vi(a2, 123.);
            test_cp_alndst(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ALIGN_OFF; i++) {
                errn += verify("test_cp_alndst: a1", i, a1[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_cp_alndst: a1", i, a1[i], 123.);
            }
            test_vi(a2, -123.);
            test_cp_alnsrc(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN - ALIGN_OFF; i++) {
                errn += verify("test_cp_alnsrc: a1", i, a1[i], -123.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ARRLEN - ALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_cp_alnsrc: a1", i, a1[i], 123.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
                a2[i] = -1;
            }
            test_2ci_aln(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ALIGN_OFF; i++) {
                errn += verify("test_2ci_aln: a1", i, a1[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_2ci_aln: a1", i, a1[i], -123.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN - ALIGN_OFF; i++) {
                errn += verify("test_2ci_aln: a2", i, a2[i], -103.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ARRLEN - ALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_2ci_aln: a2", i, a2[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
                a2[i] = -1;
            }
            test_2vi_aln(a1, a2, 123., 103.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN - ALIGN_OFF; i++) {
                errn += verify("test_2vi_aln: a1", i, a1[i], 123.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ARRLEN - ALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_2vi_aln: a1", i, a1[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ALIGN_OFF; i++) {
                errn += verify("test_2vi_aln: a2", i, a2[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_2vi_aln: a2", i, a2[i], 103.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
                a2[i] = -1;
            }
            test_vi(a2, 123.);
            test_cp_unalndst(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < UNALIGN_OFF; i++) {
                errn += verify("test_cp_unalndst: a1", i, a1[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = UNALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_cp_unalndst: a1", i, a1[i], 123.);
            }
            test_vi(a2, -123.);
            test_cp_unalnsrc(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN - UNALIGN_OFF; i++) {
                errn += verify("test_cp_unalnsrc: a1", i, a1[i], -123.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ARRLEN - UNALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_cp_unalnsrc: a1", i, a1[i], 123.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
                a2[i] = -1;
            }
            test_2ci_unaln(a1, a2);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < UNALIGN_OFF; i++) {
                errn += verify("test_2ci_unaln: a1", i, a1[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = UNALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_2ci_unaln: a1", i, a1[i], -123.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN - UNALIGN_OFF; i++) {
                errn += verify("test_2ci_unaln: a2", i, a2[i], -103.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ARRLEN - UNALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_2ci_unaln: a2", i, a2[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
                a2[i] = -1;
            }
            test_2vi_unaln(a1, a2, 123., 103.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN - UNALIGN_OFF; i++) {
                errn += verify("test_2vi_unaln: a1", i, a1[i], 123.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ARRLEN - UNALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_2vi_unaln: a1", i, a1[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < UNALIGN_OFF; i++) {
                errn += verify("test_2vi_unaln: a2", i, a2[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = UNALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_2vi_unaln: a2", i, a2[i], 103.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ALIGN_OFF; i++) {
                a1[i] = (double) i;
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ALIGN_OFF; i < ARRLEN; i++) {
                a1[i] = -1;
            }
            test_cp_alndst(a1, a1);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                int v = i % ALIGN_OFF;
                errn += verify("test_cp_alndst_overlap: a1", i, a1[i], (double) v);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ALIGN_OFF; i++) {
                a1[i + ALIGN_OFF] = -1;
            }
            test_cp_alnsrc(a1, a1);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ALIGN_OFF; i++) {
                errn += verify("test_cp_alnsrc_overlap: a1", i, a1[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ALIGN_OFF; i < ARRLEN; i++) {
                int v = i % ALIGN_OFF;
                errn += verify("test_cp_alnsrc_overlap: a1", i, a1[i], (double) v);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
            }
            test_2ci_aln(a1, a1);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN - ALIGN_OFF; i++) {
                errn += verify("test_2ci_aln_overlap: a1", i, a1[i], -103.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ARRLEN - ALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_2ci_aln_overlap: a1", i, a1[i], -123.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
            }
            test_2vi_aln(a1, a1, 123., 103.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN - ALIGN_OFF; i++) {
                errn += verify("test_2vi_aln_overlap: a1", i, a1[i], 123.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ARRLEN - ALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_2vi_aln_overlap: a1", i, a1[i], 103.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < UNALIGN_OFF; i++) {
                a1[i] = (double) i;
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = UNALIGN_OFF; i < ARRLEN; i++) {
                a1[i] = -1;
            }
            test_cp_unalndst(a1, a1);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                int v = i % UNALIGN_OFF;
                errn += verify("test_cp_unalndst_overlap: a1", i, a1[i], (double) v);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < UNALIGN_OFF; i++) {
                a1[i + UNALIGN_OFF] = -1;
            }
            test_cp_unalnsrc(a1, a1);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < UNALIGN_OFF; i++) {
                errn += verify("test_cp_unalnsrc_overlap: a1", i, a1[i], -1.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = UNALIGN_OFF; i < ARRLEN; i++) {
                int v = i % UNALIGN_OFF;
                errn += verify("test_cp_unalnsrc_overlap: a1", i, a1[i], (double) v);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
            }
            test_2ci_unaln(a1, a1);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN - UNALIGN_OFF; i++) {
                errn += verify("test_2ci_unaln_overlap: a1", i, a1[i], -103.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ARRLEN - UNALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_2ci_unaln_overlap: a1", i, a1[i], -123.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN; i++) {
                a1[i] = -1;
            }
            test_2vi_unaln(a1, a1, 123., 103.);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < ARRLEN - UNALIGN_OFF; i++) {
                errn += verify("test_2vi_unaln_overlap: a1", i, a1[i], 123.);
            }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = ARRLEN - UNALIGN_OFF; i < ARRLEN; i++) {
                errn += verify("test_2vi_unaln_overlap: a1", i, a1[i], 103.);
            }
        }
        if (errn > 0)
            return errn;
        System.out.println("Time");
        long start, end;
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_ci(a1);
        }
        end = System.currentTimeMillis();
        System.out.println("test_ci: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_vi(a2, 123.);
        }
        end = System.currentTimeMillis();
        System.out.println("test_vi: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_cp(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_cp: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2ci(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2ci: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2vi(a1, a2, 123., 103.);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2vi: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_ci_neg(a1);
        }
        end = System.currentTimeMillis();
        System.out.println("test_ci_neg: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_vi_neg(a2, 123.);
        }
        end = System.currentTimeMillis();
        System.out.println("test_vi_neg: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_cp_neg(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_cp_neg: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2ci_neg(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2ci_neg: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2vi_neg(a1, a2, 123., 103.);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2vi_neg: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_ci_oppos(a1);
        }
        end = System.currentTimeMillis();
        System.out.println("test_ci_oppos: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_vi_oppos(a2, 123.);
        }
        end = System.currentTimeMillis();
        System.out.println("test_vi_oppos: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_cp_oppos(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_cp_oppos: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2ci_oppos(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2ci_oppos: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2vi_oppos(a1, a2, 123., 103.);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2vi_oppos: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_ci_off(a1);
        }
        end = System.currentTimeMillis();
        System.out.println("test_ci_off: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_vi_off(a2, 123.);
        }
        end = System.currentTimeMillis();
        System.out.println("test_vi_off: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_cp_off(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_cp_off: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2ci_off(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2ci_off: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2vi_off(a1, a2, 123., 103.);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2vi_off: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_ci_inv(a1, OFFSET);
        }
        end = System.currentTimeMillis();
        System.out.println("test_ci_inv: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_vi_inv(a2, 123., OFFSET);
        }
        end = System.currentTimeMillis();
        System.out.println("test_vi_inv: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_cp_inv(a1, a2, OFFSET);
        }
        end = System.currentTimeMillis();
        System.out.println("test_cp_inv: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2ci_inv(a1, a2, OFFSET);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2ci_inv: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2vi_inv(a1, a2, 123., 103., OFFSET);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2vi_inv: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_ci_scl(a1);
        }
        end = System.currentTimeMillis();
        System.out.println("test_ci_scl: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_vi_scl(a2, 123.);
        }
        end = System.currentTimeMillis();
        System.out.println("test_vi_scl: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_cp_scl(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_cp_scl: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2ci_scl(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2ci_scl: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2vi_scl(a1, a2, 123., 103.);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2vi_scl: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_cp_alndst(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_cp_alndst: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_cp_alnsrc(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_cp_alnsrc: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2ci_aln(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2ci_aln: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2vi_aln(a1, a2, 123., 103.);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2vi_aln: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_cp_unalndst(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_cp_unalndst: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_cp_unalnsrc(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_cp_unalnsrc: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2ci_unaln(a1, a2);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2ci_unaln: " + (end - start));
        start = System.currentTimeMillis();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < ITERS; i++) {
            test_2vi_unaln(a1, a2, 123., 103.);
        }
        end = System.currentTimeMillis();
        System.out.println("test_2vi_unaln: " + (end - start));
        return errn;
    }

    static void test_ci(double[] a) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length; i += 1) {
            a[i] = -123.;
        }
    }

    static void test_vi(double[] a, double b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length; i += 1) {
            a[i] = b;
        }
    }

    static void test_cp(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length; i += 1) {
            a[i] = b[i];
        }
    }

    static void test_2ci(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length; i += 1) {
            a[i] = -123.;
            b[i] = -103.;
        }
    }

    static void test_2vi(double[] a, double[] b, double c, double d) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length; i += 1) {
            a[i] = c;
            b[i] = d;
        }
    }

    static void test_ci_neg(double[] a) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = a.length - 1; i >= 0; i -= 1) {
            a[i] = -123.;
        }
    }

    static void test_vi_neg(double[] a, double b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = a.length - 1; i >= 0; i -= 1) {
            a[i] = b;
        }
    }

    static void test_cp_neg(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = a.length - 1; i >= 0; i -= 1) {
            a[i] = b[i];
        }
    }

    static void test_2ci_neg(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = a.length - 1; i >= 0; i -= 1) {
            a[i] = -123.;
            b[i] = -103.;
        }
    }

    static void test_2vi_neg(double[] a, double[] b, double c, double d) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = a.length - 1; i >= 0; i -= 1) {
            a[i] = c;
            b[i] = d;
        }
    }

    static void test_ci_oppos(double[] a) {
        int limit = a.length - 1;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length; i += 1) {
            a[limit - i] = -123.;
        }
    }

    static void test_vi_oppos(double[] a, double b) {
        int limit = a.length - 1;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = limit; i >= 0; i -= 1) {
            a[limit - i] = b;
        }
    }

    static void test_cp_oppos(double[] a, double[] b) {
        int limit = a.length - 1;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length; i += 1) {
            a[i] = b[limit - i];
        }
    }

    static void test_2ci_oppos(double[] a, double[] b) {
        int limit = a.length - 1;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length; i += 1) {
            a[limit - i] = -123.;
            b[i] = -103.;
        }
    }

    static void test_2vi_oppos(double[] a, double[] b, double c, double d) {
        int limit = a.length - 1;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = limit; i >= 0; i -= 1) {
            a[i] = c;
            b[limit - i] = d;
        }
    }

    static void test_ci_off(double[] a) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - OFFSET; i += 1) {
            a[i + OFFSET] = -123.;
        }
    }

    static void test_vi_off(double[] a, double b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - OFFSET; i += 1) {
            a[i + OFFSET] = b;
        }
    }

    static void test_cp_off(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - OFFSET; i += 1) {
            a[i + OFFSET] = b[i + OFFSET];
        }
    }

    static void test_2ci_off(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - OFFSET; i += 1) {
            a[i + OFFSET] = -123.;
            b[i + OFFSET] = -103.;
        }
    }

    static void test_2vi_off(double[] a, double[] b, double c, double d) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - OFFSET; i += 1) {
            a[i + OFFSET] = c;
            b[i + OFFSET] = d;
        }
    }

    static void test_ci_inv(double[] a, int k) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - k; i += 1) {
            a[i + k] = -123.;
        }
    }

    static void test_vi_inv(double[] a, double b, int k) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - k; i += 1) {
            a[i + k] = b;
        }
    }

    static void test_cp_inv(double[] a, double[] b, int k) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - k; i += 1) {
            a[i + k] = b[i + k];
        }
    }

    static void test_2ci_inv(double[] a, double[] b, int k) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - k; i += 1) {
            a[i + k] = -123.;
            b[i + k] = -103.;
        }
    }

    static void test_2vi_inv(double[] a, double[] b, double c, double d, int k) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - k; i += 1) {
            a[i + k] = c;
            b[i + k] = d;
        }
    }

    static void test_ci_scl(double[] a) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i * SCALE < a.length; i += 1) {
            a[i * SCALE] = -123.;
        }
    }

    static void test_vi_scl(double[] a, double b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i * SCALE < a.length; i += 1) {
            a[i * SCALE] = b;
        }
    }

    static void test_cp_scl(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i * SCALE < a.length; i += 1) {
            a[i * SCALE] = b[i * SCALE];
        }
    }

    static void test_2ci_scl(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i * SCALE < a.length; i += 1) {
            a[i * SCALE] = -123.;
            b[i * SCALE] = -103.;
        }
    }

    static void test_2vi_scl(double[] a, double[] b, double c, double d) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i * SCALE < a.length; i += 1) {
            a[i * SCALE] = c;
            b[i * SCALE] = d;
        }
    }

    static void test_cp_alndst(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - ALIGN_OFF; i += 1) {
            a[i + ALIGN_OFF] = b[i];
        }
    }

    static void test_cp_alnsrc(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - ALIGN_OFF; i += 1) {
            a[i] = b[i + ALIGN_OFF];
        }
    }

    static void test_2ci_aln(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - ALIGN_OFF; i += 1) {
            a[i + ALIGN_OFF] = -123.;
            b[i] = -103.;
        }
    }

    static void test_2vi_aln(double[] a, double[] b, double c, double d) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - ALIGN_OFF; i += 1) {
            a[i] = c;
            b[i + ALIGN_OFF] = d;
        }
    }

    static void test_cp_unalndst(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - UNALIGN_OFF; i += 1) {
            a[i + UNALIGN_OFF] = b[i];
        }
    }

    static void test_cp_unalnsrc(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - UNALIGN_OFF; i += 1) {
            a[i] = b[i + UNALIGN_OFF];
        }
    }

    static void test_2ci_unaln(double[] a, double[] b) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - UNALIGN_OFF; i += 1) {
            a[i + UNALIGN_OFF] = -123.;
            b[i] = -103.;
        }
    }

    static void test_2vi_unaln(double[] a, double[] b, double c, double d) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < a.length - UNALIGN_OFF; i += 1) {
            a[i] = c;
            b[i + UNALIGN_OFF] = d;
        }
    }

    static int verify(String text, int i, double elem, double val) {
        if (elem != val) {
            System.err.println(text + "[" + i + "] = " + elem + " != " + val);
            return 1;
        }
        return 0;
    }
}
