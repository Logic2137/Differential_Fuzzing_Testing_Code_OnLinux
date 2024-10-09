 



public class StackMapCheck {
    public static void main(String args[]) throws Throwable {

        System.out.println("Regression test for bug 7127066");
        try {
            Class newClass = Class.forName("BadMap");
            throw new RuntimeException(
                "StackMapCheck failed, BadMap did not throw VerifyError");
        } catch (java.lang.VerifyError e) {
            System.out.println("BadMap passed, VerifyError was thrown");
        }

        try {
            Class newClass = Class.forName("BadMapDstore");
            throw new RuntimeException(
                "StackMapCheck failed, BadMapDstore did not throw VerifyError");
        } catch (java.lang.VerifyError e) {
            System.out.println("BadMapDstore passed, VerifyError was thrown");
        }

        try {
            Class newClass = Class.forName("BadMapIstore");
            throw new RuntimeException(
                "StackMapCheck failed, BadMapIstore did not throw VerifyError");
        } catch (java.lang.VerifyError e) {
            System.out.println("BadMapIstore passed, VerifyError was thrown");
        }
    }
}
