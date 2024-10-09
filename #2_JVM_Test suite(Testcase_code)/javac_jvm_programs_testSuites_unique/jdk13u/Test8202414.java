
package compiler.c2;

import sun.misc.Unsafe;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class Test8202414 {

    public static void main(String[] args) {
        System.err.close();
        int count = 0;
        while (count++ < 120000) {
            test();
        }
    }

    public static void test() {
        byte[] newBufb = serByte(397);
        short[] newBufs = serShort(397);
        int[] newBufi = serInt(397);
        long[] newBufl = serLong(397);
        if (newBufb.length != 397 || newBufs.length != 397 || newBufi.length != 397 || newBufl.length != 397) {
            System.out.println("array length internal error");
            throw new RuntimeException("Test failed");
        }
    }

    public static byte[] serByte(int bufLen) {
        byte[] buf = new byte[bufLen];
        THE_UNSAFE.putByte(buf, BYTE_ARRAY_BASE_OFFSET + 1, (byte) buf.length);
        System.err.println("ref " + buf);
        return buf;
    }

    public static short[] serShort(int bufLen) {
        short[] buf = new short[bufLen];
        THE_UNSAFE.putShort(buf, SHORT_ARRAY_BASE_OFFSET + 1, (short) buf.length);
        System.err.println("ref " + buf);
        return buf;
    }

    public static int[] serInt(int bufLen) {
        int[] buf = new int[bufLen];
        THE_UNSAFE.putInt(buf, INT_ARRAY_BASE_OFFSET + 1, buf.length);
        System.err.println("ref " + buf);
        return buf;
    }

    public static long[] serLong(int bufLen) {
        long[] buf = new long[bufLen];
        THE_UNSAFE.putLong(buf, LONG_ARRAY_BASE_OFFSET + 1, buf.length);
        System.err.println("ref " + buf);
        return buf;
    }

    static final Unsafe THE_UNSAFE;

    static final long BYTE_ARRAY_BASE_OFFSET;

    static final long SHORT_ARRAY_BASE_OFFSET;

    static final long INT_ARRAY_BASE_OFFSET;

    static final long LONG_ARRAY_BASE_OFFSET;

    static {
        THE_UNSAFE = (Unsafe) AccessController.doPrivileged(new PrivilegedAction<Object>() {

            @Override
            public Object run() {
                try {
                    Field f = Unsafe.class.getDeclaredField("theUnsafe");
                    f.setAccessible(true);
                    return f.get(null);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new Error();
                }
            }
        });
        BYTE_ARRAY_BASE_OFFSET = THE_UNSAFE.arrayBaseOffset(byte[].class);
        SHORT_ARRAY_BASE_OFFSET = THE_UNSAFE.arrayBaseOffset(short[].class);
        INT_ARRAY_BASE_OFFSET = THE_UNSAFE.arrayBaseOffset(int[].class);
        LONG_ARRAY_BASE_OFFSET = THE_UNSAFE.arrayBaseOffset(long[].class);
    }
}
