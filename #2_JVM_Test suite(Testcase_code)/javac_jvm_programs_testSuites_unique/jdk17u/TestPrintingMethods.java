
package test;

public class TestPrintingMethods {

    private static String expectedErrorMessage_VV = "void test.TeMe3_B.ma()";

    private static String expectedErrorMessage_integral = "double[][] test.TeMe3_B.ma(int, boolean, byte[][], float)";

    private static String expectedErrorMessage_classes = "test.TeMe3_B[][] test.TeMe3_B.ma(java.lang.Object[][][])";

    private static String expectedErrorMessage_unicode = "java.lang.Object test.TeMe3_B.m\u20ac\u00a3a(java.lang.Object)";

    static void checkMsg(Error e, String expected) throws Exception {
        String errorMsg = e.getMessage();
        if (errorMsg == null) {
            throw new RuntimeException("Caught AbstractMethodError with empty message.");
        } else if (errorMsg.contains(expected)) {
            System.out.println("Passed with message: " + errorMsg);
        } else {
            System.out.println("Expected method to be printed as \"" + expected + "\"\n" + "in exception message:  " + errorMsg);
            throw new RuntimeException("Method not printed as expected.");
        }
    }

    static void test() throws Exception {
        TeMe3_A c = new TeMe3_C();
        try {
            c.ma();
            throw new RuntimeException("Expected AbstractMethodError was not thrown.");
        } catch (AbstractMethodError e) {
            checkMsg(e, expectedErrorMessage_VV);
        }
        try {
            c.ma(2, true, new byte[2][3], 23.4f);
            throw new RuntimeException("Expected AbstractMethodError was not thrown.");
        } catch (AbstractMethodError e) {
            checkMsg(e, expectedErrorMessage_integral);
        }
        try {
            c.ma(new java.lang.Object[1][2][3]);
            throw new RuntimeException("Expected AbstractMethodError was not thrown.");
        } catch (AbstractMethodError e) {
            checkMsg(e, expectedErrorMessage_classes);
        }
        try {
            c.m\u20ac\u00a3a(new java.lang.Object());
            throw new RuntimeException("Expected AbstractMethodError was not thrown.");
        } catch (AbstractMethodError e) {
            checkMsg(e, expectedErrorMessage_unicode);
        }
    }

    public static void main(String[] args) throws Exception {
        test();
    }
}

class TeMe3_A {

    public void ma() {
        System.out.print("A.ma()");
    }

    public double[][] ma(int i, boolean z, byte[][] b, float f) {
        return null;
    }

    public TeMe3_B[][] ma(java.lang.Object[][][] o) {
        return null;
    }

    public java.lang.Object m\u20ac\u00a3a(java.lang.Object s) {
        return null;
    }
}

abstract class TeMe3_B extends TeMe3_A {

    public abstract void ma();

    public abstract double[][] ma(int i, boolean z, byte[][] b, float f);

    public abstract TeMe3_B[][] ma(java.lang.Object[][][] o);

    public abstract java.lang.Object m\u20ac\u00a3a(java.lang.Object s);
}

class TeMe3_C extends TeMe3_B {

    public void ma() {
        System.out.print("C.ma()");
    }

    public double[][] ma(int i, boolean z, byte[][] b, float f) {
        return new double[2][2];
    }

    public TeMe3_B[][] ma(java.lang.Object[][][] o) {
        return new TeMe3_C[3][3];
    }

    public java.lang.Object m\u20ac\u00a3a(java.lang.Object s) {
        return new java.lang.Object();
    }
}
