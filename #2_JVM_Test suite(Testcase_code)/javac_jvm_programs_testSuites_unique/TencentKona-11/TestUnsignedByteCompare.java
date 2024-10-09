



package compiler.c2;

public class TestUnsignedByteCompare {

    static int p, n;

    static void report(byte[] ba, int i, boolean failed) {
        
        
    }

    static void m1(byte[] ba) {
        for (int i = 0; i < ba.length; i++) {
            if ((ba[i] & 0xFF) < 0x10) {
               p++;
               report(ba, i, true);
            } else {
               n++;
               report(ba, i, false);
            }
        }
    }

    static void m2(byte[] ba) {
        for (int i = 0; i < ba.length; i++) {
            if (((ba[i] & 0xFF) & 0x80) < 0) {
               p++;
               report(ba, i, true);
            } else {
               n++;
               report(ba, i, false);
            }
        }
    }

    static public void main(String[] args) {
        final int tries = 1_000;
        final int count = 1_000;

        byte[] ba = new byte[count];

        for (int i = 0; i < count; i++) {
            int v = -(i % 126 + 1);
            ba[i] = (byte)v;
        }

        for (int t = 0; t < tries; t++) {
            m1(ba);
            if (p != 0) {
                throw new IllegalStateException("m1 error: p = " + p + ", n = " + n);
            }
        }

        for (int t = 0; t < tries; t++) {
            m2(ba);
            if (p != 0) {
                throw new IllegalStateException("m2 error: p = " + p + ", n = " + n);
            }
        }
    }
}
