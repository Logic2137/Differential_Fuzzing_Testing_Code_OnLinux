









import javax.net.ssl.*;
import javax.net.ssl.SSLEngineResult.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.nio.*;

public class SSLSocketSSLEngineTemplate {

    
    private static final boolean logging = true;

    
    private static final boolean debug = false;
    private final SSLContext sslc;
    private SSLEngine serverEngine;     
    private SSLSocket clientSocket;

    private final byte[] serverMsg =
        "Hi there Client, I'm a Server.".getBytes();
    private final byte[] clientMsg =
        "Hello Server, I'm a Client! Pleased to meet you!".getBytes();

    private ByteBuffer serverOut;       
    private ByteBuffer serverIn;        

    private volatile Exception clientException;
    private volatile Exception serverException;

    
    private ByteBuffer cTOs;            
    private ByteBuffer sTOc;            

    
    private static final String pathToStores = "../etc";
    private static final String keyStoreFile = "keystore";
    private static final String trustStoreFile = "truststore";
    private static final String keyFilename =
            System.getProperty("test.src", ".") + "/" + pathToStores
            + "/" + keyStoreFile;
    private static final String trustFilename =
            System.getProperty("test.src", ".") + "/" + pathToStores
            + "/" + trustStoreFile;

    
    public static void main(String args[]) throws Exception {
        String protocol = args[0];

        
        
        Security.setProperty("jdk.tls.disabledAlgorithms", "");
        Security.setProperty("jdk.certpath.disabledAlgorithms", "");

        if (debug) {
            System.setProperty("javax.net.debug", "all");
        }

        
        SSLSocketSSLEngineTemplate test =
            new SSLSocketSSLEngineTemplate(protocol);
        log("-------------------------------------");
        log("Testing " + protocol + " for direct buffers ...");
        test.runTest(true);

        log("---------------------------------------");
        log("Testing " + protocol + " for indirect buffers ...");
        test.runTest(false);

        log("Test Passed.");
    }

    
    public SSLSocketSSLEngineTemplate(String protocol) throws Exception {

        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore ts = KeyStore.getInstance("JKS");

        char[] passphrase = "passphrase".toCharArray();

        try (FileInputStream keyFile = new FileInputStream(keyFilename);
                FileInputStream trustFile = new FileInputStream(trustFilename)) {
            ks.load(keyFile, passphrase);
            ts.load(trustFile, passphrase);
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
        clientSocket = null;
        boolean serverClose = direct;

        
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.setReuseAddress(false);
            serverSocket.bind(null);
            int port = serverSocket.getLocalPort();
            log("Port: " + port);
            Thread thread = createClientThread(port, serverClose);

            createSSLEngine();
            createBuffers(direct);

            
            try (Socket socket = serverSocket.accept()) {
                socket.setSoTimeout(500);

                boolean closed = false;
                
                
                boolean retry = true;

                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                SSLEngineResult serverResult;   

                
                byte[] inbound = new byte[8192];
                byte[] outbound = new byte[8192];

                while (!isEngineClosed(serverEngine)) {
                    int len;

                    
                    log("================");

                    
                    try {
                        len = is.read(inbound);
                        if (len == -1) {
                            logSocketStatus(clientSocket);
                            if (clientSocket.isClosed()
                                    || clientSocket.isOutputShutdown()) {
                                log("Client socket was closed or shutdown output");
                                break;
                            } else {
                                throw new Exception("Unexpected EOF");
                            }
                        }
                        cTOs.put(inbound, 0, len);
                    } catch (SocketTimeoutException ste) {
                        
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
                        serverIn.flip();

                        
                        if (serverIn.remaining() !=  clientMsg.length) {
                            if (retry &&
                                    serverIn.remaining() < clientMsg.length) {
                                log("Need to read more from client");
                                serverIn.compact();
                                retry = false;
                                continue;
                            } else {
                                throw new Exception(
                                        "Client: Data length error");
                            }
                        }

                        for (int i = 0; i < clientMsg.length; i++) {
                            if (clientMsg[i] != serverIn.get()) {
                                throw new Exception(
                                        "Client: Data content error");
                            }
                        }
                        serverIn.compact();
                    }
                }
            } catch (Exception e) {
                serverException = e;
            } finally {
                
                if (thread != null) {
                    thread.join();
                }
            }
        } finally {
            if (serverException != null) {
                if (clientException != null) {
                    serverException.initCause(clientException);
                }
                throw serverException;
            }
            if (clientException != null) {
                if (serverException != null) {
                    clientException.initCause(serverException);
                }
                throw clientException;
            }
        }
    }

    
    private Thread createClientThread(final int port,
            final boolean serverClose) throws Exception {

        Thread t = new Thread("ClientThread") {

            @Override
            public void run() {
                
                try (SSLSocket sslSocket = (SSLSocket)sslc.getSocketFactory().
                            createSocket("localhost", port)) {
                    clientSocket = sslSocket;

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
                } catch (Exception e) {
                    clientException = e;
                }
            }
        };
        t.start();
        return t;
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

    private static void logSocketStatus(Socket socket) {
        log("##### " + socket + " #####");
        log("isBound: " + socket.isBound());
        log("isConnected: " + socket.isConnected());
        log("isClosed: " + socket.isClosed());
        log("isInputShutdown: " + socket.isInputShutdown());
        log("isOutputShutdown: " + socket.isOutputShutdown());
    }

    
    private static boolean resultOnce = true;

    private static void log(String str, SSLEngineResult result) {
        if (!logging) {
            return;
        }
        if (resultOnce) {
            resultOnce = false;
            log("The format of the SSLEngineResult is: \n"
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
            if (debug) {
                System.err.println(str);
            } else {
                System.out.println(str);
            }
        }
    }
}
