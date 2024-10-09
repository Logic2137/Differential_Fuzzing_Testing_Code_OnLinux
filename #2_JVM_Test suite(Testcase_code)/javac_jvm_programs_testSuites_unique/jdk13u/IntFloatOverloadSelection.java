
package jdk.nashorn.test.models;

@SuppressWarnings("javadoc")
public class IntFloatOverloadSelection {

    public static String overloadedMethod(final int i) {
        return "int";
    }

    public static String overloadedMethod(final float f) {
        return "float";
    }
}
