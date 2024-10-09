
package compiler.c2;

public class Test8bitLogicalOperators {

    private static byte and = 0b0011, or = 0b0011, xor = 0b0011;

    private static byte mask = 0b0101;

    public static void main(String... args) {
        test();
        if (and != 0b0001 || or != 0b0111 || xor != 0b0110)
            throw new AssertionError("8-bit logical operator failure");
    }

    public static void test() {
        and &= mask;
        or |= mask;
        xor ^= mask;
    }
}
