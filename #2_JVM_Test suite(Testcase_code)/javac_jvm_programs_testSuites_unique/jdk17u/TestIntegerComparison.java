
package compiler.integerArithmetic;

public class TestIntegerComparison {

    public static void testSigned(char c) {
        int result = c + Integer.MAX_VALUE;
        if (result == Character.MAX_VALUE) {
            throw new RuntimeException("Should not reach here!");
        }
    }

    public static void testUnsigned(char c) {
        int result = c - (Character.MAX_VALUE - Integer.MIN_VALUE) + 2;
        if (1 < result && result < 100) {
            throw new RuntimeException("Should not reach here!");
        }
    }

    public static void main(String[] args) {
        for (int i = Character.MIN_VALUE; i <= Character.MAX_VALUE; ++i) {
            testSigned((char) i);
            testUnsigned((char) i);
        }
    }
}
