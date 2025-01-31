






import java.io.*;
import java.net.*;
import java.util.*;
import java.security.*;
import javax.net.ssl.*;
import java.security.cert.*;

public class TestEnabledProtocols {

    

    static final String[][] protocolStrings = {
                                {"TLSv1"},
                                {"TLSv1", "SSLv2Hello"},
                                {"TLSv1", "SSLv3"},
                                {"SSLv3", "SSLv2Hello"},
                                {"SSLv3"},
                                {"TLSv1", "SSLv3", "SSLv2Hello"}
                                };

    static final boolean [][] eXceptionArray = {
        
        { false, true,  false, true,  true,  true }, 
        { false, false, false, true,  true,  false}, 
        { false, true,  false, true,  false, true }, 
        { true,  true,  false, false, false, false}, 
        { true,  true,  false, true,  false, true }, 
        { false, false, false, false, false, false } 
        };

    static final String[][] protocolSelected = {
        
        { "TLSv1",  null,   "TLSv1",  null,   null,     null },

        
        { "TLSv1", "TLSv1", "TLSv1",  null,   null,    "TLSv1"},

        
        { "TLSv1",  null,   "TLSv1",  null,   "SSLv3",  null },

        
        {  null,    null,   "SSLv3", "SSLv3", "SSLv3",  "SSLv3"},

        
        {  null,    null,   "SSLv3",  null,   "SSLv3",  null },

        
        { "TLSv1", "TLSv1", "TLSv1", "SSLv3", "SSLv3", "TLSv1" }

    };

    
    final static String pathToStores = "../etc";
    static String passwd = "passphrase";
    static String keyStoreFile = "keystore";
    static String trustStoreFile = "truststore";

    
    volatile static boolean serverReady = false;

    
    final static boolean debug = false;

    
    volatile int serverPort = 0;

    volatile Exception clientException = null;

    public static void main(String[] args) throws Exception {
        
        
        Security.setProperty("jdk.tls.disabledAlgorithms", "");

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

        if (debug)
            System.setProperty("javax.net.debug", "all");

        new TestEnabledProtocols();
    }

    TestEnabledProtocols() throws Exception  {
        
        SSLServerSocketFactory sslssf =
            (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket =
            (SSLServerSocket) sslssf.createServerSocket(serverPort);
        serverPort = sslServerSocket.getLocalPort();
        

        for (int i = 0; i < protocolStrings.length; i++) {
            String [] serverProtocols = protocolStrings[i];
            startServer ss = new startServer(serverProtocols,
                sslServerSocket, protocolStrings.length);
            ss.setDaemon(true);
            ss.start();
            for (int j = 0; j < protocolStrings.length; j++) {
                String [] clientProtocols = protocolStrings[j];
                startClient sc = new startClient(
                    clientProtocols, serverProtocols,
                    eXceptionArray[i][j], protocolSelected[i][j]);
                sc.start();
                sc.join();
                if (clientException != null) {
                    ss.requestStop();
                    throw clientException;
                }
            }
            ss.requestStop();
            System.out.println("Waiting for the server to complete");
            ss.join();
        }
    }

    class startServer extends Thread  {
        private String[] enabledP = null;
        SSLServerSocket sslServerSocket = null;
        int numExpConns;
        volatile boolean stopRequested = false;

        public startServer(String[] enabledProtocols,
                            SSLServerSocket sslServerSocket,
                            int numExpConns) {
            super("Server Thread");
            serverReady = false;
            enabledP = enabledProtocols;
            this.sslServerSocket = sslServerSocket;
            sslServerSocket.setEnabledProtocols(enabledP);
            this.numExpConns = numExpConns;
        }

        public void requestStop() {
            stopRequested = true;
        }

        public void run() {
            int conns = 0;
            while (!stopRequested) {
                SSLSocket socket = null;
                try {
                    serverReady = true;
                    socket = (SSLSocket)sslServerSocket.accept();
                    conns++;

                    
                    
                    serverReady = false;
                    socket.startHandshake();
                    SSLSession session = socket.getSession();
                    session.invalidate();

                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream();
                    out.write(280);
                    in.read();

                    socket.close();
                    
                    
                    Thread.sleep(30);
                } catch (SSLHandshakeException se) {
                    
                    
                    System.out.println("Server SSLHandshakeException:");
                    se.printStackTrace(System.out);
                } catch (java.io.InterruptedIOException ioe) {
                    
                    break;
                } catch (java.lang.InterruptedException ie) {
                    
                    break;
                } catch (Exception e) {
                    System.out.println("Server exception:");
                    e.printStackTrace(System.out);
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (IOException e) {
                        
                    }
                }
                if (conns >= numExpConns) {
                    break;
                }
            }
        }
    }

    private static void showProtocols(String name, String[] protocols) {
        System.out.println("Enabled protocols on the " + name + " are: " + Arrays.asList(protocols));
    }

    class startClient extends Thread {
        boolean hsCompleted = false;
        boolean exceptionExpected = false;
        private String[] enabledP = null;
        private String[] serverP = null; 
        private String protocolToUse = null;

        startClient(String[] enabledProtocol,
                    String[] serverP,
                    boolean eXception,
                    String protocol) throws Exception {
            super("Client Thread");
            this.enabledP = enabledProtocol;
            this.serverP = serverP;
            this.exceptionExpected = eXception;
            this.protocolToUse = protocol;
        }

        public void run() {
            SSLSocket sslSocket = null;
            try {
                while (!serverReady) {
                    Thread.sleep(50);
                }
                System.out.flush();
                System.out.println("=== Starting new test run ===");
                showProtocols("server", serverP);
                showProtocols("client", enabledP);

                SSLSocketFactory sslsf =
                    (SSLSocketFactory)SSLSocketFactory.getDefault();
                sslSocket = (SSLSocket)
                    sslsf.createSocket("localhost", serverPort);
                sslSocket.setEnabledProtocols(enabledP);
                sslSocket.startHandshake();

                SSLSession session = sslSocket.getSession();
                session.invalidate();
                String protocolName = session.getProtocol();
                System.out.println("Protocol name after getSession is " +
                    protocolName);

                if (protocolName.equals(protocolToUse)) {
                    System.out.println("** Success **");
                } else {
                    System.out.println("** FAILURE ** ");
                    throw new RuntimeException
                        ("expected protocol " + protocolToUse +
                         " but using " + protocolName);
                }

                InputStream in = sslSocket.getInputStream();
                OutputStream out = sslSocket.getOutputStream();
                in.read();
                out.write(280);

                sslSocket.close();

            } catch (SSLHandshakeException e) {
                if (!exceptionExpected) {
                    System.out.println("Client got UNEXPECTED SSLHandshakeException:");
                    e.printStackTrace(System.out);
                    System.out.println("** FAILURE **");
                    clientException = e;
                } else {
                    System.out.println("Client got expected SSLHandshakeException:");
                    e.printStackTrace(System.out);
                    System.out.println("** Success **");
                }
            } catch (RuntimeException e) {
                clientException = e;
            } catch (Exception e) {
                System.out.println("Client got UNEXPECTED Exception:");
                e.printStackTrace(System.out);
                System.out.println("** FAILURE **");
                clientException = e;
            }
        }
    }

}
