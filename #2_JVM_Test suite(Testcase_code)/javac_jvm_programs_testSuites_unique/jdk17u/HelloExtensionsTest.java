import javax.crypto.*;
import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.*;
import java.io.*;
import java.nio.*;
import java.security.*;

public class HelloExtensionsTest {

    private static boolean debug = false;

    private static boolean proceed = true;

    private static boolean EcAvailable = isEcAvailable();

    static String pathToStores = "../../../../javax/net/ssl/etc";

    private static String keyStoreFile = "keystore";

    private static String trustStoreFile = "truststore";

    private static String passwd = "passphrase";

    private static String keyFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + keyStoreFile;

    private static String trustFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + trustStoreFile;

    private static void checkDone(SSLEngine ssle) throws Exception {
        if (!ssle.isInboundDone()) {
            throw new Exception("isInboundDone isn't done");
        }
        if (!ssle.isOutboundDone()) {
            throw new Exception("isOutboundDone isn't done");
        }
    }

    private static void runTest(SSLEngine ssle) throws Exception {
        String hello = "16030300df010000db03035898b7826c8c0cc" + "a02d50aec8fdf3aa2e49bef0362e8592974780d25699961f" + "100003ac023c027003cc025c02900670040c009c013002fc" + "004c00e00330032c02bc02f009cc02dc031009e00a2c008c" + "012000ac003c00d0016001300ff01000078000a003400320" + "0170001000300130015000600070009000a0018000b000c0" + "019000d000e000f001000110002001200040005001400080" + "016000b00020100000d00180016060306010503050104030" + "401030303010203020102020000001a00180000156275677" + "32e6f70656e6a646b2e6a6176612e6e6574";
        byte[] msg_clihello = hexStringToByteArray(hello);
        ByteBuffer bf_clihello = ByteBuffer.wrap(msg_clihello);
        SSLSession session = ssle.getSession();
        int appBufferMax = session.getApplicationBufferSize();
        int netBufferMax = session.getPacketBufferSize();
        ByteBuffer serverIn = ByteBuffer.allocate(appBufferMax + 50);
        ByteBuffer serverOut = ByteBuffer.wrap("I'm Server".getBytes());
        ByteBuffer sTOc = ByteBuffer.allocate(netBufferMax);
        ssle.beginHandshake();
        SSLEngineResult result = ssle.unwrap(bf_clihello, serverIn);
        System.out.println("server unwrap " + result);
        runDelegatedTasks(result, ssle);
        if (!proceed) {
            return;
        }
        SSLEngineResult.HandshakeStatus status = ssle.getHandshakeStatus();
        if (status == HandshakeStatus.NEED_UNWRAP) {
            result = ssle.unwrap(bf_clihello, serverIn);
            System.out.println("server unwrap " + result);
            runDelegatedTasks(result, ssle);
        } else if (status == HandshakeStatus.NEED_WRAP) {
            result = ssle.wrap(serverOut, sTOc);
            System.out.println("server wrap " + result);
            runDelegatedTasks(result, ssle);
        } else {
            throw new Exception("unexpected handshake status " + status);
        }
    }

    private static void runDelegatedTasks(SSLEngineResult result, SSLEngine engine) throws Exception {
        if (result.getHandshakeStatus() == HandshakeStatus.NEED_TASK) {
            Runnable runnable;
            try {
                while ((runnable = engine.getDelegatedTask()) != null) {
                    log("\trunning delegated task...");
                    runnable.run();
                }
            } catch (ExceptionInInitializerError e) {
                String v = System.getProperty("jdk.tls.namedGroups");
                if (!EcAvailable || v == null) {
                    throw new RuntimeException("Unexpected Error :" + e);
                }
                if (v != null && v.contains("bug")) {
                    log("got expected error for bad jdk.tls.namedGroups");
                    proceed = false;
                    return;
                } else {
                    System.out.println("Unexpected error. " + "jdk.tls.namedGroups value: " + v);
                    throw e;
                }
            }
            HandshakeStatus hsStatus = engine.getHandshakeStatus();
            if (hsStatus == HandshakeStatus.NEED_TASK) {
                throw new Exception("handshake shouldn't need additional tasks");
            }
            log("\tnew HandshakeStatus: " + hsStatus);
        }
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static boolean isEcAvailable() {
        try {
            Signature.getInstance("SHA1withECDSA");
            Signature.getInstance("NONEwithECDSA");
            KeyAgreement.getInstance("ECDH");
            KeyFactory.getInstance("EC");
            KeyPairGenerator.getInstance("EC");
            AlgorithmParameters.getInstance("EC");
        } catch (Exception e) {
            log("EC not available. Received: " + e);
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        SSLEngine ssle = createSSLEngine(keyFilename, trustFilename);
        runTest(ssle);
        System.out.println("Test Passed.");
    }

    static private SSLEngine createSSLEngine(String keyFile, String trustFile) throws Exception {
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
        ssle = sslCtx.createSSLEngine();
        ssle.setUseClientMode(false);
        return ssle;
    }

    private static void log(String str) {
        if (debug) {
            System.out.println(str);
        }
    }
}
