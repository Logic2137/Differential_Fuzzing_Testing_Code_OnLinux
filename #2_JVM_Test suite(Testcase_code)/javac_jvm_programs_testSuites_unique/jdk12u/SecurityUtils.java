

package jdk.test.lib.security;

import java.io.File;
import java.security.KeyStore;


public final class SecurityUtils {

    private static String getCacerts() {
        String sep = File.separator;
        return System.getProperty("java.home") + sep
                + "lib" + sep + "security" + sep + "cacerts";
    }

    
    public static KeyStore getCacertsKeyStore() throws Exception {
        File file = new File(getCacerts());
        if (!file.exists()) {
            return null;
        }
        return KeyStore.getInstance(file, (char[])null);
    }

    private SecurityUtils() {}
}
