








public class CondyBadBSMArrayTest {
    public static void main(String args[]) throws Throwable {
        
        try {
            Class newClass = Class.forName("CondyBadBSMIndex");
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            if (!e.getMessage().contains("Short length on BootstrapMethods in class file")) {
                throw new RuntimeException("ClassFormatError thrown, incorrect message");
            }
            System.out.println("Test CondyBadBSMIndex passed: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        }

        
        try {
            Class newClass = Class.forName("CondyEmptyBSMArray1");
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            if (!e.getMessage().contains("Short length on BootstrapMethods in class file")) {
                throw new RuntimeException("ClassFormatError thrown, incorrect message");
            }
            System.out.println("Test CondyEmptyBSMArray1 passed: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        }

        
        try {
            Class newClass = Class.forName("CondyNoBSMArray");
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            if (!e.getMessage().contains("Missing BootstrapMethods attribute in class file")) {
                throw new RuntimeException("ClassFormatError thrown, incorrect message");
            }
            System.out.println("Test CondyNoBSMArray passed: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        }
    }
}
