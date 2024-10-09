import java.lang.UnsupportedOperationException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class CustomizedDTLSServerDefaultProtocols {

    final static String[] supportedProtocols = new String[] { "DTLSv1.0", "DTLSv1.2" };

    enum ContextVersion {

        TLS_CV_01("DTLS", new String[] { "DTLSv1.0" }, supportedProtocols), TLS_CV_02("DTLSv1.0", supportedProtocols, new String[] { "DTLSv1.0" }), TLS_CV_03("DTLS1.2", supportedProtocols, supportedProtocols);

        final String contextVersion;

        final String[] serverEnabledProtocols;

        final String[] clientEnabledProtocols;

        ContextVersion(String contextVersion, String[] serverEnabledProtocols, String[] clientEnabledProtocols) {
            this.contextVersion = contextVersion;
            this.serverEnabledProtocols = serverEnabledProtocols;
            this.clientEnabledProtocols = clientEnabledProtocols;
        }
    }

    private static boolean checkProtocols(String[] target, String[] expected) {
        boolean success = true;
        if (target.length == 0) {
            System.out.println("\tError: No protocols");
            success = false;
        }
        if (!protocolEquals(target, expected)) {
            System.out.println("\tError: Expected to get protocols " + Arrays.toString(expected));
            success = false;
        }
        System.out.println("\t  Protocols found " + Arrays.toString(target));
        return success;
    }

    private static boolean protocolEquals(String[] actualProtocols, String[] expectedProtocols) {
        if (actualProtocols.length != expectedProtocols.length) {
            return false;
        }
        Set<String> set = new HashSet<>(Arrays.asList(expectedProtocols));
        for (String actual : actualProtocols) {
            if (set.add(actual)) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkCipherSuites(String[] target) {
        boolean success = true;
        if (target.length == 0) {
            System.out.println("\tError: No cipher suites");
            success = false;
        }
        return success;
    }

    public static void main(String[] args) throws Exception {
        Security.setProperty("jdk.tls.disabledAlgorithms", "");
        System.out.println("jdk.tls.client.protocols = " + System.getProperty("jdk.tls.client.protocols"));
        System.out.println("jdk.tls.server.protocols = " + System.getProperty("jdk.tls.server.protocols"));
        Test();
    }

    static void Test() throws Exception {
        boolean failed = false;
        SSLContext context;
        for (ContextVersion cv : ContextVersion.values()) {
            System.out.println("Checking SSLContext of " + cv.contextVersion);
            try {
                context = SSLContext.getInstance(cv.contextVersion);
            } catch (NoSuchAlgorithmException e) {
                if (cv.contextVersion.compareToIgnoreCase("DTLS1.2") == 0) {
                    System.out.println("Exception expected: " + e.getMessage());
                    continue;
                }
                throw e;
            }
            if (!cv.contextVersion.equals("Default")) {
                context.init(null, null, null);
            }
            System.out.println("\tChecking default SSLParameters");
            SSLParameters parameters = context.getDefaultSSLParameters();
            String[] protocols = parameters.getProtocols();
            failed |= !checkProtocols(protocols, cv.clientEnabledProtocols);
            String[] ciphers = parameters.getCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println("\tChecking supported SSLParameters");
            parameters = context.getSupportedSSLParameters();
            protocols = parameters.getProtocols();
            failed |= !checkProtocols(protocols, supportedProtocols);
            ciphers = parameters.getCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println();
            System.out.println("\tChecking SSLEngine of this SSLContext");
            System.out.println("\tChecking SSLEngine.getSSLParameters()");
            SSLEngine engine = context.createSSLEngine();
            engine.setUseClientMode(true);
            parameters = engine.getSSLParameters();
            protocols = parameters.getProtocols();
            failed |= !checkProtocols(protocols, cv.clientEnabledProtocols);
            ciphers = parameters.getCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println("\tChecking SSLEngine.getEnabledProtocols()");
            protocols = engine.getEnabledProtocols();
            failed |= !checkProtocols(protocols, cv.clientEnabledProtocols);
            System.out.println("\tChecking SSLEngine.getEnabledCipherSuites()");
            ciphers = engine.getEnabledCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println("\tChecking SSLEngine.getSupportedProtocols()");
            protocols = engine.getSupportedProtocols();
            failed |= !checkProtocols(protocols, supportedProtocols);
            System.out.println("\tChecking SSLEngine.getSupportedCipherSuites()");
            ciphers = engine.getSupportedCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println();
            System.out.println("\tChecking SSLSocket of this SSLContext");
            try {
                context.getSocketFactory();
                failed = true;
                System.out.println("SSLSocket returned a socket for DTLS");
            } catch (UnsupportedOperationException e) {
                System.out.println("\t  " + e.getMessage());
            }
            System.out.println();
            System.out.println("\tChecking SSLServerSocket of this SSLContext");
            try {
                context.getServerSocketFactory();
                failed = true;
                System.out.println("SSLServerSocket returned a socket for DTLS");
            } catch (UnsupportedOperationException e) {
                System.out.println("\t  " + e.getMessage());
            }
            if (failed) {
                throw new Exception("Run into problems, see log for more details");
            } else {
                System.out.println("\t... Success");
            }
        }
    }
}
