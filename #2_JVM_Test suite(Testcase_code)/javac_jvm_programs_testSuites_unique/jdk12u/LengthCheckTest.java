





import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.*;
import java.io.*;
import java.security.*;
import java.nio.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class LengthCheckTest {

    
    private static final boolean logging = true;

    
    private static final boolean debug = false;
    private static final boolean dumpBufs = true;

    private final SSLContext sslc;

    private SSLEngine clientEngine;     
    private ByteBuffer clientOut;       
    private ByteBuffer clientIn;        

    private SSLEngine serverEngine;     
    private ByteBuffer serverOut;       
    private ByteBuffer serverIn;        

    private HandshakeTest handshakeTest;

    
    private ByteBuffer cTOs;            
    private ByteBuffer sTOc;            

    
    private static final String pathToStores = "../../../../javax/net/ssl/etc";
    private static final String keyStoreFile = "keystore";
    private static final String trustStoreFile = "truststore";
    private static final String passwd = "passphrase";

    private static final String keyFilename =
            System.getProperty("test.src", ".") + "/" + pathToStores +
                "/" + keyStoreFile;
    private static final String trustFilename =
            System.getProperty("test.src", ".") + "/" + pathToStores +
                "/" + trustStoreFile;

    
    private static final int TLS_RECTYPE_CCS = 0x14;
    private static final int TLS_RECTYPE_ALERT = 0x15;
    private static final int TLS_RECTYPE_HANDSHAKE = 0x16;
    private static final int TLS_RECTYPE_APPDATA = 0x17;

    private static final int TLS_HS_HELLO_REQUEST = 0x00;
    private static final int TLS_HS_CLIENT_HELLO = 0x01;
    private static final int TLS_HS_SERVER_HELLO = 0x02;
    private static final int TLS_HS_CERTIFICATE = 0x0B;
    private static final int TLS_HS_SERVER_KEY_EXCHG = 0x0C;
    private static final int TLS_HS_CERT_REQUEST = 0x0D;
    private static final int TLS_HS_SERVER_HELLO_DONE = 0x0E;
    private static final int TLS_HS_CERT_VERIFY = 0x0F;
    private static final int TLS_HS_CLIENT_KEY_EXCHG = 0x10;
    private static final int TLS_HS_FINISHED = 0x14;

    
    
    private static final int TLS_ALERT_LVL_WARNING = 0x01;
    private static final int TLS_ALERT_LVL_FATAL = 0x02;

    private static final int TLS_ALERT_UNEXPECTED_MSG = 0x0A;
    private static final int TLS_ALERT_HANDSHAKE_FAILURE = 0x28;
    private static final int TLS_ALERT_INTERNAL_ERROR = 0x50;
    private static final int TLS_ALERT_ILLEGAL_PARAMETER = 0x2F;

    public interface HandshakeTest {
        void execTest() throws Exception;
    }

    public final HandshakeTest servSendLongID = new HandshakeTest() {
        @Override
        public void execTest() throws Exception {
            boolean gotException = false;
            SSLEngineResult clientResult;   
            SSLEngineResult serverResult;   

            log("\n==== Test: Client receives 64-byte session ID ====");

            
            clientResult = clientEngine.wrap(clientOut, cTOs);
            log("client wrap: ", clientResult);
            runDelegatedTasks(clientResult, clientEngine);
            cTOs.flip();
            dumpByteBuffer("CLIENT-TO-SERVER", cTOs);

            
            serverResult = serverEngine.unwrap(cTOs, serverIn);
            log("server unwrap: ", serverResult);
            runDelegatedTasks(serverResult, serverEngine);
            cTOs.compact();

            
            serverResult = serverEngine.wrap(serverOut, sTOc);
            log("server wrap: ", serverResult);
            runDelegatedTasks(serverResult, serverEngine);
            sTOc.flip();

            
            
            if (isTlsMessage(sTOc, TLS_RECTYPE_HANDSHAKE,
                        TLS_HS_SERVER_HELLO)) {
                ArrayList<ByteBuffer> recList = splitRecord(sTOc);

                
                
                ByteBuffer servHelloBuf =
                        createEvilServerHello(recList.get(0), 64);

                recList.set(0, servHelloBuf);

                
                
                

                Iterator<ByteBuffer> iter = recList.iterator();
                while (!gotException && (iter.hasNext())) {
                    ByteBuffer bBuf = iter.next();
                    dumpByteBuffer("SERVER-TO-CLIENT", bBuf);
                    try {
                        clientResult = clientEngine.unwrap(bBuf, clientIn);
                    } catch (SSLProtocolException e) {
                        log("Received expected SSLProtocolException: " + e);
                        gotException = true;
                    }
                    log("client unwrap: ", clientResult);
                    runDelegatedTasks(clientResult, clientEngine);
                }
            } else {
                dumpByteBuffer("SERVER-TO-CLIENT", sTOc);
                log("client unwrap: ", clientResult);
                runDelegatedTasks(clientResult, clientEngine);
            }
            sTOc.compact();

            
            clientResult = clientEngine.wrap(clientOut, cTOs);
            log("client wrap: ", clientResult);
            runDelegatedTasks(clientResult, clientEngine);
            cTOs.flip();
            dumpByteBuffer("CLIENT-TO-SERVER", cTOs);

            
            
            
            if (gotException == false ||
                    !isTlsMessage(cTOs, TLS_RECTYPE_ALERT, TLS_ALERT_LVL_FATAL,
                            TLS_ALERT_ILLEGAL_PARAMETER)) {
                throw new SSLException(
                    "Client failed to throw Alert:fatal:internal_error");
            }
        }
    };

    public final HandshakeTest clientSendLongID = new HandshakeTest() {
        @Override
        public void execTest() throws Exception {
            boolean gotException = false;
            SSLEngineResult clientResult;   
            SSLEngineResult serverResult;   

            log("\n==== Test: Server receives 64-byte session ID ====");

            
            ByteBuffer evilClientHello = createEvilClientHello(64);
            dumpByteBuffer("CLIENT-TO-SERVER", evilClientHello);

            
            serverResult = serverEngine.unwrap(evilClientHello, serverIn);
            log("server unwrap: ", serverResult);
            runDelegatedTasks(serverResult, serverEngine);
            evilClientHello.compact();

            
            
            
            try {
                serverResult = serverEngine.wrap(serverOut, sTOc);
                log("server wrap: ", serverResult);
                runDelegatedTasks(serverResult, serverEngine);
            } catch (SSLProtocolException ssle) {
                log("Received expected SSLProtocolException: " + ssle);
                gotException = true;
            }

            
            serverResult = serverEngine.wrap(serverOut, sTOc);
            log("server wrap: ", serverResult);
            runDelegatedTasks(serverResult, serverEngine);
            sTOc.flip();
            dumpByteBuffer("SERVER-TO-CLIENT", sTOc);

            
            
            
            if (gotException == false ||
                    !isTlsMessage(sTOc, TLS_RECTYPE_ALERT, TLS_ALERT_LVL_FATAL,
                        TLS_ALERT_ILLEGAL_PARAMETER)) {
                throw new SSLException(
                    "Server failed to throw Alert:fatal:internal_error");
            }
        }
    };


    
    public static void main(String args[]) throws Exception {
        List<LengthCheckTest> ccsTests = new ArrayList<>();

        if (debug) {
            System.setProperty("javax.net.debug", "ssl");
        }

        ccsTests.add(new LengthCheckTest("ServSendLongID"));
        ccsTests.add(new LengthCheckTest("ClientSendLongID"));

        for (LengthCheckTest test : ccsTests) {
            test.runTest();
        }

        System.out.println("Test Passed.");
    }

    
    public LengthCheckTest(String testName) throws Exception {

        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore ts = KeyStore.getInstance("JKS");

        char[] passphrase = "passphrase".toCharArray();

        ks.load(new FileInputStream(keyFilename), passphrase);
        ts.load(new FileInputStream(trustFilename), passphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);

        SSLContext sslCtx = SSLContext.getInstance("TLS");

        sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        sslc = sslCtx;

        switch (testName) {
            case "ServSendLongID":
                handshakeTest = servSendLongID;
                break;
            case "ClientSendLongID":
                handshakeTest = clientSendLongID;
                break;
            default:
                throw new IllegalArgumentException("Unknown test name: " +
                        testName);
        }
    }

    
    private void runTest() throws Exception {
        boolean dataDone = false;

        createSSLEngines();
        createBuffers();

        handshakeTest.execTest();
    }

    
    private void createSSLEngines() throws Exception {
        
        serverEngine = sslc.createSSLEngine();
        serverEngine.setUseClientMode(false);
        serverEngine.setNeedClientAuth(false);

        
        clientEngine = sslc.createSSLEngine("client", 80);
        clientEngine.setUseClientMode(true);

        
        
        
        clientEngine.setEnabledProtocols(new String[]{"TLSv1"});
        clientEngine.setEnabledCipherSuites(
                new String[]{"TLS_RSA_WITH_AES_128_CBC_SHA"});
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

    
    private static void runDelegatedTasks(SSLEngineResult result,
            SSLEngine engine) throws Exception {

        if (result.getHandshakeStatus() == HandshakeStatus.NEED_TASK) {
            Runnable runnable;
            while ((runnable = engine.getDelegatedTask()) != null) {
                log("\trunning delegated task...");
                runnable.run();
            }
            HandshakeStatus hsStatus = engine.getHandshakeStatus();
            if (hsStatus == HandshakeStatus.NEED_TASK) {
                throw new Exception(
                    "handshake shouldn't need additional tasks");
            }
            log("\tnew HandshakeStatus: " + hsStatus);
        }
    }

    private static boolean isEngineClosed(SSLEngine engine) {
        return (engine.isOutboundDone() && engine.isInboundDone());
    }

    
    private static void checkTransfer(ByteBuffer a, ByteBuffer b)
            throws Exception {
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
            System.out.println("The format of the SSLEngineResult is: \n" +
                "\t\"getStatus() / getHandshakeStatus()\" +\n" +
                "\t\"bytesConsumed() / bytesProduced()\"\n");
        }
        HandshakeStatus hsStatus = result.getHandshakeStatus();
        log(str +
            result.getStatus() + "/" + hsStatus + ", " +
            result.bytesConsumed() + "/" + result.bytesProduced() +
            " bytes");
        if (hsStatus == HandshakeStatus.FINISHED) {
            log("\t...ready for application data");
        }
    }

    private static void log(String str) {
        if (logging) {
            System.out.println(str);
        }
    }

    
    private ArrayList<ByteBuffer> splitRecord(ByteBuffer tlsRecord) {
        SSLSession session = clientEngine.getSession();
        int netBufferMax = session.getPacketBufferSize();
        ArrayList<ByteBuffer> recordList = new ArrayList<>();

        if (tlsRecord.hasRemaining()) {
            int type = Byte.toUnsignedInt(tlsRecord.get());
            byte ver_major = tlsRecord.get();
            byte ver_minor = tlsRecord.get();
            int recLen = Short.toUnsignedInt(tlsRecord.getShort());
            byte[] newMsgData = null;
            while (tlsRecord.hasRemaining()) {
                ByteBuffer newRecord = ByteBuffer.allocateDirect(netBufferMax);
                switch (type) {
                    case TLS_RECTYPE_CCS:
                    case TLS_RECTYPE_ALERT:
                    case TLS_RECTYPE_APPDATA:
                        
                        
                        break;
                    case TLS_RECTYPE_HANDSHAKE:
                        newMsgData = getHandshakeMessage(tlsRecord);
                        break;
                }

                
                newRecord.put((byte)type);
                newRecord.put(ver_major);
                newRecord.put(ver_minor);
                newRecord.putShort((short)newMsgData.length);

                
                
                newRecord.put(newMsgData);
                newRecord.flip();
                recordList.add(newRecord);
            }
        }

        return recordList;
    }

    private static ByteBuffer createEvilClientHello(int sessIdLen) {
        ByteBuffer newRecord = ByteBuffer.allocateDirect(4096);

        
        
        

        newRecord.put((byte)TLS_RECTYPE_HANDSHAKE);     
        newRecord.putShort((short)0x0301);              
        newRecord.putShort((short)0);                   

        newRecord.putInt(TLS_HS_CLIENT_HELLO << 24);    
        newRecord.putShort((short)0x0301);
        newRecord.putInt((int)(System.currentTimeMillis() / 1000));
        SecureRandom sr = new SecureRandom();
        byte[] randBuf = new byte[28];
        sr.nextBytes(randBuf);
        newRecord.put(randBuf);                         
        newRecord.put((byte)sessIdLen);                 
        if (sessIdLen > 0) {
            byte[] sessId = new byte[sessIdLen];
            sr.nextBytes(sessId);
            newRecord.put(sessId);                      
        }
        newRecord.putShort((short)2);                   
        newRecord.putShort((short)0x002F);              
        newRecord.putShort((short)0x0100);              
        newRecord.putShort((short)5);                   
        newRecord.putShort((short)0xFF01);              
        newRecord.putShort((short)1);
        newRecord.put((byte)0);                         

        
        
        int recordLength = newRecord.position();
        newRecord.putShort(3, (short)(recordLength - 5));
        int newTypeAndLen = (newRecord.getInt(5) & 0xFF000000) |
                ((recordLength - 9) & 0x00FFFFFF);
        newRecord.putInt(5, newTypeAndLen);

        newRecord.flip();
        return newRecord;
    }

    private static ByteBuffer createEvilServerHello(ByteBuffer origHello,
            int newSessIdLen) {
        if (newSessIdLen < 0 || newSessIdLen > Byte.MAX_VALUE) {
            throw new RuntimeException("Length must be 0 <= X <= 127");
        }

        ByteBuffer newRecord = ByteBuffer.allocateDirect(4096);
        
        
        
        
        
        ByteBuffer scratchBuffer = origHello.slice();
        scratchBuffer.limit(43);
        newRecord.put(scratchBuffer);

        
        
        origHello.position(43);
        int origIDLen = Byte.toUnsignedInt(origHello.get());
        if (origIDLen > 0) {
            
            origHello.position(origHello.position() + origIDLen);
        }

        
        SecureRandom sr = new SecureRandom();
        byte[] sessId = new byte[newSessIdLen];
        sr.nextBytes(sessId);
        newRecord.put((byte)newSessIdLen);
        newRecord.put(sessId);

        
        
        
        newRecord.put(origHello.slice());

        
        
        int recordLength = newRecord.position();
        newRecord.putShort(3, (short)(recordLength - 5));
        int newTypeAndLen = (newRecord.getInt(5) & 0xFF000000) |
                ((recordLength - 9) & 0x00FFFFFF);
        newRecord.putInt(5, newTypeAndLen);

        newRecord.flip();
        return newRecord;
    }

    
    private boolean isTlsMessage(ByteBuffer srcRecord, int reqRecType,
            int... recParams) {
        boolean foundMsg = false;

        if (srcRecord.hasRemaining()) {
            srcRecord.mark();

            
            int recordType = Byte.toUnsignedInt(srcRecord.get());
            byte ver_major = srcRecord.get();
            byte ver_minor = srcRecord.get();
            int recLen = Short.toUnsignedInt(srcRecord.getShort());

            if (recordType == reqRecType) {
                
                
                if (recParams.length == 0) {
                    foundMsg = true;
                } else {
                    switch (recordType) {
                        case TLS_RECTYPE_CCS:
                        case TLS_RECTYPE_APPDATA:
                            
                            
                            
                            foundMsg = true;
                            break;
                        case TLS_RECTYPE_ALERT:
                            
                            if (recParams.length != 2) {
                                throw new RuntimeException(
                                    "Test for Alert requires level and desc.");
                            } else {
                                int level = Byte.toUnsignedInt(srcRecord.get());
                                int desc = Byte.toUnsignedInt(srcRecord.get());
                                if (level == recParams[0] &&
                                        desc == recParams[1]) {
                                    foundMsg = true;
                                }
                            }
                            break;
                        case TLS_RECTYPE_HANDSHAKE:
                            
                            if (recParams.length != 1) {
                                throw new RuntimeException(
                                    "Test for Handshake requires only HS type");
                            } else {
                                
                                
                                
                                
                                int msgHdr = srcRecord.getInt();
                                int msgType = (msgHdr >> 24) & 0x000000FF;
                                if (msgType == recParams[0]) {
                                foundMsg = true;
                            }
                        }
                        break;
                    }
                }
            }

            srcRecord.reset();
        }

        return foundMsg;
    }

    private byte[] getHandshakeMessage(ByteBuffer srcRecord) {
        
        
        
        srcRecord.mark();
        int msgHdr = srcRecord.getInt();
        int type = (msgHdr >> 24) & 0x000000FF;
        int length = msgHdr & 0x00FFFFFF;

        
        
        byte[] data = new byte[length + 4];
        srcRecord.reset();
        srcRecord.get(data, 0, length + 4);

        return (data);
    }

    
    private static void dumpByteBuffer(String header, ByteBuffer bBuf) {
        if (dumpBufs == false) {
            return;
        }

        int bufLen = bBuf.remaining();
        if (bufLen > 0) {
            bBuf.mark();

            
            
            int type = Byte.toUnsignedInt(bBuf.get());
            int ver_major = Byte.toUnsignedInt(bBuf.get());
            int ver_minor = Byte.toUnsignedInt(bBuf.get());
            int recLen = Short.toUnsignedInt(bBuf.getShort());

            log("===== " + header + " (" + tlsRecType(type) + " / " +
                ver_major + "." + ver_minor + " / " + bufLen + " bytes) =====");
            bBuf.reset();
            for (int i = 0; i < bufLen; i++) {
                if (i != 0 && i % 16 == 0) {
                    System.out.print("\n");
                }
                System.out.format("%02X ", bBuf.get(i));
            }
            log("\n===============================================");
            bBuf.reset();
        }
    }

    private static String tlsRecType(int type) {
        switch (type) {
            case 20:
                return "Change Cipher Spec";
            case 21:
                return "Alert";
            case 22:
                return "Handshake";
            case 23:
                return "Application Data";
            default:
                return ("Unknown (" + type + ")");
        }
    }
}
