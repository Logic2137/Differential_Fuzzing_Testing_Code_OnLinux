
package compiler.loopopts;

public class TestRemoveMainPostLoops {

    static int cnt1 = 0;

    int cnt2 = 0;

    void testCallee() {
        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 10; ++j) {
                cnt1 += j;
            }
        }
    }

    void test() {
        for (int i = 0; i < 10_000; ++i) {
            testCallee();
            cnt2 = 0;
            for (int j = 0; j < 10; ++j) {
                cnt2 = cnt1 + j;
            }
        }
    }

    public static void main(String[] strArr) {
        TestRemoveMainPostLoops test = new TestRemoveMainPostLoops();
        for (int i = 0; i < 100; i++) {
            cnt1 = 0;
            test.cnt2 = 0;
            test.test();
            if (cnt1 != 45000000 || test.cnt2 != 45000009) {
                throw new RuntimeException("Incorrect result: " + cnt1 + " " + test.cnt2);
            }
        }
    }
}
