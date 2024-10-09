import java.net.*;
import java.util.*;
import javax.net.ssl.*;

public class NoKerberos {

    static final List<String> KERBEROS_CIPHER_SUITES = Arrays.asList("TLS_KRB5_WITH_RC4_128_SHA", "TLS_KRB5_WITH_RC4_128_MD5", "TLS_KRB5_WITH_3DES_EDE_CBC_SHA", "TLS_KRB5_WITH_3DES_EDE_CBC_MD5", "TLS_KRB5_WITH_DES_CBC_SHA", "TLS_KRB5_WITH_DES_CBC_MD5", "TLS_KRB5_EXPORT_WITH_RC4_40_SHA", "TLS_KRB5_EXPORT_WITH_RC4_40_MD5", "TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA", "TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5");

    static void checkNotSupported(String[] supportedSuites) {
        for (String suites : supportedSuites) {
            if (KERBEROS_CIPHER_SUITES.contains(suites)) {
                throw new RuntimeException("Supported list of cipher suites " + " should not include Kerberos cipher suites");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            Class.forName("javax.security.auth.kerberos.KerberosPrincipal");
            System.out.println("Kerberos is present, nothing to test");
            return;
        } catch (ClassNotFoundException okay) {
        }
        try (Socket s = SSLSocketFactory.getDefault().createSocket()) {
            SSLSocket sslSocket = (SSLSocket) s;
            checkNotSupported(sslSocket.getSupportedCipherSuites());
            for (String kcs : KERBEROS_CIPHER_SUITES) {
                String[] suites = { kcs };
                try {
                    sslSocket.setEnabledCipherSuites(suites);
                    throw new RuntimeException("SSLSocket.setEnabledCipherSuitessuites allowed " + kcs + " but Kerberos not supported");
                } catch (IllegalArgumentException expected) {
                }
            }
        }
        try (ServerSocket ss = SSLServerSocketFactory.getDefault().createServerSocket()) {
            SSLServerSocket sslSocket = (SSLServerSocket) ss;
            checkNotSupported(sslSocket.getSupportedCipherSuites());
            for (String kcs : KERBEROS_CIPHER_SUITES) {
                String[] suites = { kcs };
                try {
                    sslSocket.setEnabledCipherSuites(suites);
                    throw new RuntimeException("SSLSocket.setEnabledCipherSuitessuites allowed " + kcs + " but Kerberos not supported");
                } catch (IllegalArgumentException expected) {
                }
            }
        }
    }
}
