

package jdk.nashorn.test.models;

import java.util.Arrays;


@SuppressWarnings("javadoc")
public class LongProvider {

    final static long[][] arrays = {
            {1L, 2L, 3L},
            {1L, 1L << 30, 1L << 50, 4L},
            {1L, Long.MAX_VALUE, Long.MIN_VALUE, 4L}
    };

    public static long getLong(final String str) {
        final long l = Long.parseLong(str);
        checkLong(l, str);
        return l;
    }

    public static long[] getLongArray(final int n) {
        return arrays[n];
    }

    public static void checkLong(final long value, final String str) {
        if (!Long.toString(value).equals(str)) {
            throw new RuntimeException("Wrong value. Expected " + str + ", got " + value);
        }
    }

    public static void checkLongArray(final long[] array, final int n) {
        if (!Arrays.equals(array, arrays[n])) {
            throw new RuntimeException("Arrays don't match: " + Arrays.toString(array) + ", " + Arrays.toString(arrays[n]));
        }
    }
}
