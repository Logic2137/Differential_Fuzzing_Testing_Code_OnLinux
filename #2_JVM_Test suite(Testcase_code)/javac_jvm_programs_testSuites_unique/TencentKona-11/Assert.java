

package org.junit;

public class Assert {
    public static void assertEquals(Object o1, Object o2) {
        if (!o1.equals(o2)) throw new RuntimeException();
    }
}
