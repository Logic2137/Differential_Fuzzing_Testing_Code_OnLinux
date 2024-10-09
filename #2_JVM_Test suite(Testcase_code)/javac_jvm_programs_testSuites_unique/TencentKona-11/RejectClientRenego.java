






import java.io.*;
import java.net.*;
import java.security.Security;
import javax.net.ssl.*;

public class RejectClientRenego implements
        HandshakeCompletedListener {

    static byte handshakesCompleted = 0;

    
    public void handshakeCompleted(HandshakeCompletedEvent event) {
        synchronized (this) {
            handshakesCompleted++;
            System.out.println("Session: " + event.getSession().toString());
            System.out.println("Seen handshake completed #" +
                handshakesCompleted);
        }
    }

    

    
    static boolean separateServerThread = false;

    
    static String pathToStores = "../../../../javax/net/ssl/etc";
    static String keyStoreFile = "keystore";
    static String trustStoreFile = "truststore";
    static String passwd = "passphrase";

    
    volatile static boolean serverReady = false;

    
    static boolean debug = false;

    

    
    void doServerSide() throws Exception {
        SSLServerSocketFactory sslssf =
            (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket =
            (SSLServerSocket) sslssf.createServerSocket(serverPort);

        serverPort = sslServerSocket.getLocalPort();

        
        serverReady = true;

        SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
        sslSocket.setEnabledProtocols(new String[] { tlsProtocol });
        sslSocket.addHandshakeCompletedListener(this);
        InputStream sslIS = sslSocket.getInputStream();
        OutputStream sslOS = sslSocket.getOutputStream();

        for (int i = 0; i < 10; i++) {
            sslIS.read();
            sslOS.write(85);
            sslOS.flush();
        }

        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("sending/receiving data, iteration: " + i);
                sslIS.read();
                sslOS.write(85);
                sslOS.flush();
            }
            throw new Exception("Not reject client initialized renegotiation");
        } catch (IOException ioe) {
            System.out.println("Got the expected exception");
        } finally {
            sslSocket.close();
        }
    }

    
    void doClientSide() throws Exception {

        
        while (!serverReady) {
            Thread.sleep(50);
        }

        SSLSocketFactory sslsf =
            (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket)
            sslsf.createSocket("localhost", serverPort);
        sslSocket.setEnabledProtocols(new String[] { tlsProtocol });

        InputStream sslIS = sslSocket.getInputStream();
        OutputStream sslOS = sslSocket.getOutputStream();

        for (int i = 0; i < 10; i++) {
            sslOS.write(280);
            sslOS.flush();
            sslIS.read();
        }

        if (!isAbbreviated) {
            System.out.println("invalidating");
            sslSocket.getSession().invalidate();
        }
        System.out.println("starting new handshake");
        sslSocket.startHandshake();

        try {
            for (int i = 0; i < 10; i++) {
                sslOS.write(280);
                sslOS.flush();
                sslIS.read();
            }
            throw new Exception("Not reject client initialized renegotiation");
        } catch (IOException ioe) {
            System.out.println("Got the expected exception");
        } finally {
            sslSocket.close();
        }
    }

    

    
    volatile int serverPort = 0;

    volatile Exception serverException = null;
    volatile Exception clientException = null;

    
    private static boolean isAbbreviated = false;

    
    private static String tlsProtocol;

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

        
        System.setProperty(
            "jdk.tls.rejectClientInitiatedRenegotiation", "true");

        if (debug) {
            System.setProperty("javax.net.debug", "all");
        }

        Security.setProperty("jdk.tls.disabledAlgorithms", "");

        
        if ("true".equals(args[0])) {
            isAbbreviated = true;
        }

        tlsProtocol = args[1];

        
        new RejectClientRenego();
    }

    Thread clientThread = null;
    Thread serverThread = null;

    
    RejectClientRenego() throws Exception {
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
