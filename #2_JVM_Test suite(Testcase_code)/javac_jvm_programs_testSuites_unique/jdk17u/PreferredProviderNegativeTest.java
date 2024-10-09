import java.security.Security;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class PreferredProviderNegativeTest {

    static final String SEC_PREF_PROP = "jdk.security.provider.preferred";

    public static void preJCESet(String value, boolean negativeProvider) throws NoSuchAlgorithmException, NoSuchPaddingException {
        Security.setProperty(SEC_PREF_PROP, value);
        if (!Security.getProperty(SEC_PREF_PROP).equals(value)) {
            throw new RuntimeException("Test Failed:The property wasn't set");
        }
        String[] arrays = value.split(":");
        Cipher cipher = Cipher.getInstance(arrays[0]);
        if (negativeProvider) {
            if (cipher.getProvider().getName().equals(arrays[1])) {
                throw new RuntimeException("Test Failed:The provider shouldn't be set.");
            }
        } else {
            if (!cipher.getProvider().getName().equals(arrays[1])) {
                throw new RuntimeException("Test Failed:The provider could be " + "set by valid provider.");
            }
        }
        System.out.println("Test Pass.");
    }

    public static void afterJCESet(String value, String expected) throws NoSuchAlgorithmException, NoSuchPaddingException {
        String[] arrays = value.split(":");
        Cipher cipher = Cipher.getInstance(arrays[0]);
        Security.setProperty(SEC_PREF_PROP, value);
        if (!cipher.getProvider().getName().equals(expected)) {
            throw new RuntimeException("Test Failed:The security property can't" + " be updated after JCE load.");
        }
        System.out.println("Test Pass");
    }

    public static void invalidAlg(String value) throws NoSuchPaddingException {
        String[] arrays = value.split(":");
        try {
            Security.setProperty(SEC_PREF_PROP, value);
            Cipher.getInstance(arrays[0]);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Test Pass:Got NoSuchAlgorithmException as expired");
            return;
        }
        throw new RuntimeException("Test Failed:Expected NoSuchAlgorithmException was not thrown");
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException {
        String expected;
        String value = args[1];
        expected = "SunJCE";
        if (args.length >= 2) {
            switch(args[0]) {
                case "preSet":
                    boolean negativeProvider = Boolean.valueOf(args[2]);
                    if (!args[1].contains(":")) {
                        value += ":" + expected;
                    }
                    PreferredProviderNegativeTest.preJCESet(value, negativeProvider);
                    break;
                case "afterSet":
                    PreferredProviderNegativeTest.afterJCESet(args[1], expected);
                    break;
                case "invalidAlg":
                    PreferredProviderNegativeTest.invalidAlg(args[1]);
                    break;
            }
        } else {
            throw new RuntimeException("Test Failed:Please pass the correct args");
        }
    }
}
