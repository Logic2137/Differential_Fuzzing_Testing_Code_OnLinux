public class TestSigParse {

    public static void main(String[] args) throws Throwable {
        System.out.println("Regression test for bug 819579");
        try {
            Class newClass = Class.forName("BadFieldRef");
            throw new RuntimeException("Expected ClasFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            String eMsg = e.getMessage();
            if (!eMsg.contains("Field") || !eMsg.contains("has illegal signature")) {
                throw new RuntimeException("Unexpected exception: " + eMsg);
            }
        }
        try {
            Class newClass = Class.forName("BadMethodRef");
            throw new RuntimeException("Expected ClasFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            String eMsg = e.getMessage();
            if (!eMsg.contains("Method") || !eMsg.contains("has illegal signature")) {
                throw new RuntimeException("Unexpected exception: " + eMsg);
            }
        }
        try {
            Class newClass = Class.forName("BadMethodSig");
            throw new RuntimeException("Expected ClasFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            String eMsg = e.getMessage();
            if (!eMsg.contains("Class name is empty or contains illegal character")) {
                throw new RuntimeException("Unexpected exception: " + eMsg);
            }
        }
    }
}
