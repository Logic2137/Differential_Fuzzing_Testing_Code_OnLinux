import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.*;
import java.io.*;
import java.security.*;
import java.nio.*;
import java.util.Random;

public class LargeBufs {

    private static boolean debug = true;

    private SSLContext sslc;

    static private SSLEngine ssle1;

    static private SSLEngine ssle2;

    private static String pathToStores = "../etc";

    private static String keyStoreFile = "keystore";

    private static String trustStoreFile = "truststore";

    private static String passwd = "passphrase";

    private static String keyFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + keyStoreFile;

    private static String trustFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + trustStoreFile;

    private ByteBuffer appOut1;

    private ByteBuffer appIn1;

    private ByteBuffer appOut2;

    private ByteBuffer appIn2;

    private ByteBuffer oneToTwo;

    private ByteBuffer twoToOne;

    private int appBufferMax;

    private int netBufferMax;

    private int OFFSET = 37;

    private void createSSLEngines() throws Exception {
        ssle1 = sslc.createSSLEngine("client", 1);
        ssle1.setUseClientMode(true);
        ssle2 = sslc.createSSLEngine();
        ssle2.setUseClientMode(false);
        ssle2.setNeedClientAuth(true);
    }

    private void runTest(String cipher) throws Exception {
        boolean dataDone = false;
        createSSLEngines();
        System.out.println("Using " + cipher);
        ssle1.setEnabledCipherSuites(new String[] { cipher });
        ssle2.setEnabledCipherSuites(new String[] { cipher });
        createBuffers();
        SSLEngineResult result1;
        SSLEngineResult result2;
        while (!isEngineClosed(ssle1) || !isEngineClosed(ssle2)) {
            log("================");
            result1 = ssle1.wrap(appOut1, oneToTwo);
            result2 = ssle2.wrap(appOut2, twoToOne);
            if ((result1.bytesConsumed() != 0) && (result1.bytesConsumed() != appBufferMax) && (result1.bytesConsumed() != OFFSET)) {
                throw new Exception("result1: " + result1);
            }
            if ((result2.bytesConsumed() != 0) && (result2.bytesConsumed() != appBufferMax) && (result2.bytesConsumed() != 2 * OFFSET)) {
                throw new Exception("result2: " + result2);
            }
            log("wrap1:  " + result1);
            log("oneToTwo  = " + oneToTwo);
            log("");
            log("wrap2:  " + result2);
            log("twoToOne  = " + twoToOne);
            runDelegatedTasks(result1, ssle1);
            runDelegatedTasks(result2, ssle2);
            oneToTwo.flip();
            twoToOne.flip();
            log("----");
            result1 = ssle1.unwrap(twoToOne, appIn1);
            result2 = ssle2.unwrap(oneToTwo, appIn2);
            if ((result1.bytesProduced() != 0) && (result1.bytesProduced() != appBufferMax) && (result1.bytesProduced() != 2 * OFFSET)) {
                throw new Exception("result1: " + result1);
            }
            if ((result2.bytesProduced() != 0) && (result2.bytesProduced() != appBufferMax) && (result2.bytesProduced() != OFFSET)) {
                throw new Exception("result1: " + result1);
            }
            log("unwrap1: " + result1);
            log("twoToOne  = " + twoToOne);
            log("");
            log("unwrap2: " + result2);
            log("oneToTwo  = " + oneToTwo);
            runDelegatedTasks(result1, ssle1);
            runDelegatedTasks(result2, ssle2);
            oneToTwo.compact();
            twoToOne.compact();
            if (!dataDone && (appOut1.limit() == appIn2.position()) && (appOut2.limit() == appIn1.position())) {
                checkTransfer(appOut1, appIn2);
                checkTransfer(appOut2, appIn1);
                log("Closing ssle1's *OUTBOUND*...");
                ssle1.closeOutbound();
                dataDone = true;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Security.setProperty("jdk.tls.disabledAlgorithms", "");
        LargeBufs test;
        test = new LargeBufs();
        test.runTest("SSL_RSA_WITH_RC4_128_MD5");
        test = new LargeBufs();
        test.runTest("SSL_RSA_WITH_3DES_EDE_CBC_SHA");
        System.out.println("Test Passed.");
    }

    public LargeBufs() throws Exception {
        sslc = getSSLContext(keyFilename, trustFilename);
    }

    private SSLContext getSSLContext(String keyFile, String trustFile) throws Exception {
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
        return sslCtx;
    }

    private void createBuffers() {
        SSLSession session = ssle1.getSession();
        appBufferMax = 16384;
        netBufferMax = session.getPacketBufferSize();
        Random random = new Random();
        byte[] one = new byte[appBufferMax * 5 + OFFSET];
        byte[] two = new byte[appBufferMax * 5 + 2 * OFFSET];
        random.nextBytes(one);
        random.nextBytes(two);
        appOut1 = ByteBuffer.wrap(one);
        appOut2 = ByteBuffer.wrap(two);
        appIn1 = ByteBuffer.allocate(appBufferMax * 6);
        appIn2 = ByteBuffer.allocate(appBufferMax * 6);
        oneToTwo = ByteBuffer.allocateDirect(netBufferMax);
        twoToOne = ByteBuffer.allocateDirect(netBufferMax);
        System.out.println("Testing arrays of: " + one.length + " and " + two.length);
        log("AppOut1 = " + appOut1);
        log("AppOut2 = " + appOut2);
        log("");
    }

    private static void runDelegatedTasks(SSLEngineResult result, SSLEngine engine) throws Exception {
        if (result.getHandshakeStatus() == HandshakeStatus.NEED_TASK) {
            Runnable runnable;
            while ((runnable = engine.getDelegatedTask()) != null) {
                log("running delegated task...");
                runnable.run();
            }
        }
    }

    private static boolean isEngineClosed(SSLEngine engine) {
        return (engine.isOutboundDone() && engine.isInboundDone());
    }

    private static void checkTransfer(ByteBuffer a, ByteBuffer b) throws Exception {
        a.flip();
        b.flip();
        if (!a.equals(b)) {
            throw new Exception("Data didn't transfer cleanly");
        } else {
            log("Data transferred cleanly");
        }
        a.position(a.limit());
        b.position(b.limit());
        a.limit(a.capacity());
        b.limit(b.capacity());
    }

    private static void log(String str) {
        if (debug) {
            System.out.println(str);
        }
    }
}
