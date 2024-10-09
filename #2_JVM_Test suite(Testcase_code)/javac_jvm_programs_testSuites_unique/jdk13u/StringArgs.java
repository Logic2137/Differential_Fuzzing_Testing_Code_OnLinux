
package jdk.nashorn.test.models;

import java.util.List;

@SuppressWarnings("javadoc")
public class StringArgs {

    public static void checkString(final List<?> list) {
        for (final Object s : list) {
            if (!(s instanceof String)) {
                throw new AssertionError("Not a String: " + s);
            }
        }
    }
}
