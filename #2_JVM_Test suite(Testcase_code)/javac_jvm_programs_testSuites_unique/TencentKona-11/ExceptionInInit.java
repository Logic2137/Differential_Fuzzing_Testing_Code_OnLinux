



import java.security.AccessController;
import java.security.PrivilegedAction;

public class ExceptionInInit {

    public static void main(String[] args) {

        Test test = null;

        try {
            System.setSecurityManager(new java.rmi.RMISecurityManager());
            Test.showTest();
        } catch (ExceptionInInitializerError e) {
        }
    }

    public static class FooBar {
        static String test = "test";
        FooBar(String test) {
            this.test = test;
        }
    }

    public static class Test extends FooBar {

        
        private static String test =
            AccessController.doPrivileged((PrivilegedAction<String>)() -> System.getProperty("test.src", "."));

        Test(String test) {
            super(test);
        }
        public static void showTest() {
            System.err.println(test);
        }
    }
}
