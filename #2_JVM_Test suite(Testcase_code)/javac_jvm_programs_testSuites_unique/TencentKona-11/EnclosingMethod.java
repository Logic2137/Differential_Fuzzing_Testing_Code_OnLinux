




public class EnclosingMethod {
    public static void main(String args[]) throws Throwable {

        System.out.println("Regression test for bug 8130183");
        try {
            Class newClass = Class.forName("badEnclMthd");
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            System.out.println("Test EnclosingMethod passed");
        }
    }
}
