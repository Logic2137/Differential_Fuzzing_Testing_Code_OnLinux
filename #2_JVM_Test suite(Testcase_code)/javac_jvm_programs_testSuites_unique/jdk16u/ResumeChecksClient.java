



import javax.net.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.net.*;
import java.util.*;

public class ResumeChecksClient {

    static String pathToStores = "../../../../javax/net/ssl/etc";
    static String keyStoreFile = "keystore";
    static String trustStoreFile = "truststore";
    static String passwd = "passphrase";

    enum TestMode {
        BASIC,
        VERSION_2_TO_3,
        VERSION_3_TO_2,
        CIPHER_SUITE,
        SIGNATURE_SCHEME
    }

    public static void main(String[] args) throws Exception {

        TestMode mode = TestMode.valueOf(args[0]);

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

        Server server = startServer();
        server.signal();
        SSLContext sslContext = SSLContext.getDefault();
        while (!server.started) {
            Thread.yield();
        }
        SSLSession firstSession = connect(sslContext, server.port, mode, false);

        server.signal();
        long secondStartTime = System.currentTimeMillis();
        Thread.sleep(10);
        SSLSession secondSession = connect(sslContext, server.port, mode, true);

        server.go = false;
        server.signal();

        switch (mode) {
        case BASIC:
            
            checkResumedSession(firstSession, secondSession);
            break;
        case VERSION_2_TO_3:
        case VERSION_3_TO_2:
        case CIPHER_SUITE:
        case SIGNATURE_SCHEME:
            
            if (secondSession.getCreationTime() <= secondStartTime) {
                throw new RuntimeException("Existing session was used");
            }
            break;
        default:
            throw new RuntimeException("unknown mode: " + mode);
        }
    }

    private static class NoSig implements AlgorithmConstraints {

        private final String alg;

        NoSig(String alg) {
            this.alg = alg;
        }


        private boolean test(String a) {
            return !a.toLowerCase().contains(alg.toLowerCase());
        }

        @Override
        public boolean permits(Set<CryptoPrimitive> primitives, Key key) {
            return true;
        }
        @Override
        public boolean permits(Set<CryptoPrimitive> primitives,
            String algorithm, AlgorithmParameters parameters) {

            return test(algorithm);
        }
        @Override
        public boolean permits(Set<CryptoPrimitive> primitives,
            String algorithm, Key key, AlgorithmParameters parameters) {

            return test(algorithm);
        }
    }

    private static SSLSession connect(SSLContext sslContext, int port,
        TestMode mode, boolean second) {

        try {
            SSLSocket sock = (SSLSocket)
                sslContext.getSocketFactory().createSocket();
            SSLParameters params = sock.getSSLParameters();

            switch (mode) {
            case BASIC:
                
                break;
            case VERSION_2_TO_3:
                if (second) {
                    params.setProtocols(new String[] {"TLSv1.3"});
                } else {
                    params.setProtocols(new String[] {"TLSv1.2"});
                }
                break;
            case VERSION_3_TO_2:
                if (second) {
                    params.setProtocols(new String[] {"TLSv1.2"});
                } else {
                    params.setProtocols(new String[] {"TLSv1.3"});
                }
                break;
            case CIPHER_SUITE:
                if (second) {
                    params.setCipherSuites(
                        new String[] {"TLS_AES_256_GCM_SHA384"});
                } else {
                    params.setCipherSuites(
                        new String[] {"TLS_AES_128_GCM_SHA256"});
                }
                break;
            case SIGNATURE_SCHEME:
                AlgorithmConstraints constraints =
                    params.getAlgorithmConstraints();
                if (second) {
                    params.setAlgorithmConstraints(new NoSig("ecdsa"));
                } else {
                    params.setAlgorithmConstraints(new NoSig("rsa"));
                }
                break;
            default:
                throw new RuntimeException("unknown mode: " + mode);
            }
            sock.setSSLParameters(params);
            sock.connect(new InetSocketAddress("localhost", port));
            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(sock.getOutputStream()));
            out.println("message");
            out.flush();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(sock.getInputStream()));
            String inMsg = reader.readLine();
            System.out.println("Client received: " + inMsg);
            SSLSession result = sock.getSession();
            sock.close();
            return result;
        } catch (Exception ex) {
            
            throw new RuntimeException(ex);
        }
    }

    private static void checkResumedSession(SSLSession initSession,
            SSLSession resSession) throws Exception {
        StringBuilder diffLog = new StringBuilder();

        
        
        long initCt = initSession.getCreationTime();
        long resumeCt = resSession.getCreationTime();
        if (initCt != resumeCt) {
            diffLog.append("Session creation time is different. Initial: ").
                    append(initCt).append(", Resumed: ").append(resumeCt).
                    append("\n");
        }

        
        if (!Arrays.equals(initSession.getLocalCertificates(),
                resSession.getLocalCertificates())) {
            diffLog.append("Local certificate mismatch between initial " +
                    "and resumed sessions\n");
        }

        if (!Arrays.equals(initSession.getPeerCertificates(),
                resSession.getPeerCertificates())) {
            diffLog.append("Peer certificate mismatch between initial " +
                    "and resumed sessions\n");
        }

        
        if (initSession.getApplicationBufferSize() !=
                resSession.getApplicationBufferSize()) {
            diffLog.append(String.format(
                    "App Buffer sizes differ: Init: %d, Res: %d\n",
                    initSession.getApplicationBufferSize(),
                    resSession.getApplicationBufferSize()));
        }

        if (initSession.getPacketBufferSize() !=
                resSession.getPacketBufferSize()) {
            diffLog.append(String.format(
                    "Packet Buffer sizes differ: Init: %d, Res: %d\n",
                    initSession.getPacketBufferSize(),
                    resSession.getPacketBufferSize()));
        }

        
        if (!initSession.getCipherSuite().equals(
                resSession.getCipherSuite())) {
            diffLog.append(String.format(
                    "CipherSuite does not match - Init: %s, Res: %s\n",
                    initSession.getCipherSuite(), resSession.getCipherSuite()));
        }

        
        if (!initSession.getPeerHost().equals(resSession.getPeerHost()) ||
                initSession.getPeerPort() != resSession.getPeerPort()) {
            diffLog.append(String.format(
                    "Host/Port mismatch - Init: %s/%d, Res: %s/%d\n",
                    initSession.getPeerHost(), initSession.getPeerPort(),
                    resSession.getPeerHost(), resSession.getPeerPort()));
        }

        
        if (!initSession.getProtocol().equals(resSession.getProtocol())) {
            diffLog.append(String.format(
                    "Protocol mismatch - Init: %s, Res: %s\n",
                    initSession.getProtocol(), resSession.getProtocol()));
        }

        
        
        if (diffLog.length() > 0) {
            throw new RuntimeException(diffLog.toString());
        }
    }

    private static Server startServer() {
        Server server = new Server();
        new Thread(server).start();
        return server;
    }

    private static class Server implements Runnable {

        public volatile boolean go = true;
        private boolean signal = false;
        public volatile int port = 0;
        public volatile boolean started = false;

        private synchronized void waitForSignal() {
            while (!signal) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    
                }
            }
            signal = false;
        }
        public synchronized void signal() {
            signal = true;
            notify();
        }

        @Override
        public void run() {
            try {

                SSLContext sc = SSLContext.getDefault();
                ServerSocketFactory fac = sc.getServerSocketFactory();
                SSLServerSocket ssock = (SSLServerSocket)
                    fac.createServerSocket(0);
                this.port = ssock.getLocalPort();

                waitForSignal();
                started = true;
                while (go) {
                    try {
                        System.out.println("Waiting for connection");
                        Socket sock = ssock.accept();
                        BufferedReader reader = new BufferedReader(
                            new InputStreamReader(sock.getInputStream()));
                        String line = reader.readLine();
                        System.out.println("server read: " + line);
                        PrintWriter out = new PrintWriter(
                            new OutputStreamWriter(sock.getOutputStream()));
                        out.println(line);
                        out.flush();
                        waitForSignal();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
