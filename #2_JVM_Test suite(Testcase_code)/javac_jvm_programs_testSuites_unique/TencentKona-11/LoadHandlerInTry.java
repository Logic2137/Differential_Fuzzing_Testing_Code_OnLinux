





public class LoadHandlerInTry {

    public static void main(String[] args) throws Exception {
        System.out.println("Regression test for bug 8075118");
        try {
            Class newClass = Class.forName("HandlerInTry");
            throw new RuntimeException(
                 "Failed to throw VerifyError for HandlerInTry");
        } catch (java.lang.VerifyError e) {
            System.out.println("Passed: VerifyError exception was thrown");
        }

        try {
            Class newClass = Class.forName("IsolatedHandlerInTry");
            throw new RuntimeException(
                 "Failed to throw VerifyError for IsolatedHandlerInTry");
        } catch (java.lang.VerifyError e) {
            System.out.println("Passed: VerifyError exception was thrown");
        }
    }
}
