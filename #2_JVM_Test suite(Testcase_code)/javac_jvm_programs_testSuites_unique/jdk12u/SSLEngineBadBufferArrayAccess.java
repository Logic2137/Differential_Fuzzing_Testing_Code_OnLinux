









import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.nio.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SSLEngineBadBufferArrayAccess {

    
    private static boolean logging = true;

    
    private static boolean debug = false;
    private SSLContext sslc;
    private SSLEngine serverEngine;     

    private final byte[] serverMsg = "Hi there Client, I'm a Server".getBytes();
    private final byte[] clientMsg = "Hello Server, I'm a Client".getBytes();

    private ByteBuffer serverOut;       
    private ByteBuffer serverIn;        

    private volatile Exception clientException;
    private volatile Exception serverException;

    
    private ByteBuffer cTOs;            
    private ByteBuffer sTOc;            

    
    private static final String pathToStores = "../../../../javax/net/ssl/etc";
    private static final String keyStoreFile = "keystore";
    private static final String trustStoreFile = "truststore";
    private static final String passwd = "passphrase";
    private static String keyFilename =
            System.getProperty("test.src", ".") + "/" + pathToStores
            + "/" + keyStoreFile;
    private static String trustFilename =
            System.getProperty("test.src", ".") + "/" + pathToStores
            + "/" + trustStoreFile;

    
    private static final CountDownLatch serverCondition = new CountDownLatch(1);

    
    private static final CountDownLatch clientCondition = new CountDownLatch(1);

    
    private volatile int serverPort = 0;

    
    public static void main(String args[]) throws Exception {
        if (debug) {
            System.setProperty("javax.net.debug", "all");
        }

        String [] protocols = new String [] {
            "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2" };

        for (String protocol : protocols) {
            
            log("Testing " + protocol + ":true");
            new SSLEngineBadBufferArrayAccess(protocol).runTest(true);

            log("Testing " + protocol + ":false");
            new SSLEngineBadBufferArrayAccess(protocol).runTest(false);
        }

        System.out.println("Test Passed.");
    }

    
    public SSLEngineBadBufferArrayAccess(String protocol) throws Exception {

        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore ts = KeyStore.getInstance("JKS");

        char[] passphrase = "passphrase".toCharArray();

        try (FileInputStream fis = new FileInputStream(keyFilename)) {
            ks.load(fis, passphrase);
        }

        try (FileInputStream fis = new FileInputStream(trustFilename)) {
            ts.load(fis, passphrase);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);

        SSLContext sslCtx = SSLContext.getInstance(protocol);

        sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        sslc = sslCtx;
    }

    
    private void runTest(boolean direct) throws Exception {
        boolean serverClose = direct;

        ServerSocket serverSocket = new ServerSocket(0);
        serverPort = serverSocket.getLocalPort();

        
        serverCondition.countDown();

        Thread clientThread = runClient(serverClose);

        
        Socket socket;
        try {
            serverSocket.setSoTimeout(30000);
            socket = (Socket) serverSocket.accept();
        } catch (SocketTimeoutException ste) {
            serverSocket.close();

            
            System.out.println(
                "No incoming client connection in 30 seconds. " +
                "Ignore in server side.");
            return;
        }

        
        try {
            
            
            
            
            
            
            
            
            boolean clientIsReady =
                    clientCondition.await(30L, TimeUnit.SECONDS);

            if (clientIsReady) {
                
                runServerApplication(socket, direct, serverClose);
            } else {    
                
                
                
                
                
                System.out.println(
                        "The client is not the expected one or timeout. " +
                        "Ignore in server side.");
            }
        } catch (Exception e) {
            System.out.println("Server died ...");
            e.printStackTrace(System.out);
            serverException = e;
        } finally {
            socket.close();
            serverSocket.close();
        }

        clientThread.join();

        if (clientException != null || serverException != null) {
            throw new RuntimeException("Test failed");
        }
    }

    
    void runServerApplication(Socket socket, boolean direct,
            boolean serverClose) throws Exception {

        socket.setSoTimeout(500);

        createSSLEngine();
        createBuffers(direct);

        boolean closed = false;

        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        SSLEngineResult serverResult;   

        
        byte[] inbound = new byte[8192];
        byte[] outbound = new byte[8192];

        while (!isEngineClosed(serverEngine)) {
            int len = 0;

            
            log("================");

            
            try {
                len = is.read(inbound);
                if (len == -1) {
                    throw new Exception("Unexpected EOF");
                }
                cTOs.put(inbound, 0, len);
            } catch (SocketTimeoutException ste) {
                
                System.out.println("Warning: " + ste);
            }

            cTOs.flip();

            serverResult = serverEngine.unwrap(cTOs, serverIn);
            log("server unwrap: ", serverResult);
            runDelegatedTasks(serverResult, serverEngine);
            cTOs.compact();

            
            log("----");

            serverResult = serverEngine.wrap(serverOut, sTOc);
            log("server wrap: ", serverResult);
            runDelegatedTasks(serverResult, serverEngine);

            sTOc.flip();

            if ((len = sTOc.remaining()) != 0) {
                sTOc.get(outbound, 0, len);
                os.write(outbound, 0, len);
                
            }

            sTOc.compact();

            if (!closed && (serverOut.remaining() == 0)) {
                closed = true;

                
                if (serverClose) {
                    serverEngine.closeOutbound();
                }
            }

            if (closed && isEngineClosed(serverEngine)) {
                serverIn.flip();

                
                if (serverIn.remaining() != clientMsg.length) {
                    throw new Exception("Client: Data length error -" +
                        " IF THIS FAILS, PLEASE REPORT THIS TO THE" +
                        " SECURITY TEAM.  WE HAVE BEEN UNABLE TO" +
                        " RELIABLY DUPLICATE.");
                }

                for (int i = 0; i < clientMsg.length; i++) {
                    if (clientMsg[i] != serverIn.get()) {
                        throw new Exception("Client: Data content error -" +
                        " IF THIS FAILS, PLEASE REPORT THIS TO THE" +
                        " SECURITY TEAM.  WE HAVE BEEN UNABLE TO" +
                        " RELIABLY DUPLICATE.");
                    }
                }
                serverIn.compact();
            }
        }
    }

    
    private Thread runClient(final boolean serverClose)
            throws Exception {

        Thread t = new Thread("ClientThread") {

            @Override
            public void run() {
                try {
                    doClientSide(serverClose);
                } catch (Exception e) {
                    System.out.println("Client died ...");
                    e.printStackTrace(System.out);
                    clientException = e;
                }
            }
        };

        t.start();
        return t;
    }

    
    void doClientSide(boolean serverClose) throws Exception {
        
        
        
        
        
        boolean serverIsReady =
                serverCondition.await(90L, TimeUnit.SECONDS);
        if (!serverIsReady) {
            System.out.println(
                    "The server is not ready yet in 90 seconds. " +
                    "Ignore in client side.");
            return;
        }

        SSLSocketFactory sslsf = sslc.getSocketFactory();
        try (SSLSocket sslSocket = (SSLSocket)sslsf.createSocket()) {
            try {
                sslSocket.connect(
                        new InetSocketAddress("localhost", serverPort), 15000);
            } catch (IOException ioe) {
                
                
                
                
                
                System.out.println(
                        "Cannot make a connection in 15 seconds. " +
                        "Ignore in client side.");
                return;
            }

            

            
            clientCondition.countDown();

            
            
            
            

            
            runClientApplication(sslSocket, serverClose);
        }
    }

    
    void runClientApplication(SSLSocket sslSocket, boolean serverClose)
            throws Exception {

        OutputStream os = sslSocket.getOutputStream();
        InputStream is = sslSocket.getInputStream();

        
        os.write(clientMsg);

        byte[] inbound = new byte[2048];
        int pos = 0;

        int len;
        while ((len = is.read(inbound, pos, 2048 - pos)) != -1) {
            pos += len;
            
            if ((pos == serverMsg.length) && !serverClose) {
                sslSocket.close();
                break;
            }
        }

        if (pos != serverMsg.length) {
            throw new Exception("Client:  Data length error");
        }

        for (int i = 0; i < serverMsg.length; i++) {
            if (inbound[i] != serverMsg[i]) {
                throw new Exception("Client:  Data content error");
            }
        }
    }

    
    private void createSSLEngine() throws Exception {
        
        serverEngine = sslc.createSSLEngine();
        serverEngine.setUseClientMode(false);
        serverEngine.getNeedClientAuth();
    }

    
    private void createBuffers(boolean direct) {

        SSLSession session = serverEngine.getSession();
        int appBufferMax = session.getApplicationBufferSize();
        int netBufferMax = session.getPacketBufferSize();

        
        if (direct) {
            serverIn = ByteBuffer.allocateDirect(appBufferMax + 50);
            cTOs = ByteBuffer.allocateDirect(netBufferMax);
            sTOc = ByteBuffer.allocateDirect(netBufferMax);
        } else {
            serverIn = ByteBuffer.allocate(appBufferMax + 50);
            cTOs = ByteBuffer.allocate(netBufferMax);
            sTOc = ByteBuffer.allocate(netBufferMax);
        }

        serverOut = ByteBuffer.wrap(serverMsg);
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

    
    private static boolean resultOnce = true;

    private static void log(String str, SSLEngineResult result) {
        if (!logging) {
            return;
        }
        if (resultOnce) {
            resultOnce = false;
            System.out.println("The format of the SSLEngineResult is: \n"
                    + "\t\"getStatus() / getHandshakeStatus()\" +\n"
                    + "\t\"bytesConsumed() / bytesProduced()\"\n");
        }
        HandshakeStatus hsStatus = result.getHandshakeStatus();
        log(str
                + result.getStatus() + "/" + hsStatus + ", "
                + result.bytesConsumed() + "/" + result.bytesProduced()
                + " bytes");
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
