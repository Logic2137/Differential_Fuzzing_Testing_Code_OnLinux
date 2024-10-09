
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

    private SecurityUtils() {
    }
}
