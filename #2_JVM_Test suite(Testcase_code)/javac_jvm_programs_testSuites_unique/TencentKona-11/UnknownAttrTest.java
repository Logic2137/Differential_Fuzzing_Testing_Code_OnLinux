




public class UnknownAttrTest {
    public static void main(String args[]) throws Throwable {

        System.out.println("Regression test for bug 8207944");
        try {
            Class newClass = Class.forName("UnknownAttr");
        } catch (java.lang.Throwable e) {
            throw new RuntimeException(
                "Unexpected exception: " + e.getMessage());
        }
    }
}
