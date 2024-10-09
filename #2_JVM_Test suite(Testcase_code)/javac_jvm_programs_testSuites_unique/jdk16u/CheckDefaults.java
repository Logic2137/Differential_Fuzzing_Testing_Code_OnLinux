
import java.security.KeyStore;
import java.security.Security;
import static java.lang.System.out;


public class CheckDefaults {
    private static final String DEFAULT_KEY_STORE_TYPE = "pkcs12";
    private static final String[] KEY_STORE_TYPES = {"jks", "pkcs12", "jceks",
        "Unregistered_type_of_KS"};

    private void runTest(String[] args) {
        if (!KeyStore.getDefaultType().
                equalsIgnoreCase(DEFAULT_KEY_STORE_TYPE)) {
            throw new RuntimeException(String.format("Default keystore type "
                    + "Expected '%s' . Actual: '%s' ", DEFAULT_KEY_STORE_TYPE,
                    KeyStore.getDefaultType()));
        }
        for (String ksDefaultType : KEY_STORE_TYPES) {
            Security.setProperty("keystore.type", ksDefaultType);
            if (!KeyStore.getDefaultType().equals(ksDefaultType)) {
                throw new RuntimeException(String.format(
                        "Keystore default type value: '%s' cannot be set up via"
                        + " keystore.type "
                        + "security property, Actual: '%s'",
                        ksDefaultType, KeyStore.getDefaultType()));
            }
        }
        out.println("Test Passed");
    }

    public static void main(String[] args) {
        CheckDefaults checkDefaultsTest = new CheckDefaults();
        checkDefaultsTest.runTest(args);
    }
}
