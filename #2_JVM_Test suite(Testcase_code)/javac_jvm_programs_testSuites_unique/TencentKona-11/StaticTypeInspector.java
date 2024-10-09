
package jdk.nashorn.test.tools;

import jdk.nashorn.internal.runtime.Undefined;

@SuppressWarnings("javadoc")
public class StaticTypeInspector {

    public static String inspect(final boolean x, final String w) {
        return w + ": boolean";
    }

    public static String inspect(final int x, final String w) {
        return w + ": int";
    }

    public static String inspect(final long x, final String w) {
        return w + ": long";
    }

    public static String inspect(final double x, final String w) {
        return w + ": double";
    }

    public static String inspect(final Undefined x, final String w) {
        return w + ": undefined";
    }

    public static String inspect(final Object x, final String w) {
        return w + ": object";
    }
}
