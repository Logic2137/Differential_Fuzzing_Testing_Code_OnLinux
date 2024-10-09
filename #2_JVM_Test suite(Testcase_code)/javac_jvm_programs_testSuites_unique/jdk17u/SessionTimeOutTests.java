import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import javax.net.ssl.*;
import java.util.*;
import java.security.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SessionTimeOutTests {

    static boolean separateServerThread = true;

    static String pathToStores = "../etc";

    static String keyStoreFile = "keystore";

    static String trustStoreFile = "truststore";

    static String passwd = "passphrase";

    private static int PORTS = 3;

    private final CountDownLatch serverCondition = new CountDownLatch(PORTS);

    static boolean debug = false;

    static int MAX_ACTIVE_CONNECTIONS = 3;

    private static final int serverConns = MAX_ACTIVE_CONNECTIONS / PORTS;

    private static final int remainingConns = MAX_ACTIVE_CONNECTIONS % PORTS;

    private static final int TIMEOUT = 30000;

    void doServerSide(int slot, int serverConns) throws Exception {
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslssf.createServerSocket(0);
        sslServerSocket.setSoTimeout(TIMEOUT);
        serverPorts[slot] = sslServerSocket.getLocalPort();
        serverCondition.countDown();
        for (int nConnections = 0; nConnections < serverConns; nConnections++) {
            SSLSocket sslSocket = null;
            try {
                sslSocket = (SSLSocket) sslServerSocket.accept();
            } catch (SocketTimeoutException ste) {
                System.out.println("No incoming client connection. Ignore in server side.");
                continue;
            }
            InputStream sslIS = sslSocket.getInputStream();
            OutputStream sslOS = sslSocket.getOutputStream();
            sslIS.read();
            sslSocket.getSession();
            sslOS.write(85);
            sslOS.flush();
            sslSocket.close();
        }
    }

    void doClientSide() throws Exception {
        if (!serverCondition.await(TIMEOUT, TimeUnit.MILLISECONDS)) {
            System.out.println("The server side is not ready yet. Ignore in client side.");
            return;
        }
        SSLSocket[] sslSockets = new SSLSocket[MAX_ACTIVE_CONNECTIONS];
        Vector<SSLSession> sessions = new Vector<>();
        SSLSessionContext sessCtx = sslctx.getClientSessionContext();
        sessCtx.setSessionTimeout(10);
        int timeout = sessCtx.getSessionTimeout();
        for (int nConnections = 0; nConnections < MAX_ACTIVE_CONNECTIONS; nConnections++) {
            try {
                SSLSocket sslSocket = (SSLSocket) sslsf.createSocket();
                sslSocket.connect(new InetSocketAddress("localhost", serverPorts[nConnections % serverPorts.length]), TIMEOUT);
                sslSockets[nConnections] = sslSocket;
            } catch (IOException ioe) {
                System.out.println("Cannot make a connection in time. Ignore in client side.");
                continue;
            }
            InputStream sslIS = sslSockets[nConnections].getInputStream();
            OutputStream sslOS = sslSockets[nConnections].getOutputStream();
            sslOS.write(237);
            sslOS.flush();
            sslIS.read();
            SSLSession sess = sslSockets[nConnections].getSession();
            if (!sessions.contains(sess))
                sessions.add(sess);
        }
        System.out.println();
        System.out.println("Current timeout is set to: " + timeout);
        System.out.println("Testing SSLSessionContext.getSession()......");
        System.out.println("========================================" + "=======================");
        System.out.println("Session                                 " + "Session-     Session");
        System.out.println("                                        " + "lifetime     timedout?");
        System.out.println("========================================" + "=======================");
        for (int i = 0; i < sessions.size(); i++) {
            SSLSession session = (SSLSession) sessions.elementAt(i);
            long currentTime = System.currentTimeMillis();
            long creationTime = session.getCreationTime();
            long lifetime = (currentTime - creationTime) / 1000;
            System.out.print(session + "      " + lifetime + "            ");
            if (sessCtx.getSession(session.getId()) == null) {
                if (lifetime < timeout)
                    System.out.println("Invalidated before timeout");
                else
                    System.out.println("YES");
            } else {
                System.out.println("NO");
                if ((timeout != 0) && (lifetime > timeout)) {
                    throw new Exception("Session timeout test failed for the" + " obove session");
                }
            }
            if (i == ((sessions.size()) / 2)) {
                System.out.println();
                sessCtx.setSessionTimeout(2);
                timeout = sessCtx.getSessionTimeout();
                System.out.println("timeout is changed to: " + timeout);
                System.out.println();
            }
        }
        Enumeration<byte[]> e = sessCtx.getIds();
        System.out.println("----------------------------------------" + "-----------------------");
        System.out.println("Testing SSLSessionContext.getId()......");
        System.out.println();
        SSLSession nextSess = null;
        SSLSession sess;
        for (int i = 0; i < sessions.size(); i++) {
            sess = (SSLSession) sessions.elementAt(i);
            String isTimedout = "YES";
            long currentTime = System.currentTimeMillis();
            long creationTime = sess.getCreationTime();
            long lifetime = (currentTime - creationTime) / 1000;
            if (nextSess != null) {
                if (isEqualSessionId(nextSess.getId(), sess.getId())) {
                    isTimedout = "NO";
                    nextSess = null;
                }
            } else if (e.hasMoreElements()) {
                nextSess = sessCtx.getSession((byte[]) e.nextElement());
                if ((nextSess != null) && isEqualSessionId(nextSess.getId(), sess.getId())) {
                    nextSess = null;
                    isTimedout = "NO";
                }
            }
            if ((timeout != 0) && (lifetime > timeout) && (isTimedout.equals("NO"))) {
                throw new Exception("Session timeout test failed for session: " + sess + " lifetime: " + lifetime);
            }
            System.out.print(sess + "      " + lifetime);
            if (((timeout == 0) || (lifetime < timeout)) && (isTimedout.equals("YES"))) {
                isTimedout = "Invalidated before timeout";
            }
            System.out.println("            " + isTimedout);
        }
        for (int i = 0; i < sslSockets.length; i++) {
            sslSockets[i].close();
        }
        System.out.println("----------------------------------------" + "-----------------------");
        System.out.println("Session timeout test passed");
    }

    boolean isEqualSessionId(byte[] id1, byte[] id2) {
        if (id1.length != id2.length)
            return false;
        else {
            for (int i = 0; i < id1.length; i++) {
                if (id1[i] != id2[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    int[] serverPorts = new int[PORTS];

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
        if (debug)
            System.setProperty("javax.net.debug", "all");
        sslctx = SSLContext.getInstance("TLS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyFilename), passwd.toCharArray());
        kmf.init(ks, passwd.toCharArray());
        sslctx.init(kmf.getKeyManagers(), null, null);
        sslssf = (SSLServerSocketFactory) sslctx.getServerSocketFactory();
        sslsf = (SSLSocketFactory) sslctx.getSocketFactory();
        new SessionTimeOutTests();
    }

    Thread clientThread = null;

    Thread serverThread = null;

    SessionTimeOutTests() throws Exception {
        Exception startException = null;
        try {
            if (separateServerThread) {
                for (int i = 0; i < serverPorts.length; i++) {
                    if (i < remainingConns)
                        startServer(i, (serverConns + 1), true);
                    else
                        startServer(i, serverConns, true);
                }
                startClient(false);
            } else {
                startClient(true);
                for (int i = 0; i < PORTS; i++) {
                    if (i < remainingConns)
                        startServer(i, (serverConns + 1), false);
                    else
                        startServer(i, serverConns, false);
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

    void startServer(final int slot, final int nConns, boolean newThread) throws Exception {
        if (newThread) {
            serverThread = new Thread() {

                public void run() {
                    try {
                        doServerSide(slot, nConns);
                    } catch (Exception e) {
                        System.err.println("Server died...");
                        e.printStackTrace();
                        serverException = e;
                    }
                }
            };
            serverThread.start();
        } else {
            try {
                doServerSide(slot, nConns);
            } catch (Exception e) {
                serverException = e;
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
