import java.io.*;
import java.security.Security;
import javax.net.ssl.*;
import java.util.Arrays;

public class UseCipherSuitesOrder {

    static boolean separateServerThread = false;

    static String pathToStores = "../etc";

    static String keyStoreFile = "keystore";

    static String trustStoreFile = "truststore";

    static String passwd = "passphrase";

    volatile static boolean serverReady = false;

    static boolean debug = false;

    void doServerSide() throws Exception {
        SSLServerSocketFactory sslssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslssf.createServerSocket(serverPort);
        serverPort = sslServerSocket.getLocalPort();
        SSLParameters params = sslServerSocket.getSSLParameters();
        params.setUseCipherSuitesOrder(true);
        params.setCipherSuites(srvEnabledCipherSuites);
        sslServerSocket.setSSLParameters(params);
        serverReady = true;
        SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
        InputStream sslIS = sslSocket.getInputStream();
        OutputStream sslOS = sslSocket.getOutputStream();
        sslIS.read();
        sslOS.write(85);
        sslOS.flush();
        SSLSession session = sslSocket.getSession();
        if (!srvEnabledCipherSuites[0].equals(session.getCipherSuite())) {
            throw new Exception("Expected to negotiate " + srvEnabledCipherSuites[0] + " , but not " + session.getCipherSuite());
        }
        sslSocket.close();
    }

    void doClientSide() throws Exception {
        while (!serverReady) {
            Thread.sleep(50);
        }
        SSLSocketFactory sslsf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) sslsf.createSocket("localhost", serverPort);
        sslSocket.setEnabledCipherSuites(cliEnabledCipherSuites);
        InputStream sslIS = sslSocket.getInputStream();
        OutputStream sslOS = sslSocket.getOutputStream();
        sslOS.write(280);
        sslOS.flush();
        sslIS.read();
        sslSocket.close();
    }

    private static String[] cliEnabledCipherSuites;

    private static String[] srvEnabledCipherSuites;

    private static void parseArguments(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java UseCipherSuitesOrder ciphersuites");
            System.out.println("\tciphersuites: " + "a list of enabled cipher suites, separated with comma");
            throw new Exception("Incorrect usage");
        }
        cliEnabledCipherSuites = args[0].split(",");
        if (cliEnabledCipherSuites.length < 2) {
            throw new Exception("Need to enable at least two cipher suites");
        }
        srvEnabledCipherSuites = Arrays.<String>copyOf(cliEnabledCipherSuites, 2);
        srvEnabledCipherSuites[0] = cliEnabledCipherSuites[1];
        srvEnabledCipherSuites[1] = cliEnabledCipherSuites[0];
    }

    volatile int serverPort = 0;

    volatile Exception serverException = null;

    volatile Exception clientException = null;

    public static void main(String[] args) throws Exception {
        Security.setProperty("jdk.tls.disabledAlgorithms", "");
        parseArguments(args);
        String keyFilename = System.getProperty("test.src", ".") + "/" + pathToStores + "/" + keyStoreFile;
        String trustFilename = System.getProperty("test.src", ".") + "/" + pathToStores + "/" + trustStoreFile;
        System.setProperty("javax.net.ssl.keyStore", keyFilename);
        System.setProperty("javax.net.ssl.keyStorePassword", passwd);
        System.setProperty("javax.net.ssl.trustStore", trustFilename);
        System.setProperty("javax.net.ssl.trustStorePassword", passwd);
        if (debug)
            System.setProperty("javax.net.debug", "all");
        new UseCipherSuitesOrder();
    }

    Thread clientThread = null;

    Thread serverThread = null;

    UseCipherSuitesOrder() throws Exception {
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
