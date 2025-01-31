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

public class CustomizedServerDefaultProtocols {

    final static String[] supportedProtocols = new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3" };

    final static String[] serverDefaultProtocols = new String[] { "TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3" };

    enum ContextVersion {

        TLS_CV_01("SSL", new String[] { "SSLv3", "TLSv1", "TLSv1.1" }, new String[] { "TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3" }),
        TLS_CV_02("TLS", new String[] { "SSLv3", "TLSv1", "TLSv1.1" }, new String[] { "TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3" }),
        TLS_CV_03("SSLv3", serverDefaultProtocols, new String[] { "TLSv1" }),
        TLS_CV_04("TLSv1", serverDefaultProtocols, new String[] { "TLSv1" }),
        TLS_CV_05("TLSv1.1", serverDefaultProtocols, new String[] { "TLSv1", "TLSv1.1" }),
        TLS_CV_06("TLSv1.2", serverDefaultProtocols, new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" }),
        TLS_CV_07("TLSv1.3", serverDefaultProtocols, new String[] { "TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3" }),
        TLS_CV_08("Default", new String[] { "SSLv3", "TLSv1", "TLSv1.1" }, new String[] { "TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3" });

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
            System.out.println("\t\t\t*** Error: No protocols");
            success = false;
        }
        if (!protocolEquals(target, expected)) {
            System.out.println("\t\t\t*** Error: Expected to get protocols " + Arrays.toString(expected));
            success = false;
        }
        System.out.println("\t\t\t  Protocols found " + Arrays.toString(target));
        System.out.println("\t\t\t--> Protocol check passed!!");
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
            System.out.println("\t\t\t*** Error: No cipher suites");
            success = false;
        }
        System.out.println("\t\t\t--> Cipher check passed!!");
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
        for (ContextVersion cv : ContextVersion.values()) {
            System.out.println("\n\nChecking SSLContext of " + cv.contextVersion);
            System.out.println("============================");
            SSLContext context = SSLContext.getInstance(cv.contextVersion);
            if (!cv.contextVersion.equals("Default")) {
                context.init(null, null, null);
            }
            System.out.println("\tChecking default SSLParameters");
            System.out.println("\t\tChecking SSLContext.getDefaultSSLParameters().getProtocols");
            SSLParameters parameters = context.getDefaultSSLParameters();
            String[] protocols = parameters.getProtocols();
            failed |= !checkProtocols(protocols, cv.clientEnabledProtocols);
            String[] ciphers = parameters.getCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println("\t\tChecking supported SSLParameters");
            parameters = context.getSupportedSSLParameters();
            protocols = parameters.getProtocols();
            failed |= !checkProtocols(protocols, supportedProtocols);
            ciphers = parameters.getCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println();
            System.out.println("\tChecking SSLEngine of this SSLContext");
            System.out.println("\t\tChecking SSLEngine.getSSLParameters()");
            SSLEngine engine = context.createSSLEngine();
            engine.setUseClientMode(true);
            parameters = engine.getSSLParameters();
            protocols = parameters.getProtocols();
            failed |= !checkProtocols(protocols, cv.clientEnabledProtocols);
            ciphers = parameters.getCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println("\t\tChecking SSLEngine.getEnabledProtocols()");
            protocols = engine.getEnabledProtocols();
            failed |= !checkProtocols(protocols, cv.clientEnabledProtocols);
            System.out.println("\t\tChecking SSLEngine.getEnabledCipherSuites()");
            ciphers = engine.getEnabledCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println("\t\tChecking SSLEngine.getSupportedProtocols()");
            protocols = engine.getSupportedProtocols();
            failed |= !checkProtocols(protocols, supportedProtocols);
            System.out.println("\t\tChecking SSLEngine.getSupportedCipherSuites()");
            ciphers = engine.getSupportedCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println();
            System.out.println("\tChecking SSLSocket of this SSLContext");
            System.out.println("\t\tChecking SSLSocket.getSSLParameters()");
            SocketFactory fac = context.getSocketFactory();
            SSLSocket socket = (SSLSocket) fac.createSocket();
            parameters = socket.getSSLParameters();
            protocols = parameters.getProtocols();
            failed |= !checkProtocols(protocols, cv.clientEnabledProtocols);
            ciphers = parameters.getCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println("\t\tChecking SSLSocket.getEnabledProtocols()");
            protocols = socket.getEnabledProtocols();
            failed |= !checkProtocols(protocols, cv.clientEnabledProtocols);
            System.out.println("\t\tChecking SSLSocket.getEnabledCipherSuites()");
            ciphers = socket.getEnabledCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println("\t\tChecking SSLSocket.getSupportedProtocols()");
            protocols = socket.getSupportedProtocols();
            failed |= !checkProtocols(protocols, supportedProtocols);
            System.out.println("\t\tChecking SSLSocket.getSupportedCipherSuites()");
            ciphers = socket.getSupportedCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println();
            System.out.println("\tChecking SSLServerSocket of this SSLContext");
            System.out.println("\t\tChecking SSLServerSocket.getSSLParameters()");
            SSLServerSocketFactory sf = context.getServerSocketFactory();
            SSLServerSocket ssocket = (SSLServerSocket) sf.createServerSocket();
            parameters = ssocket.getSSLParameters();
            protocols = parameters.getProtocols();
            failed |= !checkProtocols(protocols, cv.serverEnabledProtocols);
            ciphers = parameters.getCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println("\t\tChecking SSLEngine.getEnabledProtocols()");
            protocols = ssocket.getEnabledProtocols();
            failed |= !checkProtocols(protocols, cv.serverEnabledProtocols);
            System.out.println("\t\tChecking SSLEngine.getEnabledCipherSuites()");
            ciphers = ssocket.getEnabledCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            System.out.println("\t\tChecking SSLEngine.getSupportedProtocols()");
            protocols = ssocket.getSupportedProtocols();
            failed |= !checkProtocols(protocols, supportedProtocols);
            System.out.println("\t\tChecking SSLEngine.getSupportedCipherSuites()");
            ciphers = ssocket.getSupportedCipherSuites();
            failed |= !checkCipherSuites(ciphers);
            if (failed) {
                throw new Exception("Run into problems, see log for more details");
            }
        }
    }
}
