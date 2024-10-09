
package jdk.test.lib;

import java.util.Objects;

public class Asserts {

    public static <T extends Comparable<T>> void assertLT(T lhs, T rhs) {
        assertLessThan(lhs, rhs);
    }

    public static <T extends Comparable<T>> void assertLT(T lhs, T rhs, String msg) {
        assertLessThan(lhs, rhs, msg);
    }

    public static <T extends Comparable<T>> void assertLessThan(T lhs, T rhs) {
        assertLessThan(lhs, rhs, null);
    }

    public static <T extends Comparable<T>> void assertLessThan(T lhs, T rhs, String msg) {
        if (!(compare(lhs, rhs, msg) < 0)) {
            msg = Objects.toString(msg, "assertLessThan") + ": expected that " + Objects.toString(lhs) + " < " + Objects.toString(rhs);
            fail(msg);
        }
    }

    public static <T extends Comparable<T>> void assertLTE(T lhs, T rhs) {
        assertLessThanOrEqual(lhs, rhs);
    }

    public static <T extends Comparable<T>> void assertLTE(T lhs, T rhs, String msg) {
        assertLessThanOrEqual(lhs, rhs, msg);
    }

    public static <T extends Comparable<T>> void assertLessThanOrEqual(T lhs, T rhs) {
        assertLessThanOrEqual(lhs, rhs, null);
    }

    public static <T extends Comparable<T>> void assertLessThanOrEqual(T lhs, T rhs, String msg) {
        if (!(compare(lhs, rhs, msg) <= 0)) {
            msg = Objects.toString(msg, "assertLessThanOrEqual") + ": expected that " + Objects.toString(lhs) + " <= " + Objects.toString(rhs);
            fail(msg);
        }
    }

    public static void assertEQ(Object lhs, Object rhs) {
        assertEquals(lhs, rhs);
    }

    public static void assertEQ(Object lhs, Object rhs, String msg) {
        assertEquals(lhs, rhs, msg);
    }

    public static void assertEquals(Object lhs, Object rhs) {
        assertEquals(lhs, rhs, null);
    }

    public static void assertEquals(Object lhs, Object rhs, String msg) {
        if ((lhs != rhs) && ((lhs == null) || !(lhs.equals(rhs)))) {
            msg = Objects.toString(msg, "assertEquals") + ": expected " + Objects.toString(lhs) + " to equal " + Objects.toString(rhs);
            fail(msg);
        }
    }

    public static void assertSame(Object lhs, Object rhs) {
        assertSame(lhs, rhs, null);
    }

    public static void assertSame(Object lhs, Object rhs, String msg) {
        if (lhs != rhs) {
            msg = Objects.toString(msg, "assertSame") + ": expected " + Objects.toString(lhs) + " to equal " + Objects.toString(rhs);
            fail(msg);
        }
    }

    public static <T extends Comparable<T>> void assertGTE(T lhs, T rhs) {
        assertGreaterThanOrEqual(lhs, rhs);
    }

    public static <T extends Comparable<T>> void assertGTE(T lhs, T rhs, String msg) {
        assertGreaterThanOrEqual(lhs, rhs, msg);
    }

    public static <T extends Comparable<T>> void assertGreaterThanOrEqual(T lhs, T rhs) {
        assertGreaterThanOrEqual(lhs, rhs, null);
    }

    public static <T extends Comparable<T>> void assertGreaterThanOrEqual(T lhs, T rhs, String msg) {
        if (!(compare(lhs, rhs, msg) >= 0)) {
            msg = Objects.toString(msg, "assertGreaterThanOrEqual") + ": expected " + Objects.toString(lhs) + " >= " + Objects.toString(rhs);
            fail(msg);
        }
    }

    public static <T extends Comparable<T>> void assertGT(T lhs, T rhs) {
        assertGreaterThan(lhs, rhs);
    }

    public static <T extends Comparable<T>> void assertGT(T lhs, T rhs, String msg) {
        assertGreaterThan(lhs, rhs, msg);
    }

    public static <T extends Comparable<T>> void assertGreaterThan(T lhs, T rhs) {
        assertGreaterThan(lhs, rhs, null);
    }

    public static <T extends Comparable<T>> void assertGreaterThan(T lhs, T rhs, String msg) {
        if (!(compare(lhs, rhs, msg) > 0)) {
            msg = Objects.toString(msg, "assertGreaterThan") + ": expected " + Objects.toString(lhs) + " > " + Objects.toString(rhs);
            fail(msg);
        }
    }

    public static void assertNE(Object lhs, Object rhs) {
        assertNotEquals(lhs, rhs);
    }

    public static void assertNE(Object lhs, Object rhs, String msg) {
        assertNotEquals(lhs, rhs, msg);
    }

    public static void assertNotEquals(Object lhs, Object rhs) {
        assertNotEquals(lhs, rhs, null);
    }

    public static void assertNotEquals(Object lhs, Object rhs, String msg) {
        if ((lhs == rhs) || (lhs != null && lhs.equals(rhs))) {
            msg = Objects.toString(msg, "assertNotEquals") + ": expected " + Objects.toString(lhs) + " to not equal " + Objects.toString(rhs);
            fail(msg);
        }
    }

    public static void assertNull(Object o) {
        assertNull(o, null);
    }

    public static void assertNull(Object o, String msg) {
        assertEquals(o, null, msg);
    }

    public static void assertNotNull(Object o) {
        assertNotNull(o, null);
    }

    public static void assertNotNull(Object o, String msg) {
        assertNotEquals(o, null, msg);
    }

    public static void assertFalse(boolean value) {
        assertFalse(value, null);
    }

    public static void assertFalse(boolean value, String msg) {
        if (value) {
            msg = Objects.toString(msg, "assertFalse") + ": expected false, was true";
            fail(msg);
        }
    }

    public static void assertTrue(boolean value) {
        assertTrue(value, null);
    }

    public static void assertTrue(boolean value, String msg) {
        if (!value) {
            msg = Objects.toString(msg, "assertTrue") + ": expected true, was false";
            fail(msg);
        }
    }

    private static <T extends Comparable<T>> int compare(T lhs, T rhs, String msg) {
        if (lhs == null || rhs == null) {
            fail(lhs, rhs, msg + ": values must be non-null:", ",");
        }
        return lhs.compareTo(rhs);
    }

    public static void assertStringsEqual(String str1, String str2, String msg) {
        String lineSeparator = System.getProperty("line.separator");
        String[] str1Lines = str1.split(lineSeparator);
        String[] str2Lines = str2.split(lineSeparator);
        int minLength = Math.min(str1Lines.length, str2Lines.length);
        String[] longestStringLines = ((str1Lines.length == minLength) ? str2Lines : str1Lines);
        boolean stringsAreDifferent = false;
        StringBuilder messageBuilder = new StringBuilder(msg);
        messageBuilder.append("\n");
        for (int line = 0; line < minLength; line++) {
            if (!str1Lines[line].equals(str2Lines[line])) {
                messageBuilder.append(String.format("[line %d] '%s' differs " + "from '%s'\n", line, str1Lines[line], str2Lines[line]));
                stringsAreDifferent = true;
            }
        }
        if (minLength < longestStringLines.length) {
            String stringName = ((longestStringLines == str1Lines) ? "first" : "second");
            messageBuilder.append(String.format("Only %s string contains " + "following lines:\n", stringName));
            stringsAreDifferent = true;
            for (int line = minLength; line < longestStringLines.length; line++) {
                messageBuilder.append(String.format("[line %d] '%s'", line, longestStringLines[line]));
            }
        }
        if (stringsAreDifferent) {
            fail(messageBuilder.toString());
        }
    }

    public static String format(Object lhs, Object rhs, String message, String relation) {
        StringBuilder sb = new StringBuilder(80);
        if (message != null) {
            sb.append(message);
            sb.append(' ');
        }
        sb.append("<");
        sb.append(Objects.toString(lhs));
        sb.append("> ");
        sb.append(Objects.toString(relation, ","));
        sb.append(" <");
        sb.append(Objects.toString(rhs));
        sb.append(">");
        return sb.toString();
    }

    public static void fail() {
        fail("fail");
    }

    public static void fail(String message) {
        throw new RuntimeException(message);
    }

    public static void fail(Object lhs, Object rhs, String message, String relation) {
        throw new RuntimeException(format(lhs, rhs, message, relation));
    }

    public static void fail(String message, Throwable cause) {
        throw new RuntimeException(message, cause);
    }
}
