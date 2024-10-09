
package compiler.loopopts;

public class TestMoveStoreAfterLoopVerifyIterativeGVN {

    private static int[] iArr = new int[10];

    static void test() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j <= 10; j++) {
                iArr[i] = j;
            }
        }
    }

    static public void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            test();
        }
    }
}
