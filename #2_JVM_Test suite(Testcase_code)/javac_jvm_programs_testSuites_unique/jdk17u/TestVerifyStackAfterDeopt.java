
package compiler.interpreter;

public class TestVerifyStackAfterDeopt {

    private long method(long l1, long l2, Object[] a) {
        return l1 + l2;
    }

    private long[] result = new long[1];

    private void test() {
        this.result[0] = method(1L, 2L, new Object[0]);
    }

    public static void main(String[] args) {
        TestVerifyStackAfterDeopt t = new TestVerifyStackAfterDeopt();
        for (int i = 0; i < 100_000; ++i) {
            t.test();
        }
    }
}
