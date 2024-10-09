

package compiler.c2;


public class TestImplicitNullCheckDominance {

    double dFld;
    int iFld;

    static void test1(TestImplicitNullCheckDominance t, double d) {
        for (int i = 0; i < 100; i++) {
            t.dFld = d % 42;
            t.iFld = 43;
        }
    }

    static void test2(TestImplicitNullCheckDominance t) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 100; j++) {
               t.dFld %= 42;
               t.iFld = 43;
            }
        }
    }

    public static void main(String[] args) {
        TestImplicitNullCheckDominance t = new TestImplicitNullCheckDominance();
        for (int i = 0; i < 50_000; ++i) {
            test1(t, i);
            test2(t);
        }
    }
}
