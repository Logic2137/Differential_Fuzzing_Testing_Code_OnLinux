public class ReturnMsgs {

    public static void main(String[] args) throws Throwable {
        System.out.println("Regression test for bug 8262368");
        try {
            Class newClass = Class.forName("VoidReturnSignature");
            throw new RuntimeException("Expected VerifyError exception not thrown");
        } catch (java.lang.VerifyError e) {
            String eMsg = e.getMessage();
            if (!eMsg.contains("Method does not expect a return value")) {
                throw new RuntimeException("Unexpected exception message: " + eMsg);
            }
        }
        try {
            Class newClass = Class.forName("NonVoidReturnSignature");
            throw new RuntimeException("Expected VerifyError exception not thrown");
        } catch (java.lang.VerifyError e) {
            String eMsg = e.getMessage();
            if (!eMsg.contains("Method expects a return value")) {
                throw new RuntimeException("Unexpected exception message: " + eMsg);
            }
        }
    }
}
