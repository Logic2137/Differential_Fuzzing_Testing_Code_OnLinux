import java.io.*;
import java.net.*;
import javax.net.ssl.*;

public class SSLSocketImplThrowsWrongExceptions {

    static boolean separateServerThread = true;

    static String pathToStores = "../../../../javax/net/ssl/etc";

    static String keyStoreFile = "keystore";

    static String passwd = "passphrase";

    volatile static boolean serverReady = false;

    static boolean debug = false;

    void doServerSide() throws Exception {
        System.out.println("starting Server");
        SSLServerSocketFactory sslssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslssf.createServerSocket(serverPort);
        serverPort = sslServerSocket.getLocalPort();
        System.out.println("got server socket");
        serverReady = true;
        try {
            System.out.println("Server socket accepting...");
            SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
            System.out.println("Server starting handshake");
            sslSocket.startHandshake();
            throw new Exception("Handshake was successful");
        } catch (SSLException e) {
            System.out.println("Server reported the right exception");
            System.out.println(e.toString());
        } catch (Exception e) {
            System.out.println("Server reported the wrong exception");
            throw e;
        }
    }

    void doClientSide() throws Exception {
        System.out.println("    Client starting");
        while (!serverReady) {
            Thread.sleep(50);
        }
        SSLSocketFactory sslsf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try {
            System.out.println("        Client creating socket");
            SSLSocket sslSocket = (SSLSocket) sslsf.createSocket("localhost", serverPort);
            System.out.println("        Client starting handshake");
            sslSocket.startHandshake();
            throw new Exception("Handshake was successful");
        } catch (SSLException e) {
            System.out.println("       Client reported correct exception");
            System.out.println("       " + e.toString());
        } catch (Exception e) {
            System.out.println("        Client reported the wrong exception");
            throw e;
        }
    }

    volatile int serverPort = 0;

    volatile Exception serverException = null;

    volatile Exception clientException = null;

    public static void main(String[] args) throws Exception {
        String keyFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + keyStoreFile;
        System.setProperty("javax.net.ssl.keyStore", keyFilename);
        System.setProperty("javax.net.ssl.keyStorePassword", passwd);
        if (debug)
            System.setProperty("javax.net.debug", "all");
        new SSLSocketImplThrowsWrongExceptions();
    }

    Thread clientThread = null;

    Thread serverThread = null;

    SSLSocketImplThrowsWrongExceptions() throws Exception {
        Exception startException = null;
        try {
            if (separateServerThread) {
                startServer(true);
                startClient(false);
            } else {
                startClient(true);
                startServer(false);
            }
        } catch (Exception e) {
            startException = e;
        }
        if (separateServerThread) {
            if (serverThread != null) {
                serverThread.join();
            }
        } else {
            if (clientThread != null) {
                clientThread.join();
            }
        }
        Exception local;
        Exception remote;
        if (separateServerThread) {
            remote = serverException;
            local = clientException;
        } else {
            remote = clientException;
            local = serverException;
        }
        Exception exception = null;
        if ((local != null) && (remote != null)) {
            local.initCause(remote);
            exception = local;
        } else if (local != null) {
            exception = local;
        } else if (remote != null) {
            exception = remote;
        } else if (startException != null) {
            exception = startException;
        }
        if (exception != null) {
            if (exception != startException && startException != null) {
                exception.addSuppressed(startException);
            }
            throw exception;
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
            try {
                doServerSide();
            } catch (Exception e) {
                serverException = e;
            } finally {
                serverReady = true;
            }
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
            try {
                doClientSide();
            } catch (Exception e) {
                clientException = e;
            }
        }
    }
}
