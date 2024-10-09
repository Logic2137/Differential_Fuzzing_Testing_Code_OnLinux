public class AnnotationTag {

    public static void main(String[] args) throws Throwable {
        System.out.println("Regression test for bug 8042041");
        try {
            Class newClass = Class.forName("badAnnotTag");
        } catch (java.lang.Throwable e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage());
        }
    }
}
