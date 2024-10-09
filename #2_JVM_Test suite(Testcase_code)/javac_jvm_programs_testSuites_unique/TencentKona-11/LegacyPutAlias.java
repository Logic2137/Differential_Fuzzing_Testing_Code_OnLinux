

import static java.lang.System.out;

import java.security.Provider;



public class LegacyPutAlias {
    private static final String LEGACY_ALGO = "SRLegacy";
    private static final String MODERN_ALGO = "SRModern";
    private static final String LEGACY_ALIAS = "AliasLegacy";
    private static final String MODERN_ALIAS = "AliasModern";

    public static void main(String[] args) {
        checkAlias(LEGACY_ALGO, LEGACY_ALIAS);
        checkAlias(MODERN_ALGO, MODERN_ALIAS);
    }

    private static void checkAlias(String algo, String alias) {
        out.println("Checking alias " + alias + " for " + algo);
        Provider p = new CustomProvider();
        p.put("Alg.Alias.SecureRandom." + alias, algo);
        validate(p, algo, alias);
        out.println("=> Test Passed");
    }

    private static void validate(Provider p, String algo, String alias) {
        Provider.Service s = p.getService("SecureRandom", alias);
        if (s == null) {
            throw new RuntimeException("Failed alias " + alias + " check, " +
                    "exp: " + algo + ", got null");
        }
        if (!algo.equals(s.getAlgorithm())) {
            throw new RuntimeException("Failed alias " + alias + " check, " +
                    "exp: " + algo + ", got " + s.getAlgorithm());
        }
    }


    private static final String SR_IMPLCLASS =
            "sun.security.provider.SecureRandom";
    private static class CustomProvider extends Provider {
        private static class CustomService extends Provider.Service {
            CustomService(Provider p, String type, String algo, String cName) {
                super(p, type, algo, cName, null, null);
            }
        }

        CustomProvider() {
            super("CP", 1.0, "test provider that registers two services, " +
                    "one with put and one with putService");

            putService(new CustomService(this, "SecureRandom",
                    MODERN_ALGO, SR_IMPLCLASS));
            put("SecureRandom." + LEGACY_ALGO, SR_IMPLCLASS);
        }
    }
}

