public class Test8000805 {

    static long loadS2LmaskFF(short[] sa) {
        return sa[0] & 0xFF;
    }

    static long loadS2LmaskFF_1(short[] sa) {
        return sa[0] & 0xFF;
    }

    static long loadS2Lmask16(short[] sa) {
        return sa[0] & 0xFFFE;
    }

    static long loadS2Lmask16_1(short[] sa) {
        return sa[0] & 0xFFFE;
    }

    static long loadS2Lmask13(short[] sa) {
        return sa[0] & 0x0FFF;
    }

    static long loadS2Lmask13_1(short[] sa) {
        return sa[0] & 0x0FFF;
    }

    static int loadUS_signExt(char[] ca) {
        return (ca[0] << 16) >> 16;
    }

    static int loadUS_signExt_1(char[] ca) {
        return (ca[0] << 16) >> 16;
    }

    static long loadB2L_mask8(byte[] ba) {
        return ba[0] & 0x55;
    }

    static long loadB2L_mask8_1(byte[] ba) {
        return ba[0] & 0x55;
    }

    public static void main(String[] args) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            byte[] ba = new byte[] { (byte) i };
            {
                long v1 = loadB2L_mask8(ba);
                long v2 = loadB2L_mask8_1(ba);
                if (v1 != v2)
                    throw new InternalError(String.format("loadB2L_mask8 failed: %x != %x", v1, v2));
            }
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            short[] sa = new short[] { (short) i };
            char[] ca = new char[] { (char) i };
            {
                long v1 = loadS2LmaskFF(sa);
                long v2 = loadS2LmaskFF_1(sa);
                if (v1 != v2)
                    throw new InternalError(String.format("loadS2LmaskFF failed: %x != %x", v1, v2));
            }
            {
                long v1 = loadS2Lmask16(sa);
                long v2 = loadS2Lmask16_1(sa);
                if (v1 != v2)
                    throw new InternalError(String.format("loadS2Lmask16 failed: %x != %x", v1, v2));
            }
            {
                long v1 = loadS2Lmask13(sa);
                long v2 = loadS2Lmask13_1(sa);
                if (v1 != v2)
                    throw new InternalError(String.format("loadS2Lmask13 failed: %x != %x", v1, v2));
            }
            {
                int v1 = loadUS_signExt(ca);
                int v2 = loadUS_signExt_1(ca);
                if (v1 != v2)
                    throw new InternalError(String.format("loadUS_signExt failed: %x != %x", v1, v2));
            }
        }
        System.out.println("TEST PASSED.");
    }
}
