
package jdk.internal.net.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.StringTokenizer;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class SimpleSSLContext {

    private final SSLContext ssl;

    public SimpleSSLContext() throws IOException {
        String paths = System.getProperty("test.src.path");
        StringTokenizer st = new StringTokenizer(paths, File.pathSeparator);
        boolean securityExceptions = false;
        SSLContext sslContext = null;
        while (st.hasMoreTokens()) {
            String path = st.nextToken();
            try {
                File f = new File(path, "../../../../../lib/jdk/test/lib/net/testkeys");
                if (f.exists()) {
                    try (FileInputStream fis = new FileInputStream(f)) {
                        sslContext = init(fis);
                        break;
                    }
                }
            } catch (SecurityException e) {
                securityExceptions = true;
            }
        }
        if (securityExceptions) {
            System.out.println("SecurityExceptions thrown on loading testkeys");
        }
        ssl = sslContext;
    }

    private SSLContext init(InputStream i) throws IOException {
        try {
            char[] passphrase = "passphrase".toCharArray();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(i, passphrase);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
            kmf.init(ks, passphrase);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
            tmf.init(ks);
            SSLContext ssl = SSLContext.getInstance("TLS");
            ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            return ssl;
        } catch (KeyManagementException | KeyStoreException | UnrecoverableKeyException | CertificateException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public SSLContext get() {
        return ssl;
    }
}
