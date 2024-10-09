



public class TestEmptyUnqName {
    public static void main(String args[]) throws Throwable {

        System.out.println("Regression test for bug 8225789");

        try {
            Class newClass = Class.forName("EmptyUnqName");
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            if (!e.getMessage().contains("Class name is empty or contains illegal character")) {
                throw new RuntimeException("Wrong ClassFormatError: " + e.getMessage());
            }
        }
    }
}
