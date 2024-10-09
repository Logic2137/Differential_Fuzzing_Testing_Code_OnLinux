
package compiler.c1;

public class TestLinearScanHasFPURegisters {

    void test(String[] args) {
        String[] arr = new String[4];
        float f = -1;
        try {
            arr[0] = "-1";
            if (args.length > 1) {
                f = 42;
                arr[1] = "42";
            }
        } catch (Exception e) {
            for (int i = 0; i < 1; ++i) {
                f = f;
            }
        }
    }

    public static void main(String[] args) {
        TestLinearScanHasFPURegisters t = new TestLinearScanHasFPURegisters();
        for (int i = 0; i < 1000; ++i) {
            t.test(args);
        }
    }
}
