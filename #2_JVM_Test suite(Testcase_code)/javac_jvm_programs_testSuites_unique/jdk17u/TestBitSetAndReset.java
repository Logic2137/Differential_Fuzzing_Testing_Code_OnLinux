
package compiler.c2;

public class TestBitSetAndReset {

    private static final int COUNT = 10_000;

    private static final long MASK63 = 0x8000_0000_0000_0000L;

    private static final long MASK32 = 0x0000_0001_0000_0000L;

    private static final long MASK31 = 0x0000_0000_8000_0000L;

    private static final long MASK15 = 0x0000_0000_0000_8000L;

    private static final long MASK00 = 0x0000_0000_0000_0001L;

    private static long andq, orq;

    public static void main(String... args) {
        boolean success = true;
        for (int i = 0; i < COUNT; i++) {
            andq = MASK63 | MASK31 | MASK15 | MASK00;
            orq = 0;
            test63();
            test32();
            test31();
            test15();
            test00();
            success &= andq == 0 && orq == (MASK63 | MASK32 | MASK31 | MASK15 | MASK00);
        }
        if (!success)
            throw new AssertionError("Failure while setting or clearing long vector bits!");
    }

    private static void test63() {
        andq &= ~MASK63;
        orq |= MASK63;
    }

    private static void test32() {
        andq &= ~MASK32;
        orq |= MASK32;
    }

    private static void test31() {
        andq &= ~MASK31;
        orq |= MASK31;
    }

    private static void test15() {
        andq &= ~MASK15;
        orq |= MASK15;
    }

    private static void test00() {
        andq &= ~MASK00;
        orq |= MASK00;
    }
}
