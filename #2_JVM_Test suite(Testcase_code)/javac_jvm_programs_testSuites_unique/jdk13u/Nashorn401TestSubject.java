
package jdk.nashorn.test.models;

@SuppressWarnings("javadoc")
public class Nashorn401TestSubject {

    public String method2(final int arg) {
        return "int method 2";
    }

    public String method2(final double arg) {
        return "double method 2";
    }

    public String method2(final String arg) {
        return "string method 2";
    }

    public String method3(final double arg) {
        return "double method 3: " + arg;
    }

    public String method3(final int arg) {
        return "int method 3: " + arg;
    }

    public String method4(final Double arg) {
        return "double method 4: " + arg;
    }

    public String method4(final int arg) {
        return "int method 4: " + arg;
    }
}
