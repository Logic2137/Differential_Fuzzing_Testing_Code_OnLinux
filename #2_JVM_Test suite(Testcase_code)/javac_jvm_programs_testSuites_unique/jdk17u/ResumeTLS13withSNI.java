import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.*;
import java.io.*;
import java.security.*;
import java.nio.*;
import java.util.List;

public class ResumeTLS13withSNI {

    private static final boolean logging = false;

    private static final boolean debug = true;

    private static final ByteBuffer clientOut = ByteBuffer.wrap("Hi Server, I'm Client".getBytes());

    private static final ByteBuffer serverOut = ByteBuffer.wrap("Hello Client, I'm Server".getBytes());

    private static final String pathToStores = "../etc";

    private static final String keyStoreFile = "keystore";

    private static final String trustStoreFile = "truststore";

    private static final char[] passphrase = "passphrase".toCharArray();

    private static final String keyFilename = System.getProperty("test.src", ".") + "/" + pathToStores + "/" + keyStoreFile;

    private static final String trustFilename = System.getProperty("test.src", ".") + "/" + pathToStores + "/" + trustStoreFile;

    private static final String HOST_NAME = "arf.yak.foo";

    private static final SNIHostName SNI_NAME = new SNIHostName(HOST_NAME);

    private static final SNIMatcher SNI_MATCHER = SNIHostName.createSNIMatcher("arf\\.yak\\.foo");

    public static void main(String[] args) throws Exception {
        if (debug) {
            System.setProperty("javax.net.debug", "ssl:handshake");
        }
        KeyManagerFactory kmf = makeKeyManagerFactory(keyFilename, passphrase);
        TrustManagerFactory tmf = makeTrustManagerFactory(trustFilename, passphrase);
        SSLContext sslCtx = SSLContext.getInstance("TLS");
        sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        SSLEngine clientEngine = makeEngine(sslCtx, kmf, tmf, true);
        SSLParameters cliSSLParams = clientEngine.getSSLParameters();
        cliSSLParams.setServerNames(List.of(SNI_NAME));
        clientEngine.setSSLParameters(cliSSLParams);
        clientEngine.setEnabledProtocols(new String[] { "TLSv1.3" });
        SSLEngine serverEngine = makeEngine(sslCtx, kmf, tmf, false);
        SSLParameters servSSLParams = serverEngine.getSSLParameters();
        servSSLParams.setSNIMatchers(List.of(SNI_MATCHER));
        serverEngine.setSSLParameters(servSSLParams);
        initialHandshake(clientEngine, serverEngine);
        SSLEngine newCliEngine = makeEngine(sslCtx, kmf, tmf, true);
        newCliEngine.setEnabledProtocols(new String[] { "TLSv1.3" });
        ByteBuffer resCliHello = getResumptionClientHello(newCliEngine);
        dumpBuffer("Resumed ClientHello Data", resCliHello);
        checkResumedClientHelloSNI(resCliHello);
    }

    private static void initialHandshake(SSLEngine clientEngine, SSLEngine serverEngine) throws Exception {
        boolean dataDone = false;
        SSLSession session = clientEngine.getSession();
        int appBufferMax = session.getApplicationBufferSize();
        int netBufferMax = session.getPacketBufferSize();
        ByteBuffer clientIn = ByteBuffer.allocate(appBufferMax + 50);
        ByteBuffer serverIn = ByteBuffer.allocate(appBufferMax + 50);
        ByteBuffer cTOs = ByteBuffer.allocateDirect(netBufferMax);
        ByteBuffer sTOc = ByteBuffer.allocateDirect(netBufferMax);
        SSLEngineResult clientResult;
        SSLEngineResult serverResult;
        Exception clientException = null;
        Exception serverException = null;
        while (!dataDone) {
            log("================");
            try {
                clientResult = clientEngine.wrap(clientOut, cTOs);
                log("client wrap: ", clientResult);
            } catch (Exception e) {
                clientException = e;
                System.err.println("Client wrap() threw: " + e.getMessage());
            }
            logEngineStatus(clientEngine);
            runDelegatedTasks(clientEngine);
            log("----");
            try {
                serverResult = serverEngine.wrap(serverOut, sTOc);
                log("server wrap: ", serverResult);
            } catch (Exception e) {
                serverException = e;
                System.err.println("Server wrap() threw: " + e.getMessage());
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
                System.err.println("Client unwrap() threw: " + e.getMessage());
            }
            logEngineStatus(clientEngine);
            runDelegatedTasks(clientEngine);
            log("----");
            try {
                serverResult = serverEngine.unwrap(cTOs, serverIn);
                log("server unwrap: ", serverResult);
            } catch (Exception e) {
                serverException = e;
                System.err.println("Server unwrap() threw: " + e.getMessage());
            }
            logEngineStatus(serverEngine);
            runDelegatedTasks(serverEngine);
            cTOs.compact();
            sTOc.compact();
            if (!dataDone && (clientOut.limit() == serverIn.position()) && (serverOut.limit() == clientIn.position())) {
                checkTransfer(serverOut, clientIn);
                checkTransfer(clientOut, serverIn);
                dataDone = true;
            }
        }
    }

    private static ByteBuffer getResumptionClientHello(SSLEngine clientEngine) throws Exception {
        SSLSession session = clientEngine.getSession();
        int appBufferMax = session.getApplicationBufferSize();
        int netBufferMax = session.getPacketBufferSize();
        ByteBuffer cTOs = ByteBuffer.allocateDirect(netBufferMax);
        Exception clientException = null;
        SSLEngineResult clientResult;
        SSLEngineResult serverResult;
        log("================");
        try {
            clientResult = clientEngine.wrap(clientOut, cTOs);
            log("client wrap: ", clientResult);
        } catch (Exception e) {
            clientException = e;
            System.err.println("Client wrap() threw: " + e.getMessage());
        }
        logEngineStatus(clientEngine);
        runDelegatedTasks(clientEngine);
        log("----");
        cTOs.flip();
        return cTOs;
    }

    private static void checkResumedClientHelloSNI(ByteBuffer resCliHello) throws Exception {
        boolean foundMatchingSNI = false;
        boolean foundPSK = false;
        resCliHello.position(resCliHello.position() + 5);
        byte hsMsgType = resCliHello.get();
        if (hsMsgType != 0x01) {
            throw new Exception("Message is not a ClientHello, MsgType = " + hsMsgType);
        }
        resCliHello.position(resCliHello.position() + 3);
        short chProto = resCliHello.getShort();
        if (chProto != 0x0303) {
            throw new Exception("Client Hello protocol version is not TLSv1.2: Got " + String.format("0x%04X", chProto));
        }
        resCliHello.position(resCliHello.position() + 32);
        int sessIdLen = Byte.toUnsignedInt(resCliHello.get());
        resCliHello.position(resCliHello.position() + sessIdLen);
        int csLen = Short.toUnsignedInt(resCliHello.getShort());
        resCliHello.position(resCliHello.position() + csLen);
        int compLen = Byte.toUnsignedInt(resCliHello.get());
        resCliHello.position(resCliHello.position() + compLen);
        System.err.println("ClientHello Extensions Check");
        int extListLen = Short.toUnsignedInt(resCliHello.getShort());
        while (extListLen > 0) {
            int extType = Short.toUnsignedInt(resCliHello.getShort());
            int extLen = Short.toUnsignedInt(resCliHello.getShort());
            switch(extType) {
                case 0:
                    System.err.println("* Found server_name Extension");
                    int snListLen = Short.toUnsignedInt(resCliHello.getShort());
                    while (snListLen > 0) {
                        int nameType = Byte.toUnsignedInt(resCliHello.get());
                        if (nameType == 0) {
                            int hostNameLen = Short.toUnsignedInt(resCliHello.getShort());
                            byte[] hostNameData = new byte[hostNameLen];
                            resCliHello.get(hostNameData);
                            String hostNameStr = new String(hostNameData);
                            System.err.println("\tHostname: " + hostNameStr);
                            if (hostNameStr.equals(HOST_NAME)) {
                                foundMatchingSNI = true;
                            }
                            snListLen -= 3 + hostNameLen;
                        } else {
                            throw new Exception("Unknown server name type: " + nameType);
                        }
                    }
                    break;
                case 41:
                    foundPSK = true;
                    System.err.println("* Found pre_shared_key Extension");
                    resCliHello.position(resCliHello.position() + extLen);
                    break;
                default:
                    System.err.format("* Found extension %d (%d bytes)\n", extType, extLen);
                    resCliHello.position(resCliHello.position() + extLen);
                    break;
            }
            extListLen -= extLen + 4;
        }
        if (!foundMatchingSNI) {
            throw new Exception("Could not find a matching server_name");
        } else if (!foundPSK) {
            throw new Exception("Missing PSK extension, not a resumption?");
        }
    }

    private static TrustManagerFactory makeTrustManagerFactory(String tsPath, char[] pass) throws GeneralSecurityException, IOException {
        TrustManagerFactory tmf;
        KeyStore ts = KeyStore.getInstance("JKS");
        try (FileInputStream fsIn = new FileInputStream(tsPath)) {
            ts.load(fsIn, pass);
            tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ts);
        }
        return tmf;
    }

    private static KeyManagerFactory makeKeyManagerFactory(String ksPath, char[] pass) throws GeneralSecurityException, IOException {
        KeyManagerFactory kmf;
        KeyStore ks = KeyStore.getInstance("JKS");
        try (FileInputStream fsIn = new FileInputStream(ksPath)) {
            ks.load(fsIn, pass);
            kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, pass);
        }
        return kmf;
    }

    private static SSLEngine makeEngine(SSLContext ctx, KeyManagerFactory kmf, TrustManagerFactory tmf, boolean isClient) throws GeneralSecurityException {
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        SSLEngine ssle = ctx.createSSLEngine("localhost", 8443);
        ssle.setUseClientMode(isClient);
        ssle.setNeedClientAuth(false);
        return ssle;
    }

    private static void logEngineStatus(SSLEngine engine) {
        log("\tCurrent HS State  " + engine.getHandshakeStatus().toString());
        log("\tisInboundDone():  " + engine.isInboundDone());
        log("\tisOutboundDone(): " + engine.isOutboundDone());
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
            System.err.println("The format of the SSLEngineResult is: \n" + "\t\"getStatus() / getHandshakeStatus()\" +\n" + "\t\"bytesConsumed() / bytesProduced()\"\n");
        }
        HandshakeStatus hsStatus = result.getHandshakeStatus();
        log(str + result.getStatus() + "/" + hsStatus + ", " + result.bytesConsumed() + "/" + result.bytesProduced() + " bytes");
        if (hsStatus == HandshakeStatus.FINISHED) {
            log("\t...ready for application data");
        }
    }

    private static void log(String str) {
        if (logging) {
            System.err.println(str);
        }
    }

    private static void dumpBuffer(String header, ByteBuffer data) {
        data.mark();
        System.err.format("========== %s ==========\n", header);
        int i = 0;
        while (data.remaining() > 0) {
            if (i != 0 && i % 16 == 0) {
                System.err.print("\n");
            }
            System.err.format("%02X ", data.get());
            i++;
        }
        System.err.println();
        data.reset();
    }
}
