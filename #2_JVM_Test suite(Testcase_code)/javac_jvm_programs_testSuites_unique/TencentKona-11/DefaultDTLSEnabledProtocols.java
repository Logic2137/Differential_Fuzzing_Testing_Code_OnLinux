






import java.security.Security;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

public class DefaultDTLSEnabledProtocols {
    static enum ContextVersion {
        TLS_CV_01("DTLS",
                new String[] {"DTLSv1.0", "DTLSv1.2"}),
        TLS_CV_02("DTLSv1.0",
                new String[] {"DTLSv1.0"}),
        TLS_CV_03("DTLSv1.2",
                new String[] {"DTLSv1.0", "DTLSv1.2"});

        final String contextVersion;
        final String[] enabledProtocols;
        final static String[] supportedProtocols = new String[] {
                "DTLSv1.0", "DTLSv1.2"};

        ContextVersion(String contextVersion, String[] enabledProtocols) {
            this.contextVersion = contextVersion;
            this.enabledProtocols = enabledProtocols;
        }
    }

    private static boolean checkProtocols(String[] target, String[] expected) {
        boolean success = true;
        if (target.length == 0) {
            System.out.println("\tError: No protocols");
            success = false;
        }

        if (!protocolEquals(target, expected)) {
            System.out.println("\tError: Expected to get protocols " +
                    Arrays.toString(expected));
            success = false;
        }
        System.out.println("\t  Protocols found " + Arrays.toString(target));

        return success;
    }

    private static boolean protocolEquals(
            String[] actualProtocols,
            String[] expectedProtocols) {
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

        boolean failed = false;
        for (ContextVersion cv : ContextVersion.values()) {
            System.out.println("Checking SSLContext of " + cv.contextVersion);
            SSLContext context = SSLContext.getInstance(cv.contextVersion);

            
            if (!cv.contextVersion.equals("Default")) {
                
                context.init((KeyManager[])null, (TrustManager[])null, null);
            }

            
            
            
            
            System.out.println("\tChecking default SSLParameters");
            SSLParameters parameters = context.getDefaultSSLParameters();

            String[] protocols = parameters.getProtocols();
            failed |= !checkProtocols(protocols, cv.enabledProtocols);

            String[] ciphers = parameters.getCipherSuites();
            failed |= !checkCipherSuites(ciphers);

            
            System.out.println("\tChecking supported SSLParameters");
            parameters = context.getSupportedSSLParameters();

            protocols = parameters.getProtocols();
            failed |= !checkProtocols(protocols, cv.supportedProtocols);

            ciphers = parameters.getCipherSuites();
            failed |= !checkCipherSuites(ciphers);

            
            
            
            
            System.out.println();
            System.out.println("\tChecking SSLEngine of this SSLContext");
            System.out.println("\tChecking SSLEngine.getSSLParameters()");
            SSLEngine engine = context.createSSLEngine();
            engine.setUseClientMode(true);
            parameters = engine.getSSLParameters();

            protocols = parameters.getProtocols();
            failed |= !checkProtocols(protocols, cv.enabledProtocols);

            ciphers = parameters.getCipherSuites();
            failed |= !checkCipherSuites(ciphers);

            System.out.println("\tChecking SSLEngine.getEnabledProtocols()");
            protocols = engine.getEnabledProtocols();
            failed |= !checkProtocols(protocols, cv.enabledProtocols);

            System.out.println("\tChecking SSLEngine.getEnabledCipherSuites()");
            ciphers = engine.getEnabledCipherSuites();
            failed |= !checkCipherSuites(ciphers);

            System.out.println("\tChecking SSLEngine.getSupportedProtocols()");
            protocols = engine.getSupportedProtocols();
            failed |= !checkProtocols(protocols, cv.supportedProtocols);

            System.out.println(
                    "\tChecking SSLEngine.getSupportedCipherSuites()");
            ciphers = engine.getSupportedCipherSuites();
            failed |= !checkCipherSuites(ciphers);

            
            
            
            
            System.out.println();
            System.out.println("\tChecking SSLSocket of this SSLContext");
            System.out.println("\tChecking SSLSocket.getSSLParameters()");
            SocketFactory fac = context.getSocketFactory();
            SSLSocket socket = (SSLSocket)fac.createSocket();
            parameters = socket.getSSLParameters();

            protocols = parameters.getProtocols();
            failed |= !checkProtocols(protocols, cv.enabledProtocols);

            ciphers = parameters.getCipherSuites();
            failed |= !checkCipherSuites(ciphers);

            System.out.println("\tChecking SSLEngine.getEnabledProtocols()");
            protocols = socket.getEnabledProtocols();
            failed |= !checkProtocols(protocols, cv.enabledProtocols);

            System.out.println("\tChecking SSLEngine.getEnabledCipherSuites()");
            ciphers = socket.getEnabledCipherSuites();
            failed |= !checkCipherSuites(ciphers);

            System.out.println("\tChecking SSLEngine.getSupportedProtocols()");
            protocols = socket.getSupportedProtocols();
            failed |= !checkProtocols(protocols, cv.supportedProtocols);

            System.out.println(
                    "\tChecking SSLEngine.getSupportedCipherSuites()");
            ciphers = socket.getSupportedCipherSuites();
            failed |= !checkCipherSuites(ciphers);

            
            
            
            
            System.out.println();
            System.out.println("\tChecking SSLServerSocket of this SSLContext");
            System.out.println("\tChecking SSLServerSocket.getSSLParameters()");
            SSLServerSocketFactory sf = context.getServerSocketFactory();
            SSLServerSocket ssocket = (SSLServerSocket)sf.createServerSocket();
            parameters = ssocket.getSSLParameters();

            protocols = parameters.getProtocols();
            failed |= !checkProtocols(protocols, cv.supportedProtocols);

            ciphers = parameters.getCipherSuites();
            failed |= !checkCipherSuites(ciphers);

            System.out.println("\tChecking SSLEngine.getEnabledProtocols()");
            protocols = ssocket.getEnabledProtocols();
            failed |= !checkProtocols(protocols, cv.supportedProtocols);

            System.out.println("\tChecking SSLEngine.getEnabledCipherSuites()");
            ciphers = ssocket.getEnabledCipherSuites();
            failed |= !checkCipherSuites(ciphers);

            System.out.println("\tChecking SSLEngine.getSupportedProtocols()");
            protocols = ssocket.getSupportedProtocols();
            failed |= !checkProtocols(protocols, cv.supportedProtocols);

            System.out.println(
                    "\tChecking SSLEngine.getSupportedCipherSuites()");
            ciphers = ssocket.getSupportedCipherSuites();
            failed |= !checkCipherSuites(ciphers);
        }

        if (failed) {
            throw new Exception("Run into problems, see log for more details");
        } else {
            System.out.println("\t... Success");
        }
    }
}
