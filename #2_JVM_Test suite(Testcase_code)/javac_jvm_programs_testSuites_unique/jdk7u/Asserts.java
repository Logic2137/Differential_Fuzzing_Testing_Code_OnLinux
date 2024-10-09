

package com.oracle.java.testlibrary;


public class Asserts {

    
    public static <T extends Comparable<T>> void assertLT(T lhs, T rhs) {
        assertLessThan(lhs, rhs);
    }

    
    public static <T extends Comparable<T>> void assertLT(T lhs, T rhs, String msg) {
        assertLessThan(lhs, rhs, msg);
    }

    
    public static <T extends Comparable<T>> void assertLessThan(T lhs, T rhs) {
        String msg = "Expected that " + format(lhs) + " < " + format(rhs);
        assertLessThan(lhs, rhs, msg);
    }

    
    public static <T extends Comparable<T>>void assertLessThan(T lhs, T rhs, String msg) {
        assertTrue(compare(lhs, rhs, msg) < 0, msg);
    }

    
    public static <T extends Comparable<T>> void assertLTE(T lhs, T rhs) {
        assertLessThanOrEqual(lhs, rhs);
    }

    
    public static <T extends Comparable<T>> void assertLTE(T lhs, T rhs, String msg) {
        assertLessThanOrEqual(lhs, rhs, msg);
    }

    
    public static <T extends Comparable<T>> void assertLessThanOrEqual(T lhs, T rhs) {
        String msg = "Expected that " + format(lhs) + " <= " + format(rhs);
        assertLessThanOrEqual(lhs, rhs, msg);
    }

    
    public static <T extends Comparable<T>> void assertLessThanOrEqual(T lhs, T rhs, String msg) {
        assertTrue(compare(lhs, rhs, msg) <= 0, msg);
    }

    
    public static void assertEQ(Object lhs, Object rhs) {
        assertEquals(lhs, rhs);
    }

    
    public static void assertEQ(Object lhs, Object rhs, String msg) {
        assertEquals(lhs, rhs, msg);
    }

    
    public static void assertEquals(Object lhs, Object rhs) {
        String msg = "Expected " + format(lhs) + " to equal " + format(rhs);
        assertEquals(lhs, rhs, msg);
    }

    
    public static void assertEquals(Object lhs, Object rhs, String msg) {
        if (lhs == null) {
            if (rhs != null) {
                error(msg);
            }
        } else {
            assertTrue(lhs.equals(rhs), msg);
        }
    }

    
    public static <T extends Comparable<T>> void assertGTE(T lhs, T rhs) {
        assertGreaterThanOrEqual(lhs, rhs);
    }

    
    public static <T extends Comparable<T>> void assertGTE(T lhs, T rhs, String msg) {
        assertGreaterThanOrEqual(lhs, rhs, msg);
    }

    
    public static <T extends Comparable<T>> void assertGreaterThanOrEqual(T lhs, T rhs) {
        String msg = "Expected that " + format(lhs) + " >= " + format(rhs);
        assertGreaterThanOrEqual(lhs, rhs, msg);
    }

    
    public static <T extends Comparable<T>> void assertGreaterThanOrEqual(T lhs, T rhs, String msg) {
        assertTrue(compare(lhs, rhs, msg) >= 0, msg);
    }

    
    public static <T extends Comparable<T>> void assertGT(T lhs, T rhs) {
        assertGreaterThan(lhs, rhs);
    }

    
    public static <T extends Comparable<T>> void assertGT(T lhs, T rhs, String msg) {
        assertGreaterThan(lhs, rhs, msg);
    }

    
    public static <T extends Comparable<T>> void assertGreaterThan(T lhs, T rhs) {
        String msg = "Expected that " + format(lhs) + " > " + format(rhs);
        assertGreaterThan(lhs, rhs, msg);
    }

    
    public static <T extends Comparable<T>> void assertGreaterThan(T lhs, T rhs, String msg) {
        assertTrue(compare(lhs, rhs, msg) > 0, msg);
    }

    
    public static void assertNE(Object lhs, Object rhs) {
        assertNotEquals(lhs, rhs);
    }

    
    public static void assertNE(Object lhs, Object rhs, String msg) {
        assertNotEquals(lhs, rhs, msg);
    }

    
    public static void assertNotEquals(Object lhs, Object rhs) {
        String msg = "Expected " + format(lhs) + " to not equal " + format(rhs);
        assertNotEquals(lhs, rhs, msg);
    }

    
    public static void assertNotEquals(Object lhs, Object rhs, String msg) {
        if (lhs == null) {
            if (rhs == null) {
                error(msg);
            }
        } else {
            assertFalse(lhs.equals(rhs), msg);
        }
    }

    
    public static void assertNull(Object o) {
        assertNull(o, "Expected " + format(o) + " to be null");
    }

    
    public static void assertNull(Object o, String msg) {
        assertEquals(o, null, msg);
    }

    
    public static void assertNotNull(Object o) {
        assertNotNull(o, "Expected non null reference");
    }

    
    public static void assertNotNull(Object o, String msg) {
        assertNotEquals(o, null, msg);
    }

    
    public static void assertFalse(boolean value) {
        assertFalse(value, "Expected value to be false");
    }

    
    public static void assertFalse(boolean value, String msg) {
        assertTrue(!value, msg);
    }

    
    public static void assertTrue(boolean value) {
        assertTrue(value, "Expected value to be true");
    }

    
    public static void assertTrue(boolean value, String msg) {
        if (!value) {
            error(msg);
        }
    }

    private static <T extends Comparable<T>> int compare(T lhs, T rhs, String msg) {
        assertNotNull(lhs, msg);
        assertNotNull(rhs, msg);
        return lhs.compareTo(rhs);
    }

    private static String format(Object o) {
        return o == null? "null" : o.toString();
    }

    private static void error(String msg) {
        throw new RuntimeException(msg);
    }

}
