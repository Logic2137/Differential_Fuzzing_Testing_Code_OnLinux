import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.net.*;
import javax.net.ssl.*;

public class SSLSocketInconsistentSNI {

    static boolean separateServerThread = true;

    static String pathToStores = "../etc";

    static String keyStoreFile = "keystore";

    static String trustStoreFile = "truststore";

    static String passwd = "passphrase";

    volatile static boolean serverReady = false;

    static boolean debug = false;

    void doServerSide() throws Exception {
        SSLServerSocketFactory sslssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslssf.createServerSocket(serverPort);
        SNIMatcher matcher = SNIHostName.createSNIMatcher(serverAcceptableHostname);
        Collection<SNIMatcher> matchers = new ArrayList<>(1);
        matchers.add(matcher);
        SSLParameters params = sslServerSocket.getSSLParameters();
        params.setSNIMatchers(matchers);
        sslServerSocket.setSSLParameters(params);
        serverPort = sslServerSocket.getLocalPort();
        serverReady = true;
        SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
        try {
            InputStream sslIS = sslSocket.getInputStream();
            OutputStream sslOS = sslSocket.getOutputStream();
            sslIS.read();
            sslOS.write(85);
            sslOS.flush();
            throw new Exception("Mismatched server name indication was accepted");
        } catch (SSLHandshakeException sslhe) {
        } catch (IOException ioe) {
        } finally {
            sslSocket.close();
            sslServerSocket.close();
        }
    }

    void doClientSide() throws Exception {
        while (!serverReady) {
            Thread.sleep(50);
        }
        SSLSocketFactory sslsf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) sslsf.createSocket("localhost", serverPort);
        SNIHostName serverName = new SNIHostName(clientRequestedHostname);
        List<SNIServerName> serverNames = new ArrayList<>(1);
        serverNames.add(serverName);
        SSLParameters params = sslSocket.getSSLParameters();
        params.setServerNames(serverNames);
        sslSocket.setSSLParameters(params);
        try {
            InputStream sslIS = sslSocket.getInputStream();
            OutputStream sslOS = sslSocket.getOutputStream();
            sslOS.write(280);
            sslOS.flush();
            sslIS.read();
            throw new Exception("Mismatched server name indication was accepted");
        } catch (SSLHandshakeException sslhe) {
        } catch (IOException ioe) {
        } finally {
            sslSocket.close();
        }
    }

    private static String clientRequestedHostname = "www.example.com";

    private static String serverAcceptableHostname = "www\\.example\\.org";

    volatile int serverPort = 0;

    volatile Exception serverException = null;

    volatile Exception clientException = null;

    public static void main(String[] args) throws Exception {
        String keyFilename = System.getProperty("test.src", ".") + "/" + pathToStores + "/" + keyStoreFile;
        String trustFilename = System.getProperty("test.src", ".") + "/" + pathToStores + "/" + trustStoreFile;
        System.setProperty("javax.net.ssl.keyStore", keyFilename);
        System.setProperty("javax.net.ssl.keyStorePassword", passwd);
        System.setProperty("javax.net.ssl.trustStore", trustFilename);
        System.setProperty("javax.net.ssl.trustStorePassword", passwd);
        if (debug)
            System.setProperty("javax.net.debug", "all");
        new SSLSocketInconsistentSNI();
    }

    Thread clientThread = null;

    Thread serverThread = null;

    SSLSocketInconsistentSNI() throws Exception {
        try {
            if (separateServerThread) {
                startServer(true);
                startClient(false);
            } else {
                startClient(true);
                startServer(false);
            }
        } catch (Exception e) {
        }
        if (separateServerThread) {
            serverThread.join();
        } else {
            clientThread.join();
        }
        Exception local;
        Exception remote;
        String whichRemote;
        if (separateServerThread) {
            remote = serverException;
            local = clientException;
            whichRemote = "server";
        } else {
            remote = clientException;
            local = serverException;
            whichRemote = "client";
        }
        if ((local != null) && (remote != null)) {
            System.out.println(whichRemote + " also threw:");
            remote.printStackTrace();
            System.out.println();
            throw local;
        }
        if (remote != null) {
            throw remote;
        }
        if (local != null) {
            throw local;
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
