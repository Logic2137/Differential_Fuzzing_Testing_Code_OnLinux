
package compiler.types;

public class TestTypePropagationToCmpU {

    public static void main(String[] args) {
        try {
            for (int i = 0; i < 100_000; ++i) {
                test();
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("Test failed");
        }
    }

    static int global = 42;

    public static void test() {
        int a = Integer.MIN_VALUE;
        int b = global;
        char[] buf = { 0 };
        for (int i = 0; i <= b; ++i) {
            a = i - b;
        }
        char c = buf[(a * 11) / 2 - a];
        buf[0] = 0;
    }
}
