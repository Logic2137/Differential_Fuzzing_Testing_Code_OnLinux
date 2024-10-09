



import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.ArrayBlockingQueue;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionBindingEvent;
import javax.net.ssl.SSLSessionBindingListener;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLSessionFinalizeTest {

    

    
    static boolean separateServerThread = true;

    
    static String pathToStores = "../etc";
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

        while (serverReady) {
            SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();

            InputStream sslIS = sslSocket.getInputStream();
            OutputStream sslOS = sslSocket.getOutputStream();

            sslIS.read();
            sslOS.write(85);
            sslOS.flush();

            sslSocket.close();
        }
    }

    
    SBListener doClientSide() throws Exception {

        
        while (!serverReady) {
            Thread.sleep(50);
        }

        SSLSocketFactory sslsf =
            (SSLSocketFactory) SSLSocketFactory.getDefault();

        try {
                SSLSocket sslSocket = (SSLSocket)
                    sslsf.createSocket("localhost", serverPort);
                InputStream sslIS = sslSocket.getInputStream();
                OutputStream sslOS = sslSocket.getOutputStream();

            sslOS.write(280);
            sslOS.flush();
            sslIS.read();

            sslOS.close();
            sslIS.close();

            SSLSession sslSession = sslSocket.getSession();
            System.out.printf(" sslSession: %s %n   %s%n", sslSession, sslSession.getClass());
            SBListener sbListener = new SBListener(sslSession);

            sslSession.putValue("x", sbListener);

            sslSession.invalidate();

            sslSocket.close();

            sslOS = null;
            sslIS = null;
            sslSession = null;
            sslSocket = null;
            Reference.reachabilityFence(sslOS);
            Reference.reachabilityFence(sslIS);
            Reference.reachabilityFence(sslSession);
            Reference.reachabilityFence(sslSocket);

            return sbListener;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    

    
    volatile int serverPort = 0;

    volatile Exception serverException = null;
    volatile Exception clientException = null;

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

        if (debug)
            System.setProperty("javax.net.debug", "all");

        
        new SSLSessionFinalizeTest();
    }

    ArrayBlockingQueue<Thread> threads = new ArrayBlockingQueue<Thread>(100);

    ArrayBlockingQueue<SBListener> sbListeners = new ArrayBlockingQueue<>(100);

    
    SSLSessionFinalizeTest() throws Exception {
        final int count = 1;
        if (separateServerThread) {
            startServer(true);
            startClients(true, count);
        } else {
            startClients(true, count);
            startServer(true);
        }

        
        Thread t;
        while ((t = threads.take()) != Thread.currentThread()) {
            System.out.printf("  joining: %s%n", t);
            t.join(1000L);
        }
        serverReady = false;
        System.gc();
        System.gc();


        SBListener listener = null;
        while ((listener = sbListeners.poll()) != null) {
            if (!listener.check()) {
                System.out.printf("  sbListener not called on finalize: %s%n",
                        listener);
            }
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
            Thread t = new Thread("Server") {
                public void run() {
                    try {
                        doServerSide();
                    } catch (Exception e) {
                        
                        System.err.println("Server died..." + e);
                        serverReady = true;
                        serverException = e;
                    }
                }
            };
            threads.add(t);
            t.setDaemon(true);
            t.start();
        } else {
            doServerSide();
        }
    }

    void startClients(boolean newThread, int count) throws Exception {
        for (int i = 0; i < count; i++) {
            System.out.printf(" newClient: %d%n", i);
            startClient(newThread);
        }
        serverReady = false;

        threads.add(Thread.currentThread());    
    }
    void startClient(boolean newThread) throws Exception {
        if (newThread) {
            Thread t = new Thread("Client") {
                public void run() {
                    try {
                        sbListeners.add(doClientSide());
                    } catch (Exception e) {
                        
                        System.err.println("Client died..." + e);
                        clientException = e;
                    }
                }
            };
            System.out.printf(" starting: %s%n", t);
            threads.add(t);
            t.start();
        } else {
            sbListeners.add(doClientSide());
        }
    }


    static class SBListener implements SSLSessionBindingListener {
        private volatile int unboundNotified;
        private final WeakReference<SSLSession> session;

        SBListener(SSLSession session) {
            this.unboundNotified = 0;
            this.session = new WeakReference<SSLSession>(session);
        }

        boolean check() {
            System.out.printf("  check: %s%n", this);
            return unboundNotified > 0 && session.get() == null;
        }

        @Override
        public void valueBound(SSLSessionBindingEvent event) {
            System.out.printf(" valueBound: %s%n", event.getName());
        }

        @Override
        public void valueUnbound(SSLSessionBindingEvent event) {
            System.out.printf(" valueUnbound: %s%n", event.getName());
            unboundNotified++;
        }

        public String toString() {
            return "count: " + unboundNotified +
                    ", ref: " + session.get();
        }
    }
}
