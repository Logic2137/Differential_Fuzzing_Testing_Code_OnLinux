import static java.lang.System.out;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class DefaultProvider {

    private static final String OS_NAME = System.getProperty("os.name");

    private static final String SUNOS = "SunOS";

    private static final String WINDOWS = "Windows";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        out.println("Operating System: " + OS_NAME);
        out.println("TEST: Default provider with constructor");
        SecureRandom secureRandom = new SecureRandom();
        String provider = secureRandom.getProvider().getName();
        if (!provider.equals("SUN")) {
            throw new RuntimeException("Unexpected provider name: " + provider);
        }
        out.println("Passed, default provider with constructor: " + provider);
        out.println("TEST: SHA1PRNG supported on all platforms by SUN provider");
        String algorithm = "SHA1PRNG";
        provider = "SUN";
        SecureRandom instance = SecureRandom.getInstance(algorithm);
        assertInstance(instance, algorithm, provider);
        out.println("Passed.");
        if (!OS_NAME.startsWith(WINDOWS)) {
            out.println("TEST: NativePRNG supported on all platforms" + "(except Windows), by SUN provider");
            algorithm = "NativePRNG";
            provider = "SUN";
        } else {
            out.println("TEST: Windows-PRNG supported on windows by SunMSCAPI provider");
            algorithm = "Windows-PRNG";
            provider = "SunMSCAPI";
        }
        instance = SecureRandom.getInstance(algorithm);
        assertInstance(instance, algorithm, provider);
        out.println("Passed.");
    }

    private static void assertInstance(SecureRandom instance, String expectedAlgorithm, String expectedProvider) {
        if (instance != null) {
            if (!expectedAlgorithm.equalsIgnoreCase(instance.getAlgorithm())) {
                throw new RuntimeException("Expected algorithm:" + expectedAlgorithm + " actual: " + instance.getAlgorithm());
            }
            if (!expectedProvider.equalsIgnoreCase(instance.getProvider().getName())) {
                throw new RuntimeException("Expected provider: " + expectedProvider + " actual: " + instance.getProvider().getName());
            }
        } else {
            throw new RuntimeException("Secure instance is not created");
        }
    }
}
