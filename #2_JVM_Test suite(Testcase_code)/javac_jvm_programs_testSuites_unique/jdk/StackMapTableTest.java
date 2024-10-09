



public class StackMapTableTest {

    public static void main(String args[]) throws Throwable {
        System.out.println("Regression test for bug 8205422");
        try {
            Class newClass = Class.forName("badStackMapTable");
            throw new RuntimeException("Expected VerifyError exception not thrown");
        } catch (java.lang.VerifyError e) {
            if (!e.getMessage().contains("same_locals_1_stack_item_frame(@18,BAD:9)")) {
               throw new RuntimeException(
                   "Unexpected VerifyError message: " + e.getMessage());
            }
        }
    }
}
