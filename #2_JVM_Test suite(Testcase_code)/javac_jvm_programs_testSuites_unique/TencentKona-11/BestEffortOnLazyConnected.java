








import java.io.*;
import java.net.*;
import javax.net.ssl.*;

public class BestEffortOnLazyConnected {

    

    
    private static final boolean separateServerThread = true;

    
    private static final String pathToStores = "../etc";
    private static final String keyStoreFile = "keystore";
    private static final String trustStoreFile = "truststore";
    private static final String passwd = "passphrase";

    
    private static volatile boolean serverReady = false;

    
    private static final boolean debug = false;

    
    private static String hostname = null;

    

    
    private void doServerSide() throws Exception {
        SSLServerSocketFactory sslssf =
            (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        try (SSLServerSocket sslServerSocket =
                (SSLServerSocket) sslssf.createServerSocket(serverPort)) {

            serverPort = sslServerSocket.getLocalPort();

            
            serverReady = true;

            try (SSLSocket sslSocket = (SSLSocket)sslServerSocket.accept()) {
                InputStream sslIS = sslSocket.getInputStream();
                OutputStream sslOS = sslSocket.getOutputStream();

                sslIS.read();
                sslOS.write(85);
                sslOS.flush();

                ExtendedSSLSession session =
                        (ExtendedSSLSession)sslSocket.getSession();
                if (session.getRequestedServerNames().isEmpty()) {
                    throw new Exception("No expected Server Name Indication");
                }
            }
        }
    }

    
    private void doClientSide() throws Exception {

        
        while (!serverReady) {
            Thread.sleep(50);
        }

        SSLSocketFactory sslsf =
            (SSLSocketFactory) SSLSocketFactory.getDefault();

        try (SSLSocket sslSocket = (SSLSocket)sslsf.createSocket()) {

            sslSocket.connect(new InetSocketAddress(hostname, serverPort), 0);

            InputStream sslIS = sslSocket.getInputStream();
            OutputStream sslOS = sslSocket.getOutputStream();

            sslOS.write(280);
            sslOS.flush();
            sslIS.read();
        }
    }


    

    
    private volatile int serverPort = 0;

    private volatile Exception serverException = null;
    private volatile Exception clientException = null;

    public static void main(String[] args) throws Exception {
        String keyFilename =
            System.getProperty("test.src", ".") + "/" + pathToStores +
                "/" + keyStoreFile;
        String trustFilename =
            System.getProperty("test.src", ".") + "/" + pathToStores +
                "/" + trustStoreFile;

        System.setProperty("javax.net.ssl.keyStore", keyFilename);
        System.setProperty("javax.net.ssl.keyStorePassword", passwd);
        System.setProperty("javax.net.ssl.trustStore", trustFilename);
        System.setProperty("javax.net.ssl.trustStorePassword", passwd);

        if (debug) {
            System.setProperty("javax.net.debug", "all");
        }

        try {
            hostname = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException uhe) {
            System.out.println(
                "Ignore the test as the local hostname cannot be determined");

            return;
        }

        System.out.println(
                "The fully qualified domain name of the local host is " +
                hostname);
        
        if ((hostname == null) || hostname.isEmpty() ||
                !hostname.contains(".") || hostname.endsWith(".") ||
                hostname.startsWith("localhost") ||
                Character.isDigit(hostname.charAt(hostname.length() - 1))) {

            System.out.println("Ignore the test as the local hostname " +
                    "cannot be determined as fully qualified domain name");

            return;
        }

        
        new BestEffortOnLazyConnected();
    }

    private Thread clientThread = null;
    private Thread serverThread = null;

    
    BestEffortOnLazyConnected() throws Exception {
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

    private void startServer(boolean newThread) throws Exception {
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

    private void startClient(boolean newThread) throws Exception {
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
