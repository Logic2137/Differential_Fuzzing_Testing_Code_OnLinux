
package jdk.test.lib.security;

import java.io.File;
import java.security.KeyStore;
import java.security.Security;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class SecurityUtils {

    private static String getCacerts() {
        String sep = File.separator;
        return System.getProperty("java.home") + sep + "lib" + sep + "security" + sep + "cacerts";
    }

    public static KeyStore getCacertsKeyStore() throws Exception {
        File file = new File(getCacerts());
        if (!file.exists()) {
            return null;
        }
        return KeyStore.getInstance(file, (char[]) null);
    }

    public static void removeFromDisabledTlsAlgs(String... protocols) {
        removeFromDisabledAlgs("jdk.tls.disabledAlgorithms", List.<String>of(protocols));
    }

    private static void removeFromDisabledAlgs(String prop, List<String> algs) {
        String value = Security.getProperty(prop);
        value = Arrays.stream(value.split(",")).map(s -> s.trim()).filter(s -> !algs.contains(s)).collect(Collectors.joining(","));
        Security.setProperty(prop, value);
    }

    public static void removeAlgsFromDSigPolicy(String... algs) {
        removeFromDSigPolicy("disallowAlg", List.<String>of(algs));
    }

    private static void removeFromDSigPolicy(String rule, List<String> algs) {
        String value = Security.getProperty("jdk.xml.dsig.secureValidationPolicy");
        value = Arrays.stream(value.split(",")).filter(v -> !v.contains(rule) || !anyMatch(v, algs)).collect(Collectors.joining(","));
        Security.setProperty("jdk.xml.dsig.secureValidationPolicy", value);
    }

    private static boolean anyMatch(String value, List<String> algs) {
        for (String alg : algs) {
            if (value.contains(alg)) {
                return true;
            }
        }
        return false;
    }

    private SecurityUtils() {
    }
}
