





public class CondyCFVCheckTest {
    public static void main(String args[]) throws Throwable {
        try {
            Class newClass = Class.forName("CondyCFVCheck");
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            if (!e.getMessage().contains("Class file version does not support constant tag 17 in class file")) {
                throw new RuntimeException("ClassFormatError thrown, incorrect message");
            }
            System.out.println("Test CondyCFVCheckTest passed: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        }
    }
}
