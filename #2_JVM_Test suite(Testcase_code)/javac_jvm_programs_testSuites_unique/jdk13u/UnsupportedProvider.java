import java.security.Provider;
import java.security.Security;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UnsupportedProvider {

    public static void main(String[] args) {
        String[] algorithms = { "SHA3-224", "SHA3-256", "SHA3-384", "SHA3-512" };
        for (Provider prov : Security.getProviders()) {
            for (String algo : algorithms) {
                try {
                    String provName = prov.getName();
                    MessageDigest md = MessageDigest.getInstance(algo, prov);
                    if (!isSHA3Supported(provName)) {
                        throw new RuntimeException("SHA-3 is not supported by " + provName + " provider, but expected " + "NoSuchAlgorithmException is not thrown");
                    }
                } catch (NoSuchAlgorithmException ex) {
                    if (isSHA3Supported(prov.getName())) {
                        throw new RuntimeException("SHA-3 should be supported " + "by " + prov.getName() + " provider, got" + " unexpected NoSuchAlgorithmException");
                    }
                    continue;
                }
            }
        }
    }

    static boolean isSHA3Supported(String provName) {
        if ("SUN".equals(provName)) {
            return true;
        }
        if ("OracleUcrypto".equals(provName) && "SunOS".equals(System.getProperty("os.name")) && System.getProperty("os.version").compareTo("5.12") >= 0) {
            return true;
        }
        return false;
    }
}
