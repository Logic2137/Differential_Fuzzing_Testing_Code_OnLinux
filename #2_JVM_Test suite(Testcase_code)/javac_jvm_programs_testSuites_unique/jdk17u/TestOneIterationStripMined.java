
package compiler.loopopts;

public class TestOneIterationStripMined {

    static int iField;

    static volatile Object oField;

    public static void test1(int val) {
        iField = 0;
        for (int i = 0; i < 1; ++i) {
            if (val == 0) {
                break;
            }
            val = 0;
        }
    }

    public static void test2(int val) {
        oField = null;
        for (int i = 0; i < 1; ++i) {
            if (val == 0) {
                break;
            }
            val = 0;
        }
    }

    public static void test3(int val) {
        for (int i = 0; i < 1; ++i) {
            if (val == 0) {
                break;
            }
            val = 0;
        }
    }

    public static void main(String[] args) {
        test1(0);
        test2(0);
        test3(0);
    }
}
