import java.io.*;
import java.net.*;
import java.util.*;
import java.security.*;
import javax.net.ssl.*;

public class SetSoTimeout {

    static boolean separateServerThread = true;

    static String pathToStores = "../../../../javax/net/ssl/etc";

    static String keyStoreFile = "keystore";

    static String trustStoreFile = "truststore";

    static String passwd = "passphrase";

    volatile static boolean serverReady = false;

    static boolean debug = false;

    void doServerSide() throws Exception {
        SSLServerSocketFactory sslssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslssf.createServerSocket(serverPort);
        serverPort = sslServerSocket.getLocalPort();
        serverReady = true;
        SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
        Thread.sleep(500);
        sslSocket.close();
    }

    void doClientSide() throws Exception {
        boolean caught = false;
        while (!serverReady) {
            Thread.sleep(50);
        }
        SSLSocketFactory sslsf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) sslsf.createSocket(InetAddress.getLocalHost(), serverPort);
        sslSocket.setSoTimeout(100);
        sslSocket.getSession();
        if (!sslSocket.isClosed()) {
            throw new RuntimeException("Expected connection to be closed");
        }
        sslSocket.close();
    }

    volatile int serverPort = 0;

    volatile Exception serverException = null;

    volatile Exception clientException = null;

    public static void main(String[] args) throws Exception {
        String keyFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + keyStoreFile;
        String trustFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + trustStoreFile;
        System.setProperty("javax.net.ssl.keyStore", keyFilename);
        System.setProperty("javax.net.ssl.keyStorePassword", passwd);
        System.setProperty("javax.net.ssl.trustStore", trustFilename);
        System.setProperty("javax.net.ssl.trustStorePassword", passwd);
        if (debug)
            System.setProperty("javax.net.debug", "all");
        new SetSoTimeout();
    }

    Thread clientThread = null;

    Thread serverThread = null;

    SetSoTimeout() throws Exception {
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
