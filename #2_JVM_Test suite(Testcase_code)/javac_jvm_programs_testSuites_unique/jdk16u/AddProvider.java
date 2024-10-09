


import java.security.Provider;
import java.security.Security;

public class AddProvider {

    public static void main(String[] args) throws Exception {
        boolean legacy = args[0].equals("2");
        Security.addProvider(new TestProvider("Test1"));
        Security.insertProviderAt(new TestProvider("Test2"), 1);
        try {
            Security.addProvider(new TestProvider("Test3"));
            if (legacy) {
                throw new Exception("Expected SecurityException");
            }
        } catch (SecurityException se) {
            if (!legacy) {
                throw se;
            }
        }
    }

    private static class TestProvider extends Provider {
        TestProvider(String name) {
            super(name, "0.0", "Not for use in production systems!");
        }
    }
}
