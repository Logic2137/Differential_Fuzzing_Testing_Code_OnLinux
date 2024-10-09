import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.util.*;
import java.security.*;

public class SessionCacheSizeTests {

    static boolean separateServerThread = true;

    static String pathToStores = "../etc";

    static String keyStoreFile = "keystore";

    static String trustStoreFile = "truststore";

    static String passwd = "passphrase";

    volatile static boolean serverReady = false;

    static boolean debug = false;

    static int MAX_ACTIVE_CONNECTIONS = 4;

    static final int FREE_PORT = 0;

    void doServerSide(int serverConns) throws Exception {
        try (SSLServerSocket sslServerSocket = (SSLServerSocket) sslssf.createServerSocket(FREE_PORT)) {
            sslServerSocket.setSoTimeout(45000);
            synchronized (serverPorts) {
                int serverPort = sslServerSocket.getLocalPort();
                System.out.printf("server #%d started on port %d%n", createdPorts, serverPort);
                serverPorts[createdPorts++] = serverPort;
                if (createdPorts == serverPorts.length) {
                    serverReady = true;
                }
            }
            int read = 0;
            int nConnections = 0;
            SSLSession[] sessions = new SSLSession[serverConns];
            SSLSessionContext sessCtx = sslctx.getServerSessionContext();
            while (nConnections < serverConns) {
                try (SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept()) {
                    sslSocket.setSoTimeout(90000);
                    InputStream sslIS = sslSocket.getInputStream();
                    OutputStream sslOS = sslSocket.getOutputStream();
                    read = sslIS.read();
                    sessions[nConnections] = sslSocket.getSession();
                    sslOS.write(85);
                    sslOS.flush();
                    nConnections++;
                }
            }
        }
    }

    void doClientSide() throws Exception {
        while (!serverReady) {
            Thread.sleep(50);
        }
        int nConnections = 0;
        SSLSocket[] sslSockets = new SSLSocket[MAX_ACTIVE_CONNECTIONS];
        Vector sessions = new Vector();
        SSLSessionContext sessCtx = sslctx.getClientSessionContext();
        sessCtx.setSessionTimeout(0);
        try {
            while (nConnections < (MAX_ACTIVE_CONNECTIONS - 1)) {
                int serverPort = serverPorts[nConnections % (serverPorts.length)];
                System.out.printf("client #%d connects to port %d%n", nConnections, serverPort);
                sslSockets[nConnections] = (SSLSocket) sslsf.createSocket("localhost", serverPort);
                InputStream sslIS = sslSockets[nConnections].getInputStream();
                OutputStream sslOS = sslSockets[nConnections].getOutputStream();
                sslOS.write(237);
                sslOS.flush();
                int read = sslIS.read();
                SSLSession sess = sslSockets[nConnections].getSession();
                if (!sessions.contains(sess))
                    sessions.add(sess);
                nConnections++;
            }
            System.out.println("Current cacheSize is set to: " + sessCtx.getSessionCacheSize());
            System.out.println();
            System.out.println("Currently cached Sessions......");
            System.out.println("============================================" + "============================");
            System.out.println("Session                                     " + "      Session-last-accessTime");
            System.out.println("============================================" + "============================");
            checkCachedSessions(sessCtx, nConnections);
            sessCtx.setSessionCacheSize(2);
            System.out.println("Session cache size changed to: " + sessCtx.getSessionCacheSize());
            System.out.println();
            checkCachedSessions(sessCtx, nConnections);
            sessCtx.setSessionCacheSize(3);
            System.out.println("Session cache size changed to: " + sessCtx.getSessionCacheSize());
            int serverPort = serverPorts[nConnections % (serverPorts.length)];
            System.out.printf("client #%d connects to port %d%n", nConnections, serverPort);
            sslSockets[nConnections] = (SSLSocket) sslsf.createSocket("localhost", serverPort);
            InputStream sslIS = sslSockets[nConnections].getInputStream();
            OutputStream sslOS = sslSockets[nConnections].getOutputStream();
            sslOS.write(237);
            sslOS.flush();
            int read = sslIS.read();
            SSLSession sess = sslSockets[nConnections].getSession();
            if (!sessions.contains(sess))
                sessions.add(sess);
            nConnections++;
            checkCachedSessions(sessCtx, nConnections);
        } finally {
            for (int i = 0; i < nConnections; i++) {
                if (sslSockets[i] != null) {
                    sslSockets[i].close();
                }
            }
        }
        System.out.println("Session cache size tests passed");
    }

    void checkCachedSessions(SSLSessionContext sessCtx, int nConn) throws Exception {
        int nSessions = 0;
        Enumeration e = sessCtx.getIds();
        int cacheSize = sessCtx.getSessionCacheSize();
        SSLSession sess;
        while (e.hasMoreElements()) {
            sess = sessCtx.getSession((byte[]) e.nextElement());
            long lastAccessedTime = sess.getLastAccessedTime();
            System.out.println(sess + "       " + new Date(lastAccessedTime));
            nSessions++;
        }
        System.out.println("--------------------------------------------" + "----------------------------");
        if ((cacheSize > 0) && (nSessions > cacheSize)) {
            for (int conn = nConn; conn < MAX_ACTIVE_CONNECTIONS; conn++) {
                SSLSocket s = (SSLSocket) sslsf.createSocket("localhost", serverPorts[conn % (serverPorts.length)]);
                s.close();
            }
            throw new Exception("Session cache size test failed," + " current cache size: " + cacheSize + " #sessions cached: " + nSessions);
        }
    }

    int[] serverPorts = new int[] { 0, 0, 0, 0 };

    int createdPorts = 0;

    static SSLServerSocketFactory sslssf;

    static SSLSocketFactory sslsf;

    static SSLContext sslctx;

    volatile Exception serverException = null;

    volatile Exception clientException = null;

    public static void main(String[] args) throws Exception {
        String keyFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + keyStoreFile;
        String trustFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + trustStoreFile;
        System.setProperty("javax.net.ssl.keyStore", keyFilename);
        System.setProperty("javax.net.ssl.keyStorePassword", passwd);
        System.setProperty("javax.net.ssl.trustStore", trustFilename);
        System.setProperty("javax.net.ssl.trustStorePassword", passwd);
        System.setProperty("javax.net.ssl.sessionCacheSize", String.valueOf(0));
        sslctx = SSLContext.getInstance("TLS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keyFilename)) {
            ks.load(fis, passwd.toCharArray());
        }
        kmf.init(ks, passwd.toCharArray());
        sslctx.init(kmf.getKeyManagers(), null, null);
        sslssf = (SSLServerSocketFactory) sslctx.getServerSocketFactory();
        sslsf = (SSLSocketFactory) sslctx.getSocketFactory();
        if (debug)
            System.setProperty("javax.net.debug", "all");
        new SessionCacheSizeTests();
    }

    Thread clientThread = null;

    Thread serverThread = null;

    SessionCacheSizeTests() throws Exception {
        int serverConns = MAX_ACTIVE_CONNECTIONS / (serverPorts.length);
        int remainingConns = MAX_ACTIVE_CONNECTIONS % (serverPorts.length);
        Exception startException = null;
        try {
            if (separateServerThread) {
                for (int i = 0; i < serverPorts.length; i++) {
                    if (i < remainingConns) {
                        startServer(serverConns + 1, true);
                    } else {
                        startServer(serverConns, true);
                    }
                }
                startClient(false);
            } else {
                startClient(true);
                for (int i = 0; i < serverPorts.length; i++) {
                    if (i < remainingConns) {
                        startServer(serverConns + 1, false);
                    } else {
                        startServer(serverConns, false);
                    }
                }
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

    void startServer(final int nConns, boolean newThread) throws Exception {
        if (newThread) {
            serverThread = new Thread() {

                public void run() {
                    try {
                        doServerSide(nConns);
                    } catch (Exception e) {
                        System.err.println("Server died...");
                        e.printStackTrace();
                        serverReady = true;
                        serverException = e;
                    }
                }
            };
            serverThread.start();
        } else {
            try {
                doServerSide(nConns);
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
