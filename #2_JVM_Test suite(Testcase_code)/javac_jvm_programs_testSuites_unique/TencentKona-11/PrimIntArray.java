




public class PrimIntArray {

    public static void main(String args[]) throws Throwable {
        System.out.println("Regression test for bug 8129895");

        try {
            Class newClass = Class.forName("primArray");
            throw new RuntimeException("Expected VerifyError exception not thrown with new verifier");
        } catch (java.lang.VerifyError e) {
            System.out.println("Test PrimIntArray passed with new verifier");
        }

        try {
            Class newClass = Class.forName("primArray49");
            throw new RuntimeException("Expected VerifyError exception not thrown by old verifier");
        } catch (java.lang.VerifyError e) {
            System.out.println("Test PrimIntArray passed with old verifier");
        }
    }
}
