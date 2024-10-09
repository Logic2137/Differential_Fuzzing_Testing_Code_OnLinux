




import java.io.*;
import java.net.*;
import java.util.*;
import java.security.*;
import javax.net.ssl.*;

public class SSLSocketCloseHang {
    

    
    static boolean separateServerThread = true;

    
    static String pathToStores = "../../../../javax/net/ssl/etc";
    static String keyStoreFile = "keystore";
    static String trustStoreFile = "truststore";
    static String passwd = "passphrase";

    
    volatile static boolean serverReady = false;

    
    volatile static boolean clientClosed = false;

    
    static boolean debug = false;

    static String socketCloseType;

    

    
    void doServerSide() throws Exception {
        SSLServerSocketFactory sslssf =
            (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket =
            (SSLServerSocket) sslssf.createServerSocket(serverPort);

        serverPort = sslServerSocket.getLocalPort();

        
        serverReady = true;

        System.err.println("Server accepting: " + System.nanoTime());
        SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
        System.err.println("Server accepted: " + System.nanoTime());
        sslSocket.startHandshake();
        System.err.println("Server handshake complete: " + System.nanoTime());
        while (!clientClosed) {
            Thread.sleep(500);
        }
    }

    
    void doClientSide() throws Exception {
        boolean caught = false;

        
        System.out.println("waiting on server");
        while (!serverReady) {
            Thread.sleep(50);
        }
        Thread.sleep(500);
        System.out.println("server ready");

        Socket baseSocket = new Socket("localhost", serverPort);
        baseSocket.setSoTimeout(1000);

        SSLSocketFactory sslsf =
            (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket)
            sslsf.createSocket(baseSocket, "localhost", serverPort, false);

        
        System.err.println("Client starting handshake: " + System.nanoTime());
        sslSocket.startHandshake();
        System.err.println("Client handshake done: " + System.nanoTime());

        Thread.sleep(500);
        System.err.println("Client closing: " + System.nanoTime());

        closeConnection(sslSocket);

        clientClosed = true;
        System.err.println("Client closed: " + System.nanoTime());
    }

    private void closeConnection(SSLSocket sslSocket) throws IOException {
        if ("shutdownInput".equals(socketCloseType)) {
            shutdownInput(sslSocket);
            
            
            sslSocket.shutdownInput();
            
            sslSocket.shutdownOutput();
        } else if ("shutdownOutput".equals(socketCloseType)) {
            sslSocket.shutdownOutput();
            
            
            sslSocket.shutdownOutput();
            
            shutdownInput(sslSocket);
        } else {
            sslSocket.close();
        }
    }

    private void shutdownInput(SSLSocket sslSocket) throws IOException {
        try {
            sslSocket.shutdownInput();
        } catch (SSLException e) {
            if (!e.getMessage().contains
                    ("closing inbound before receiving peer's close_notify")) {
                throw new RuntimeException("expected different exception "
                        + "message. " + e.getMessage());
            }
        }
        if (!sslSocket.getSession().isValid()) {
            throw new RuntimeException("expected session to remain valid");
        }
    }

    

    
    volatile int serverPort = 0;

    volatile Exception serverException = null;
    volatile Exception clientException = null;

    volatile byte[] serverDigest = null;

    public static void main(String[] args) throws Exception {
        String keyFilename =
            System.getProperty("test.src", "./") + "/" + pathToStores +
                "/" + keyStoreFile;
        String trustFilename =
            System.getProperty("test.src", "./") + "/" + pathToStores +
                "/" + trustStoreFile;

        System.setProperty("javax.net.ssl.keyStore", keyFilename);
        System.setProperty("javax.net.ssl.keyStorePassword", passwd);
        System.setProperty("javax.net.ssl.trustStore", trustFilename);
        System.setProperty("javax.net.ssl.trustStorePassword", passwd);
        System.setProperty("jdk.tls.client.protocols", args[0]);

        if (debug)
            System.setProperty("javax.net.debug", "all");

        socketCloseType = args.length > 1 ? args[1] : "";


        
        new SSLSocketCloseHang();
    }

    Thread clientThread = null;
    Thread serverThread = null;

    
    SSLSocketCloseHang() throws Exception {
        if (separateServerThread) {
            startServer(true);
            startClient(false);
        } else {
            startClient(true);
            startServer(false);
        }

        
        if (separateServerThread) {
            serverThread.join();
        } else {
            clientThread.join();
        }

        
        if (serverException != null) {
            System.out.print("Server Exception:");
            throw serverException;
        }
        if (clientException != null) {
            System.out.print("Client Exception:");
            throw clientException;
        }
    }

    void startServer(boolean newThread) throws Exception {
        if (newThread) {
            serverThread = new Thread() {
                public void run() {
                    try {
                        doServerSide();
                    } catch (Exception e) {
                        
                        System.err.println("Server died...");
                        System.err.println(e);
                        serverReady = true;
                        serverException = e;
                    }
                }
            };
            serverThread.start();
        } else {
            doServerSide();
        }
    }

    void startClient(boolean newThread) throws Exception {
        if (newThread) {
            clientThread = new Thread() {
                public void run() {
                    try {
                        doClientSide();
                    } catch (Exception e) {
                        
                        System.err.println("Client died...");
                        clientException = e;
                    }
                }
            };
            clientThread.start();
        } else {
            doClientSide();
        }
    }
}
