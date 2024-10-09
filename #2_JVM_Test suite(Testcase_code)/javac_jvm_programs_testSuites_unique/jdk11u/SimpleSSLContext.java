

package jdk.testlibrary;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.*;
import javax.net.ssl.*;


public class SimpleSSLContext {

    SSLContext ssl;

    
    public SimpleSSLContext() throws IOException {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                @Override
                public Void run() throws Exception {
                    String paths = System.getProperty("test.src.path");
                    StringTokenizer st = new StringTokenizer(paths, File.pathSeparator);
                    boolean securityExceptions = false;
                    while (st.hasMoreTokens()) {
                        String path = st.nextToken();
                        try {
                            File f = new File(path, "jdk/testlibrary/testkeys");
                            if (f.exists()) {
                                try (FileInputStream fis = new FileInputStream(f)) {
                                    init(fis);
                                    return null;
                                }
                            }
                        } catch (SecurityException e) {
                            
                            
                            securityExceptions = true;
                        }
                    }
                    if (securityExceptions) {
                        System.err.println("SecurityExceptions thrown on loading testkeys");
                    }
                    return null;
                }
            });
        } catch (PrivilegedActionException pae) {
            Throwable t = pae.getCause() != null ? pae.getCause() : pae;
            if (t instanceof IOException)
                throw (IOException)t;
            if (t instanceof RuntimeException)
                throw (RuntimeException)t;
            if (t instanceof Error)
                throw (Error)t;
            throw new RuntimeException(t);
        }
    }

    
    public SimpleSSLContext(String dir) throws IOException {
        String file = dir+"/testkeys";
        try (FileInputStream fis = new FileInputStream(file)) {
            init(fis);
        }
    }

    private void init(InputStream i) throws IOException {
        try {
            char[] passphrase = "passphrase".toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(i, passphrase);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, passphrase);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            ssl = SSLContext.getInstance("TLS");
            ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e.getMessage());
        } catch (KeyStoreException e) {
            throw new RuntimeException(e.getMessage());
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e.getMessage());
        } catch (CertificateException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public SSLContext get() {
        return ssl;
    }
}
