public class TestBadClassName {

    public static void main(String[] args) throws Throwable {
        System.out.println("Regression test for bug 8042660");
        try {
            Class newClass = Class.forName("UseBadInterface1");
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            System.out.println("Test UseBadInterface1 passed test case with illegal class name");
        }
        try {
            Class newClass = Class.forName("UseBadInterface2");
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            System.out.println("Test UseBadInterface1 passed test case with illegal class name");
        }
    }
}
