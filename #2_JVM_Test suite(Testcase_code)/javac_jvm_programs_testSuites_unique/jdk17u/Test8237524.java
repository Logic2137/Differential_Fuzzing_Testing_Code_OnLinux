
package compiler.intrinsics;

import java.lang.reflect.Constructor;

public class Test8237524 {

    private static int stringCompareTo(String s1, String s2) {
        return s1.compareTo(s2);
    }

    public static void main(String[] args) throws Exception {
        Constructor<String> c = String.class.getDeclaredConstructor(byte[].class, byte.class);
        c.setAccessible(true);
        byte[] bytes = new byte[] { 'Y', 'm', '_', 'l', 'V', 'n', 'W', 'S', 'w', 'm', 'W', 'S' };
        String s1 = c.newInstance(bytes, (byte) 0);
        String s2 = c.newInstance(bytes, (byte) 1);
        for (int i = 0; i < 50000; i++) {
            if (stringCompareTo(s1, s2) >= 0) {
                System.out.println("FAIL. s1 should be less than s2 according to Java API Spec");
                System.exit(1);
            }
        }
    }
}
