












import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.*;
import java.io.*;
import java.security.*;
import java.nio.*;

public class CloseStart {

    private static boolean debug = false;

    private static String pathToStores = "../../../../javax/net/ssl/etc";
    private static String keyStoreFile = "keystore";
    private static String trustStoreFile = "truststore";
    private static String passwd = "passphrase";

    private static String keyFilename =
            System.getProperty("test.src", "./") + "/" + pathToStores +
                "/" + keyStoreFile;
    private static String trustFilename =
            System.getProperty("test.src", "./") + "/" + pathToStores +
                "/" + trustStoreFile;

    private static void checkDone(SSLEngine ssle) throws Exception {
        if (!ssle.isInboundDone()) {
            throw new Exception("isInboundDone isn't done");
        }
        if (!ssle.isOutboundDone()) {
            throw new Exception("isOutboundDone isn't done");
        }
    }

    private static void runTest2(SSLEngine ssle) throws Exception {
        ssle.closeOutbound();
        checkDone(ssle);
    }

    public static void main(String args[]) throws Exception {

        SSLEngine ssle = createSSLEngine(keyFilename, trustFilename);
        ssle.closeInbound();
        if (!ssle.isInboundDone()) {
            throw new Exception("isInboundDone isn't done");
        }

        ssle = createSSLEngine(keyFilename, trustFilename);
        ssle.closeOutbound();
        if (!ssle.isOutboundDone()) {
            throw new Exception("isOutboundDone isn't done");
        }

        System.out.println("Test Passed.");
    }

    
    static private SSLEngine createSSLEngine(String keyFile, String trustFile)
            throws Exception {

        SSLEngine ssle;

        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore ts = KeyStore.getInstance("JKS");

        char[] passphrase = "passphrase".toCharArray();

        ks.load(new FileInputStream(keyFile), passphrase);
        ts.load(new FileInputStream(trustFile), passphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);

        SSLContext sslCtx = SSLContext.getInstance("TLS");

        sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        ssle = sslCtx.createSSLEngine("client", 1001);
        ssle.setUseClientMode(true);

        return ssle;
    }
}
