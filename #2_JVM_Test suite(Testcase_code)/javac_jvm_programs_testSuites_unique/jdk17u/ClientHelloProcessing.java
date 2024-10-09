import java.io.FileInputStream;
import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class ClientHelloProcessing {

    private static final ByteBuffer SERVOUTBUF = ByteBuffer.wrap("Server Side".getBytes());

    private static final String pathToStores = "../etc";

    private static final String keyStoreFile = "keystore";

    private static final String trustStoreFile = "truststore";

    private static final String passwd = "passphrase";

    private static final String keyFilename = System.getProperty("test.src", ".") + "/" + pathToStores + "/" + keyStoreFile;

    private static final String trustFilename = System.getProperty("test.src", ".") + "/" + pathToStores + "/" + trustStoreFile;

    private static TrustManagerFactory trustMgrFac = null;

    private static KeyManagerFactory keyMgrFac = null;

    private static final byte[] CLIHELLO_NOPSK_NOPSKEXMODE = { 22, 3, 1, 1, 0, 1, 0, 0, -4, 3, 3, -101, 121, 106, -48, -53, -43, 89, -5, 72, -4, 75, -93, 45, -91, -69, -116, 30, -7, -89, -38, -123, 35, 24, 96, 29, -93, -22, 10, -97, -15, -11, 3, 32, -2, -124, 17, 32, 91, -55, -102, 80, 105, 82, -11, -62, -123, 105, -6, -53, -106, -1, 15, 55, 98, 27, -32, 114, -126, -13, 42, -104, -102, 37, -65, 52, 0, 8, 19, 2, 19, 3, 19, 1, 0, -1, 1, 0, 0, -85, 0, 0, 0, 14, 0, 12, 0, 0, 9, 108, 111, 99, 97, 108, 104, 111, 115, 116, 0, 11, 0, 4, 3, 0, 1, 2, 0, 10, 0, 4, 0, 2, 0, 23, 0, 35, 0, 0, 0, 5, 0, 5, 1, 0, 0, 0, 0, 0, 22, 0, 0, 0, 23, 0, 0, 0, 13, 0, 30, 0, 28, 4, 3, 5, 3, 6, 3, 8, 7, 8, 8, 8, 9, 8, 10, 8, 11, 8, 4, 8, 5, 8, 6, 4, 1, 5, 1, 6, 1, 0, 43, 0, 3, 2, 3, 4, 0, 51, 0, 71, 0, 69, 0, 23, 0, 65, 4, 125, -92, -50, -91, -39, -55, -114, 0, 22, 2, -50, 123, -126, 0, -94, 100, -119, -106, 125, -81, -24, 51, -84, 25, 25, -115, 13, -17, -20, 93, 68, -97, -79, -98, 91, 86, 91, -114, 123, 119, -87, -12, 32, 63, -41, 50, 126, -70, 96, 33, -6, 94, -7, -68, 54, -47, 53, 0, 88, 40, -48, -102, -50, 88 };

    private static final byte[] CLIHELLO_NOPSK_YESPSKEXMODE = { 22, 3, 1, 1, 6, 1, 0, 1, 2, 3, 3, -101, 121, 106, -48, -53, -43, 89, -5, 72, -4, 75, -93, 45, -91, -69, -116, 30, -7, -89, -38, -123, 35, 24, 96, 29, -93, -22, 10, -97, -15, -11, 3, 32, -2, -124, 17, 32, 91, -55, -102, 80, 105, 82, -11, -62, -123, 105, -6, -53, -106, -1, 15, 55, 98, 27, -32, 114, -126, -13, 42, -104, -102, 37, -65, 52, 0, 8, 19, 2, 19, 3, 19, 1, 0, -1, 1, 0, 0, -79, 0, 0, 0, 14, 0, 12, 0, 0, 9, 108, 111, 99, 97, 108, 104, 111, 115, 116, 0, 11, 0, 4, 3, 0, 1, 2, 0, 10, 0, 4, 0, 2, 0, 23, 0, 35, 0, 0, 0, 5, 0, 5, 1, 0, 0, 0, 0, 0, 22, 0, 0, 0, 23, 0, 0, 0, 13, 0, 30, 0, 28, 4, 3, 5, 3, 6, 3, 8, 7, 8, 8, 8, 9, 8, 10, 8, 11, 8, 4, 8, 5, 8, 6, 4, 1, 5, 1, 6, 1, 0, 43, 0, 3, 2, 3, 4, 0, 45, 0, 2, 1, 1, 0, 51, 0, 71, 0, 69, 0, 23, 0, 65, 4, 125, -92, -50, -91, -39, -55, -114, 0, 22, 2, -50, 123, -126, 0, -94, 100, -119, -106, 125, -81, -24, 51, -84, 25, 25, -115, 13, -17, -20, 93, 68, -97, -79, -98, 91, 86, 91, -114, 123, 119, -87, -12, 32, 63, -41, 50, 126, -70, 96, 33, -6, 94, -7, -68, 54, -47, 53, 0, 88, 40, -48, -102, -50, 88 };

    private static final byte[] CLIHELLO_YESPSK_NOPSKEXMODE = { 22, 3, 1, 1, 62, 1, 0, 1, 58, 3, 3, -25, 48, -28, 35, 54, -95, -98, -39, -3, -76, 41, 25, -58, 87, 105, 19, 46, -98, 119, -102, 121, 127, 24, -116, -9, -99, 22, 116, -97, 90, 73, -18, 32, 108, 110, -45, 20, 8, 4, 47, -85, -48, -60, 127, -34, -18, 109, 25, -34, 45, 103, -107, -29, 117, -112, -16, 14, -5, -24, 24, 61, -9, 28, -119, -73, 0, 8, 19, 2, 19, 3, 19, 1, 0, -1, 1, 0, 0, -23, 0, 0, 0, 14, 0, 12, 0, 0, 9, 108, 111, 99, 97, 108, 104, 111, 115, 116, 0, 11, 0, 4, 3, 0, 1, 2, 0, 10, 0, 4, 0, 2, 0, 23, 0, 35, 0, 0, 0, 5, 0, 5, 1, 0, 0, 0, 0, 0, 22, 0, 0, 0, 23, 0, 0, 0, 13, 0, 30, 0, 28, 4, 3, 5, 3, 6, 3, 8, 7, 8, 8, 8, 9, 8, 10, 8, 11, 8, 4, 8, 5, 8, 6, 4, 1, 5, 1, 6, 1, 0, 43, 0, 3, 2, 3, 4, 0, 51, 0, 71, 0, 69, 0, 23, 0, 65, 4, -6, 101, 105, -2, -6, 85, -99, -37, 112, 90, 44, -123, -107, 4, -12, -64, 92, 40, 100, 22, -53, -124, 54, 56, 102, 25, 76, -86, -1, 6, 110, 95, 92, -86, -35, -101, 115, 85, 99, 19, 6, -43, 105, -37, -92, 53, -97, 84, -1, -53, 87, -53, -107, -13, -14, 32, 101, -35, 39, 102, -17, -119, -25, -51, 0, 41, 0, 58, 0, 21, 0, 15, 67, 108, 105, 101, 110, 116, 95, 105, 100, 101, 110, 116, 105, 116, 121, 0, 0, 0, 0, 0, 33, 32, -113, -27, -44, -71, -68, -26, -47, 57, -82, -29, -13, -61, 77, 52, -60, 27, 74, -120, -104, 102, 21, 121, 0, 48, 43, -40, -19, -67, 57, -20, 97, 23 };

    private static final byte[] CLIHELLO_YESPSK_YESPSKEXMODE = { 22, 3, 1, 1, 68, 1, 0, 1, 64, 3, 3, -25, 48, -28, 35, 54, -95, -98, -39, -3, -76, 41, 25, -58, 87, 105, 19, 46, -98, 119, -102, 121, 127, 24, -116, -9, -99, 22, 116, -97, 90, 73, -18, 32, 108, 110, -45, 20, 8, 4, 47, -85, -48, -60, 127, -34, -18, 109, 25, -34, 45, 103, -107, -29, 117, -112, -16, 14, -5, -24, 24, 61, -9, 28, -119, -73, 0, 8, 19, 2, 19, 3, 19, 1, 0, -1, 1, 0, 0, -17, 0, 0, 0, 14, 0, 12, 0, 0, 9, 108, 111, 99, 97, 108, 104, 111, 115, 116, 0, 11, 0, 4, 3, 0, 1, 2, 0, 10, 0, 4, 0, 2, 0, 23, 0, 35, 0, 0, 0, 5, 0, 5, 1, 0, 0, 0, 0, 0, 22, 0, 0, 0, 23, 0, 0, 0, 13, 0, 30, 0, 28, 4, 3, 5, 3, 6, 3, 8, 7, 8, 8, 8, 9, 8, 10, 8, 11, 8, 4, 8, 5, 8, 6, 4, 1, 5, 1, 6, 1, 0, 43, 0, 3, 2, 3, 4, 0, 45, 0, 2, 1, 1, 0, 51, 0, 71, 0, 69, 0, 23, 0, 65, 4, -6, 101, 105, -2, -6, 85, -99, -37, 112, 90, 44, -123, -107, 4, -12, -64, 92, 40, 100, 22, -53, -124, 54, 56, 102, 25, 76, -86, -1, 6, 110, 95, 92, -86, -35, -101, 115, 85, 99, 19, 6, -43, 105, -37, -92, 53, -97, 84, -1, -53, 87, -53, -107, -13, -14, 32, 101, -35, 39, 102, -17, -119, -25, -51, 0, 41, 0, 58, 0, 21, 0, 15, 67, 108, 105, 101, 110, 116, 95, 105, 100, 101, 110, 116, 105, 116, 121, 0, 0, 0, 0, 0, 33, 32, -113, -27, -44, -71, -68, -26, -47, 57, -82, -29, -13, -61, 77, 52, -60, 27, 74, -120, -104, 102, 21, 121, 0, 48, 43, -40, -19, -67, 57, -20, 97, 23 };

    private static final byte[] CLIHELLO_SUPGRP_SECT163K1 = { 22, 3, 1, 0, -46, 1, 0, 0, -50, 3, 3, 5, -53, -82, -101, -125, 72, 81, -40, 86, 53, 91, 114, 96, 28, -74, 123, 124, -44, -21, 81, -14, -98, -43, 11, 90, -87, -106, 13, 63, -62, 100, 111, 0, 0, 56, -64, 44, -64, 48, 0, -97, -52, -87, -52, -88, -52, -86, -64, 43, -64, 47, 0, -98, -64, 36, -64, 40, 0, 107, -64, 35, -64, 39, 0, 103, -64, 10, -64, 20, 0, 57, -64, 9, -64, 19, 0, 51, 0, -99, 0, -100, 0, 61, 0, 60, 0, 53, 0, 47, 0, -1, 1, 0, 0, 109, 0, 0, 0, 14, 0, 12, 0, 0, 9, 108, 111, 99, 97, 108, 104, 111, 115, 116, 0, 11, 0, 4, 3, 0, 1, 2, 0, 10, 0, 6, 0, 4, 0, 1, 0, 23, 0, 35, 0, 0, 0, 5, 0, 5, 1, 0, 0, 0, 0, 0, 22, 0, 0, 0, 23, 0, 0, 0, 13, 0, 48, 0, 46, 4, 3, 5, 3, 6, 3, 8, 7, 8, 8, 8, 9, 8, 10, 8, 11, 8, 4, 8, 5, 8, 6, 4, 1, 5, 1, 6, 1, 3, 3, 2, 3, 3, 1, 2, 1, 3, 2, 2, 2, 4, 2, 5, 2, 6, 2 };

    public static interface TestCase {

        void execTest() throws Exception;
    }

    private static final Map<String, TestCase> TESTMAP = new HashMap<>();

    public static void main(String[] args) throws Exception {
        boolean allGood = true;
        System.setProperty("javax.net.debug", "ssl:handshake");
        trustMgrFac = makeTrustManagerFactory(trustFilename, passwd);
        keyMgrFac = makeKeyManagerFactory(keyFilename, passwd);
        TESTMAP.put("noPskNoKexModes", noPskNoKexModes);
        TESTMAP.put("noPskYesKexModes", noPskYesKexModes);
        TESTMAP.put("yesPskNoKexModes", yesPskNoKexModes);
        TESTMAP.put("yesPskYesKexModes", yesPskYesKexModes);
        TESTMAP.put("supGroupsSect163k1", supGroupsSect163k1);
        if (args == null || args.length < 1) {
            throw new Exception("FAIL: Test @run line is missing a test label");
        }
        TestCase test = Objects.requireNonNull(TESTMAP.get(args[0]), "No TestCase found for test label " + args[0]);
        test.execTest();
    }

    private static final TestCase noPskNoKexModes = new TestCase() {

        @Override
        public void execTest() throws Exception {
            System.out.println("\nTest: PSK = No, PSKEX = No");
            processClientHello("TLS", CLIHELLO_NOPSK_NOPSKEXMODE);
            System.out.println("PASS");
        }
    };

    private static final TestCase noPskYesKexModes = new TestCase() {

        @Override
        public void execTest() throws Exception {
            System.out.println("\nTest: PSK = No, PSKEX = Yes");
            processClientHello("TLS", CLIHELLO_NOPSK_YESPSKEXMODE);
            System.out.println("PASS");
        }
    };

    private static final TestCase yesPskNoKexModes = new TestCase() {

        @Override
        public void execTest() throws Exception {
            try {
                System.out.println("\nTest: PSK = Yes, PSKEX = No");
                processClientHello("TLS", CLIHELLO_YESPSK_NOPSKEXMODE);
                throw new Exception("FAIL: Client Hello processed without expected error");
            } catch (SSLHandshakeException sslhe) {
                System.out.println("PASS: Caught expected exception: " + sslhe);
            }
        }
    };

    private static final TestCase yesPskYesKexModes = new TestCase() {

        @Override
        public void execTest() throws Exception {
            System.out.println("\nTest: PSK = Yes, PSKEX = Yes");
            processClientHello("TLS", CLIHELLO_YESPSK_YESPSKEXMODE);
            System.out.println("PASS");
        }
    };

    private static final TestCase supGroupsSect163k1 = new TestCase() {

        @Override
        public void execTest() throws Exception {
            System.out.println("\nTest: Use of non-default-enabled " + "Supported Group (sect163k1)");
            processClientHello("TLS", CLIHELLO_SUPGRP_SECT163K1);
            System.out.println("PASS");
        }
    };

    private static void processClientHello(String proto, byte[] message) throws Exception {
        SSLEngine serverEng = makeServerEngine(proto, keyMgrFac, trustMgrFac);
        ByteBuffer sTOc = makePacketBuf(serverEng);
        SSLEngineResult serverResult;
        ByteBuffer cTOs = ByteBuffer.wrap(message);
        System.out.println("CLIENT-TO-SERVER\n" + dumpHexBytes(cTOs, 16, "\n", " "));
        serverResult = serverEng.unwrap(cTOs, SERVOUTBUF);
        printResult("server unwrap: ", serverResult);
        runDelegatedTasks(serverResult, serverEng);
        serverEng.wrap(SERVOUTBUF, sTOc);
    }

    private static TrustManagerFactory makeTrustManagerFactory(String tsPath, String pass) throws GeneralSecurityException, IOException {
        KeyStore ts = KeyStore.getInstance("JKS");
        char[] passphrase = pass.toCharArray();
        ts.load(new FileInputStream(tsPath), passphrase);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);
        return tmf;
    }

    private static KeyManagerFactory makeKeyManagerFactory(String ksPath, String pass) throws GeneralSecurityException, IOException {
        KeyStore ks = KeyStore.getInstance("JKS");
        char[] passphrase = pass.toCharArray();
        ks.load(new FileInputStream(ksPath), passphrase);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
        return kmf;
    }

    private static SSLEngine makeServerEngine(String proto, KeyManagerFactory kmf, TrustManagerFactory tmf) throws GeneralSecurityException {
        SSLContext ctx = SSLContext.getInstance(proto != null ? proto : "TLS");
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        SSLEngine ssle = ctx.createSSLEngine();
        ssle.setUseClientMode(false);
        ssle.setNeedClientAuth(false);
        return ssle;
    }

    private static ByteBuffer makePacketBuf(SSLEngine engine) {
        SSLSession sess = engine.getSession();
        ByteBuffer packetBuf = ByteBuffer.allocate(sess.getPacketBufferSize());
        return packetBuf;
    }

    private static void runDelegatedTasks(SSLEngineResult result, SSLEngine engine) throws Exception {
        HandshakeStatus hsStatus = result.getHandshakeStatus();
        if (hsStatus == HandshakeStatus.NEED_TASK) {
            Runnable runnable;
            while ((runnable = engine.getDelegatedTask()) != null) {
                System.out.println("\trunning delegated task...");
                runnable.run();
            }
            hsStatus = engine.getHandshakeStatus();
            if (hsStatus == HandshakeStatus.NEED_TASK) {
                throw new Exception("handshake shouldn't need additional tasks");
            }
            System.out.println("\tnew HandshakeStatus: " + hsStatus);
        }
    }

    private static void printResult(String str, SSLEngineResult result) {
        System.out.println("The format of the SSLEngineResult is: \n" + "\t\"getStatus() / getHandshakeStatus()\" +\n" + "\t\"bytesConsumed() / bytesProduced()\"\n");
        HandshakeStatus hsStatus = result.getHandshakeStatus();
        System.out.println(str + result.getStatus() + "/" + hsStatus + ", " + result.bytesConsumed() + "/" + result.bytesProduced() + " bytes");
        if (hsStatus == HandshakeStatus.FINISHED) {
            System.out.println("\t...ready for application data");
        }
    }

    private static String dumpHexBytes(byte[] data, int itemsPerLine, String lineDelim, String itemDelim) {
        return dumpHexBytes(ByteBuffer.wrap(data), itemsPerLine, lineDelim, itemDelim);
    }

    private static String dumpHexBytes(ByteBuffer data, int itemsPerLine, String lineDelim, String itemDelim) {
        StringBuilder sb = new StringBuilder();
        if (data != null) {
            data.mark();
            int i = 0;
            while (data.remaining() > 0) {
                if (i % itemsPerLine == 0 && i != 0) {
                    sb.append(lineDelim);
                }
                sb.append(String.format("%02X", data.get())).append(itemDelim);
                i++;
            }
            data.reset();
        }
        return sb.toString();
    }
}
