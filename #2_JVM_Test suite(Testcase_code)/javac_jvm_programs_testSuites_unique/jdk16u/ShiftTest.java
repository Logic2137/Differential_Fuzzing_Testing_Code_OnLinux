



package compiler.codegen;

public class ShiftTest {
    static final int w = 32;

    private static void doTest(long ct) throws Exception {
        int S22 = 0xc46cf7c2;
        int S23 = 0xcfda9162;
        int S24 = 0xd029aa4c;
        int S25 = 0x17cf1801;
        int A = (int)(ct & 0xffffffffL);
        int B = (int)(ct >>> 32);
        int x, y;
        x = B - S25;
        y = A & (w-1);
        B = ((x >>> y) | (x << (w-y))) ^ A;
        x = A - S24;
        y = B & (w-1);
        A = ((x >>> y) | (x << (w-y))) ^ B;
        x = B - S23;
        y = A & (w-1);
        B = ((x >>> y) | (x << (w-y))) ^ A;
        x = A - S22;
        y = B & (w-1);
        A = ((x >>> y) | (x << (w-y))) ^ B;
        String astr = Integer.toHexString(A);
        String bstr = Integer.toHexString(B);
        System.err.println("A = " + astr + " B = " + bstr);
        if ((!astr.equals("dcb38144")) ||
            (!bstr.equals("1916de73"))) {
            throw new RuntimeException("Unexpected shift results!");
        }
        System.err.println("Test passed");
    }

    public static void main(String[] args) throws Exception {
        doTest(0x496def29b74be041L);
    }
}
