


package compiler.c2;

public class TestSignExtract {

    private static final long[] LONG_VALUES = {0L, 0xFFFFFFFFL, 0x12L, -1L, -123L, -0x12L, Long.MAX_VALUE, Long.MIN_VALUE};
    private static final int[] INT_VALUES = {0, 0x1234, -1, -0x12345678, Integer.MAX_VALUE, Integer.MIN_VALUE};


    private static int signExtractInt1(int x) {
        return (x >> 1) >>> 31;
    }

    private static int signExtractInt2(int x) {
        return (x >> 32) >>> 31;
    }

    private static int signExtractInt3(int x) {
        return (x >> 31) >>> 31;
    }

    private static int signExtractInt4(int x) {
        return 0 - (x >> 31);
    }

    private static long signExtractLong1(long x) {
        return (x >> 1) >>> 63;
    }

    private static long signExtractLong2(long x) {
        return (x >> 54) >>> 63;
    }

    private static long signExtractLong3(long x) {
        return (x >> 63) >>> 63;
    }

    private static long signExtractLong4(long x) {
        return 0 - (x >> 63);
    }

    private static int WARMUP = 5000;

    public static void main(String[] args) {
        for (int i = 0; i < WARMUP; i++) {
            for (int e : INT_VALUES) {
                
                assert e >>> 31 == signExtractInt1(e);
                assert e >>> 31 == signExtractInt2(e);
                assert e >>> 31 == signExtractInt3(e);
                
                assert e >>> 31 == signExtractInt4(e);
            }

            for (long e : LONG_VALUES) {
                
                assert e >>> 63 == signExtractLong1(e);
                assert e >>> 63 == signExtractLong2(e);
                assert e >>> 63 == signExtractLong3(e);
                
                assert e >>> 63 == signExtractLong4(e);
            }
        }
    }
}
