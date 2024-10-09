public class CondyBadNameTypeTest {

    public static void main(String[] args) throws Throwable {
        try {
            Class newClass = Class.forName("CondyBadNameType");
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            if (!e.getMessage().contains("Invalid constant pool index")) {
                throw new RuntimeException("ClassFormatError thrown, incorrect message");
            }
            System.out.println("Test CondyBadNameTypeTest passed: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        }
    }
}
