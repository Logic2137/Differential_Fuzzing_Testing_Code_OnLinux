import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.*;
import java.io.*;
import java.security.*;
import java.nio.*;

public class SSLEngineFailedALPN {

    private static final boolean logging = true;

    private static final boolean debug = false;

    private final SSLContext sslc;

    private SSLEngine clientEngine;

    private ByteBuffer clientOut;

    private ByteBuffer clientIn;

    private SSLEngine serverEngine;

    private ByteBuffer serverOut;

    private ByteBuffer serverIn;

    private ByteBuffer cTOs;

    private ByteBuffer sTOc;

    private static final String pathToStores = "../../../../javax/net/ssl/etc";

    private static final String keyStoreFile = "keystore";

    private static final String trustStoreFile = "truststore";

    private static final char[] passphrase = "passphrase".toCharArray();

    private static final String keyFilename = System.getProperty("test.src", ".") + "/" + pathToStores + "/" + keyStoreFile;

    private static final String trustFilename = System.getProperty("test.src", ".") + "/" + pathToStores + "/" + trustStoreFile;

    public static void main(String[] args) throws Exception {
        if (debug) {
            System.setProperty("javax.net.debug", "all");
        }
        SSLEngineFailedALPN test = new SSLEngineFailedALPN();
        test.runTest();
        System.out.println("Test Passed.");
    }

    public SSLEngineFailedALPN() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore ts = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyFilename), passphrase);
        ts.load(new FileInputStream(trustFilename), passphrase);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);
        SSLContext sslCtx = SSLContext.getInstance("TLS");
        sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        sslc = sslCtx;
    }

    private void runTest() throws Exception {
        boolean dataDone = false;
        createSSLEngines();
        createBuffers();
        SSLEngineResult clientResult;
        SSLEngineResult serverResult;
        Exception clientException = null;
        Exception serverException = null;
        while (!isEngineClosed(clientEngine) || !isEngineClosed(serverEngine)) {
            log("================");
            try {
                clientResult = clientEngine.wrap(clientOut, cTOs);
                log("client wrap: ", clientResult);
            } catch (Exception e) {
                clientException = e;
                System.out.println("Client wrap() threw: " + e.getMessage());
            }
            logEngineStatus(clientEngine);
            runDelegatedTasks(clientEngine);
            log("----");
            try {
                serverResult = serverEngine.wrap(serverOut, sTOc);
                log("server wrap: ", serverResult);
            } catch (Exception e) {
                serverException = e;
                System.out.println("Server wrap() threw: " + e.getMessage());
            }
            logEngineStatus(serverEngine);
            runDelegatedTasks(serverEngine);
            cTOs.flip();
            sTOc.flip();
            log("--------");
            try {
                clientResult = clientEngine.unwrap(sTOc, clientIn);
                log("client unwrap: ", clientResult);
            } catch (Exception e) {
                clientException = e;
                System.out.println("Client unwrap() threw: " + e.getMessage());
            }
            logEngineStatus(clientEngine);
            runDelegatedTasks(clientEngine);
            log("----");
            try {
                serverResult = serverEngine.unwrap(cTOs, serverIn);
                log("server unwrap: ", serverResult);
            } catch (Exception e) {
                serverException = e;
                System.out.println("Server unwrap() threw: " + e.getMessage());
            }
            logEngineStatus(serverEngine);
            runDelegatedTasks(serverEngine);
            cTOs.compact();
            sTOc.compact();
            if (!dataDone && (clientOut.limit() == serverIn.position()) && (serverOut.limit() == clientIn.position())) {
                checkTransfer(serverOut, clientIn);
                checkTransfer(clientOut, serverIn);
                log("\tClosing clientEngine's *OUTBOUND*...");
                clientEngine.closeOutbound();
                logEngineStatus(clientEngine);
                dataDone = true;
                log("\tClosing serverEngine's *OUTBOUND*...");
                serverEngine.closeOutbound();
                logEngineStatus(serverEngine);
            }
        }
        log("================");
        if ((clientException != null) && (clientException instanceof SSLHandshakeException)) {
            log("Client threw proper exception");
            clientException.printStackTrace(System.out);
        } else {
            throw new Exception("Client Exception not seen");
        }
        if ((serverException != null) && (serverException instanceof SSLHandshakeException)) {
            log("Server threw proper exception:");
            serverException.printStackTrace(System.out);
        } else {
            throw new Exception("Server Exception not seen");
        }
    }

    private static void logEngineStatus(SSLEngine engine) {
        log("\tCurrent HS State  " + engine.getHandshakeStatus().toString());
        log("\tisInboundDone():  " + engine.isInboundDone());
        log("\tisOutboundDone(): " + engine.isOutboundDone());
    }

    private void createSSLEngines() throws Exception {
        serverEngine = sslc.createSSLEngine();
        serverEngine.setUseClientMode(false);
        serverEngine.setNeedClientAuth(true);
        SSLParameters paramsServer = serverEngine.getSSLParameters();
        paramsServer.setApplicationProtocols(new String[] { "one" });
        serverEngine.setSSLParameters(paramsServer);
        clientEngine = sslc.createSSLEngine("client", 80);
        clientEngine.setUseClientMode(true);
        SSLParameters paramsClient = clientEngine.getSSLParameters();
        paramsClient.setApplicationProtocols(new String[] { "two" });
        clientEngine.setSSLParameters(paramsClient);
    }

    private void createBuffers() {
        SSLSession session = clientEngine.getSession();
        int appBufferMax = session.getApplicationBufferSize();
        int netBufferMax = session.getPacketBufferSize();
        clientIn = ByteBuffer.allocate(appBufferMax + 50);
        serverIn = ByteBuffer.allocate(appBufferMax + 50);
        cTOs = ByteBuffer.allocateDirect(netBufferMax);
        sTOc = ByteBuffer.allocateDirect(netBufferMax);
        clientOut = ByteBuffer.wrap("Hi Server, I'm Client".getBytes());
        serverOut = ByteBuffer.wrap("Hello Client, I'm Server".getBytes());
    }

    private static void runDelegatedTasks(SSLEngine engine) throws Exception {
        if (engine.getHandshakeStatus() == HandshakeStatus.NEED_TASK) {
            Runnable runnable;
            while ((runnable = engine.getDelegatedTask()) != null) {
                log("    running delegated task...");
                runnable.run();
            }
            HandshakeStatus hsStatus = engine.getHandshakeStatus();
            if (hsStatus == HandshakeStatus.NEED_TASK) {
                throw new Exception("handshake shouldn't need additional tasks");
            }
            logEngineStatus(engine);
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
            log("\tData transferred cleanly");
        }
        a.position(a.limit());
        b.position(b.limit());
        a.limit(a.capacity());
        b.limit(b.capacity());
    }

    private static boolean resultOnce = true;

    private static void log(String str, SSLEngineResult result) {
        if (!logging) {
            return;
        }
        if (resultOnce) {
            resultOnce = false;
            System.out.println("The format of the SSLEngineResult is: \n" + "\t\"getStatus() / getHandshakeStatus()\" +\n" + "\t\"bytesConsumed() / bytesProduced()\"\n");
        }
        HandshakeStatus hsStatus = result.getHandshakeStatus();
        log(str + result.getStatus() + "/" + hsStatus + ", " + result.bytesConsumed() + "/" + result.bytesProduced() + " bytes");
        if (hsStatus == HandshakeStatus.FINISHED) {
            log("\t...ready for application data");
        }
    }

    private static void log(String str) {
        if (logging) {
            System.out.println(str);
        }
    }
}
