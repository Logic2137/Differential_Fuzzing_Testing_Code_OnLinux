
package compiler.codegen;

public class TestMultiMemInstructionMatching {

    static volatile int iFldV = 42;

    static volatile long lFldV = 42;

    static int iFld = 42;

    static long lFld = 42;

    static int test_blsiI_rReg_mem_1() {
        return (0 - iFldV) & iFldV;
    }

    static int test_blsiI_rReg_mem_2() {
        int sub = (0 - iFld);
        iFldV++;
        return sub & iFld;
    }

    static int test_blsrI_rReg_mem_1() {
        return (iFldV - 1) & iFldV;
    }

    static int test_blsrI_rReg_mem_2() {
        int sub = (iFld - 1);
        iFldV++;
        return sub & iFld;
    }

    static int test_blsmskI_rReg_mem_1() {
        return (iFldV - 1) ^ iFldV;
    }

    static int test_blsmskI_rReg_mem_2() {
        int sub = (iFld - 1);
        iFldV++;
        return sub ^ iFld;
    }

    static long test_blsiL_rReg_mem_1() {
        return (0 - lFldV) & lFldV;
    }

    static long test_blsiL_rReg_mem_2() {
        long sub = (0 - lFld);
        lFldV++;
        return sub & lFld;
    }

    static long test_blsrL_rReg_mem_1() {
        return (lFldV - 1) & lFldV;
    }

    static long test_blsrL_rReg_mem_2() {
        long sub = (lFld - 1);
        lFldV++;
        return sub & lFld;
    }

    static long test_blsmskL_rReg_mem_1() {
        return (lFldV - 1) ^ lFldV;
    }

    static long test_blsmskL_rReg_mem_2() {
        long sub = (lFld - 1);
        lFldV++;
        return sub ^ lFld;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100_000; ++i) {
            test_blsiI_rReg_mem_1();
            test_blsiI_rReg_mem_2();
            test_blsrI_rReg_mem_1();
            test_blsrI_rReg_mem_2();
            test_blsmskI_rReg_mem_1();
            test_blsmskI_rReg_mem_2();
            test_blsiL_rReg_mem_1();
            test_blsiL_rReg_mem_2();
            test_blsrL_rReg_mem_1();
            test_blsrL_rReg_mem_2();
            test_blsmskL_rReg_mem_1();
            test_blsmskL_rReg_mem_2();
        }
    }
}
