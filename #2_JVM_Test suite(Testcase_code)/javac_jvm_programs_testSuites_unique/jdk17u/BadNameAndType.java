public class BadNameAndType {

    public static void main(String[] args) throws Throwable {
        System.out.println("Regression test for bug 8042660");
        try {
            Class newClass = Class.forName("emptySigUtf8");
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            System.out.println("Test BadNameAndType passed test case emptySigUtf8");
        }
        try {
            Class newClass = Class.forName("emptyNameUtf8");
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            System.out.println("Test BadNameAndType passed test case emptyNameUtf8");
        }
    }
}
