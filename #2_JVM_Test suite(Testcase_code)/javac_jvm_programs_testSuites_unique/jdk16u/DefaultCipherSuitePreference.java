






import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

public class DefaultCipherSuitePreference {
    private static final String[] contextAlgorithms = {
            "Default", "SSL", "TLS", "SSLv3", "TLSv1",
            "TLSv1.1", "TLSv1.2", "TLSv1.3"
        };

    public static void main(String[] args) throws Exception {
        for (String algorithm : contextAlgorithms) {
            System.out.println("Checking SSLContext of " + algorithm);
            SSLContext sslContext = SSLContext.getInstance(algorithm);

            
            if (!algorithm.equals("Default")) {
                
                sslContext.init((KeyManager[])null, (TrustManager[])null, null);
            }

            
            
            
            
            checkDefaultCipherSuitePreference(
                    sslContext.getDefaultSSLParameters(),
                    "SSLContext.getDefaultSSLParameters()");

            
            checkDefaultCipherSuitePreference(
                    sslContext.getSupportedSSLParameters(),
                    "SSLContext.getSupportedSSLParameters()");

            
            
            
            
            SSLEngine engine = sslContext.createSSLEngine();
            engine.setUseClientMode(true);
            checkDefaultCipherSuitePreference(
                    engine.getSSLParameters(),
                    "client mode SSLEngine.getSSLParameters()");

            engine.setUseClientMode(false);
            checkDefaultCipherSuitePreference(
                    engine.getSSLParameters(),
                    "server mode SSLEngine.getSSLParameters()");

            
            
            
            
            SocketFactory fac = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket)fac.createSocket();
            checkDefaultCipherSuitePreference(
                    socket.getSSLParameters(),
                    "SSLSocket.getSSLParameters()");

            
            
            
            
            SSLServerSocketFactory sf = sslContext.getServerSocketFactory();
            SSLServerSocket ssocket = (SSLServerSocket)sf.createServerSocket();
            checkDefaultCipherSuitePreference(
                    ssocket.getSSLParameters(),
                    "SSLServerSocket.getSSLParameters()");
        }
    }

    private static void checkDefaultCipherSuitePreference(
            SSLParameters parameters, String context) throws Exception {
        if (!parameters.getUseCipherSuitesOrder()) {
            throw new Exception(
                    "The local cipher suite preference is not honored " +
                    "in the connection populated SSLParameters object (" +
                    context + ")");
        }
    }
}
