import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

public class CacertsLimit {

    public static void main(String[] args) throws Exception {
        for (String algorithm : new String[] { "SunX509", "PKIX" }) {
            CacertsLimit.ensureLimit(algorithm);
        }
    }

    private static void ensureLimit(String algorithm) throws Exception {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
        tmf.init((KeyStore) null);
        TrustManager[] tms = tmf.getTrustManagers();
        if (tms == null || tms.length == 0) {
            throw new Exception("No default key store used for trust manager");
        }
        if (!(tms[0] instanceof X509TrustManager)) {
            throw new Exception("The trust manger is not an instance of X509TrustManager");
        }
        checkLimit(((X509TrustManager) tms[0]).getAcceptedIssuers());
    }

    private static void checkLimit(X509Certificate[] trustedCerts) throws Exception {
        int sizeAccount = 0;
        for (X509Certificate cert : trustedCerts) {
            X500Principal x500Principal = cert.getSubjectX500Principal();
            byte[] encodedPrincipal = x500Principal.getEncoded();
            sizeAccount += encodedPrincipal.length;
            if (sizeAccount > 0xFFFF) {
                throw new Exception("There are too many trusted CAs in cacerts. The " + "certificate_authorities extension cannot be used " + "for TLS connections.  Please rethink about the size" + "of the cacerts, or have a release note for the " + "impacted behaviors");
            } else if (sizeAccount > 0x4000) {
                throw new Exception("There are too many trusted CAs in cacerts. The " + "certificate_authorities extension cannot be " + "packaged in one TLS record, which would result in " + "interoperability issues.  Please rethink about the " + "size of the cacerts, or have a release note for " + "the impacted behaviors");
            }
        }
    }
}
