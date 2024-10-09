



import javax.net.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.net.*;
import java.util.*;

public class ResumeChecksServer {

    static String pathToStores = "../../../../javax/net/ssl/etc";
    static String keyStoreFile = "keystore";
    static String trustStoreFile = "truststore";
    static String passwd = "passphrase";

    enum TestMode {
        BASIC,
        CLIENT_AUTH,
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

        SSLSession secondSession = null;

        SSLContext sslContext = SSLContext.getDefault();
        ServerSocketFactory fac = sslContext.getServerSocketFactory();
        SSLServerSocket ssock = (SSLServerSocket)
            fac.createServerSocket(0);

        Client client = startClient(ssock.getLocalPort());

        try {
            connect(client, ssock, mode, false);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        long secondStartTime = System.currentTimeMillis();
        Thread.sleep(10);
        try {
            secondSession = connect(client, ssock, mode, true);
        } catch (SSLHandshakeException ex) {
            
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        client.go = false;
        client.signal();

        switch (mode) {
        case BASIC:
            
            if (secondSession.getCreationTime() > secondStartTime) {
                throw new RuntimeException("Session was not reused");
            }
            break;
        case CLIENT_AUTH:
            
            secondSession.getPeerCertificates();
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

        public boolean permits(Set<CryptoPrimitive> primitives, Key key) {
            return true;
        }
        public boolean permits(Set<CryptoPrimitive> primitives,
            String algorithm, AlgorithmParameters parameters) {

            return test(algorithm);
        }
        public boolean permits(Set<CryptoPrimitive> primitives,
            String algorithm, Key key, AlgorithmParameters parameters) {

            return test(algorithm);
        }
    }

    private static SSLSession connect(Client client, SSLServerSocket ssock,
        TestMode mode, boolean second) throws Exception {

        try {
            client.signal();
            System.out.println("Waiting for connection");
            SSLSocket sock = (SSLSocket) ssock.accept();
            SSLParameters params = sock.getSSLParameters();

            switch (mode) {
            case BASIC:
                
                break;
            case CLIENT_AUTH:
                if (second) {
                    params.setNeedClientAuth(true);
                } else {
                    params.setNeedClientAuth(false);
                }
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
                        new String[] {"TLS_AES_128_GCM_SHA256"});
                } else {
                    params.setCipherSuites(
                        new String[] {"TLS_AES_256_GCM_SHA384"});
                }
                break;
            case SIGNATURE_SCHEME:
                params.setNeedClientAuth(true);
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
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(sock.getInputStream()));
            String line = reader.readLine();
            System.out.println("server read: " + line);
            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(sock.getOutputStream()));
            out.println(line);
            out.flush();
            out.close();
            SSLSession result = sock.getSession();
            sock.close();
            return result;
        } catch (SSLHandshakeException ex) {
            if (!second) {
                throw ex;
            }
        }
        return null;
    }

    private static Client startClient(int port) {
        Client client = new Client(port);
        new Thread(client).start();
        return client;
    }

    private static class Client implements Runnable {

        public volatile boolean go = true;
        private boolean signal = false;
        private final int port;

        Client(int port) {
            this.port = port;
        }

        private synchronized void waitForSignal() {
            while (!signal) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    
                }
            }
            signal = false;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                
            }
        }
        public synchronized void signal() {
            signal = true;
            notify();
        }

        public void run() {
            try {

                SSLContext sc = SSLContext.getDefault();

                waitForSignal();
                while (go) {
                    try {
                        SSLSocket sock = (SSLSocket)
                            sc.getSocketFactory().createSocket();
                        sock.connect(new InetSocketAddress("localhost", port));
                        PrintWriter out = new PrintWriter(
                            new OutputStreamWriter(sock.getOutputStream()));
                        out.println("message");
                        out.flush();
                        BufferedReader reader = new BufferedReader(
                            new InputStreamReader(sock.getInputStream()));
                        String inMsg = reader.readLine();
                        System.out.println("Client received: " + inMsg);
                        out.close();
                        sock.close();
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
