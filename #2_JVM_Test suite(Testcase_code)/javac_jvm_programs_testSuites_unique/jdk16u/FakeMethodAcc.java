




public class FakeMethodAcc {
    public static void main(String args[]) throws Throwable {

        System.out.println("Regression test for bug 8166304");
        try {
            Class newClass = Class.forName("fakeMethodAccessor");
            throw new RuntimeException(
                "Missing expected IllegalAccessError exception");
        } catch (java.lang.IllegalAccessError e) {
        }
    }
}
