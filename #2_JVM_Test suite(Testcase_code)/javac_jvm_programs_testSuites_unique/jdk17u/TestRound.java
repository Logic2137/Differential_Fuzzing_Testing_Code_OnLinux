
package compiler.floatingpoint;

public class TestRound {

    public static void main(String[] args) {
        for (int i = 0; i < 10_000; i++) {
            Math.round(Double.NaN);
        }
        if (Math.round(1d) != 1) {
            throw new AssertionError("TEST FAILED");
        }
        System.out.println("Test passed.");
    }
}
