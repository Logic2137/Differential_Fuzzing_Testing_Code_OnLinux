

import java.io.*;
import java.net.*;
import java.util.*;
import java.security.*;
import java.security.cert.*;
import javax.net.*;
import javax.net.ssl.*;

import sun.security.util.KeyUtil;

public class ShortRSAKeyWithinTLS {

    

    
    static boolean separateServerThread = false;

    
    volatile static boolean serverReady = false;

    
    static boolean debug = false;

    

    
    void doServerSide() throws Exception {

        
        serverKS = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
        serverKS.load(null, null);
        System.out.println("Loaded keystore: Windows-MY");

        
        checkKeySize(serverKS);

        
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(serverKS, null);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(serverKS);
        TrustManager[] tms = tmf.getTrustManagers();
        if (tms == null || tms.length == 0) {
            throw new Exception("unexpected trust manager implementation");
        } else {
            if (!(tms[0] instanceof X509TrustManager)) {
                throw new Exception("unexpected trust manager" +
                        " implementation: " +
                        tms[0].getClass().getCanonicalName());
            }
        }
        serverTM = new MyExtendedX509TM((X509TrustManager)tms[0]);
        tms = new TrustManager[] {serverTM};

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(kmf.getKeyManagers(), tms, null);

        ServerSocketFactory ssf = ctx.getServerSocketFactory();
        SSLServerSocket sslServerSocket = (SSLServerSocket)
                                ssf.createServerSocket(serverPort);
        sslServerSocket.setNeedClientAuth(true);
        serverPort = sslServerSocket.getLocalPort();
        System.out.println("serverPort = " + serverPort);

        
        serverReady = true;

        SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
        InputStream sslIS = sslSocket.getInputStream();
        OutputStream sslOS = sslSocket.getOutputStream();

        sslIS.read();
        sslOS.write(85);
        sslOS.flush();

        sslSocket.close();
    }

    
    void doClientSide() throws Exception {

        
        while (!serverReady) {
            Thread.sleep(50);
        }

        
        KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
        ks.load(null, null);
        System.out.println("Loaded keystore: Windows-MY");

        
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, null);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        SSLSocketFactory sslsf = ctx.getSocketFactory();
        SSLSocket sslSocket = (SSLSocket)
            sslsf.createSocket("localhost", serverPort);

        if (clientProtocol != null) {
            sslSocket.setEnabledProtocols(new String[] {clientProtocol});
        }

        if (clientCiperSuite != null) {
            sslSocket.setEnabledCipherSuites(new String[] {clientCiperSuite});
        }

        InputStream sslIS = sslSocket.getInputStream();
        OutputStream sslOS = sslSocket.getOutputStream();

        sslOS.write(280);
        sslOS.flush();
        sslIS.read();

        sslSocket.close();
    }

    private void checkKeySize(KeyStore ks) throws Exception {
        PrivateKey privateKey = null;
        PublicKey publicKey = null;

        if (ks.containsAlias(keyAlias)) {
            System.out.println("Loaded entry: " + keyAlias);
            privateKey = (PrivateKey)ks.getKey(keyAlias, null);
            publicKey = (PublicKey)ks.getCertificate(keyAlias).getPublicKey();

            int privateKeySize = KeyUtil.getKeySize(privateKey);
            if (privateKeySize != keySize) {
                throw new Exception("Expected key size is " + keySize +
                        ", but the private key size is " + privateKeySize);
            }

            int publicKeySize = KeyUtil.getKeySize(publicKey);
            if (publicKeySize != keySize) {
                throw new Exception("Expected key size is " + keySize +
                        ", but the public key size is " + publicKeySize);
            }
        }
    }

    

    
    volatile int serverPort = 0;

    volatile Exception serverException = null;
    volatile Exception clientException = null;

    private static String keyAlias;
    private static int keySize;
    private static String clientProtocol = null;
    private static String clientCiperSuite = null;

    private static void parseArguments(String[] args) {
        keyAlias = args[0];
        keySize = Integer.parseInt(args[1]);

        if (args.length > 2) {
            clientProtocol = args[2];
        }

        if (args.length > 3) {
            clientCiperSuite = args[3];
        }
    }

    public static void main(String[] args) throws Exception {
        if (debug) {
            System.setProperty("javax.net.debug", "all");
        }

        
        parseArguments(args);

        new ShortRSAKeyWithinTLS();
    }

    Thread clientThread = null;
    Thread serverThread = null;
    KeyStore serverKS;
    MyExtendedX509TM serverTM;

    
    ShortRSAKeyWithinTLS() throws Exception {
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


    class MyExtendedX509TM extends X509ExtendedTrustManager
            implements X509TrustManager {

        X509TrustManager tm;

        MyExtendedX509TM(X509TrustManager tm) {
            this.tm = tm;
        }

        public void checkClientTrusted(X509Certificate chain[], String authType)
                throws CertificateException {
            tm.checkClientTrusted(chain, authType);
        }

        public void checkServerTrusted(X509Certificate chain[], String authType)
                throws CertificateException {
            tm.checkServerTrusted(chain, authType);
        }

        public X509Certificate[] getAcceptedIssuers() {
            List<X509Certificate> certs = new ArrayList<>();
            try {
                for (X509Certificate c : tm.getAcceptedIssuers()) {
                    if (serverKS.getCertificateAlias(c).equals(keyAlias))
                        certs.add(c);
                }
            } catch (KeyStoreException kse) {
                throw new RuntimeException(kse);
            }
            return certs.toArray(new X509Certificate[certs.size()]);
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType,
                Socket socket) throws CertificateException {
            tm.checkClientTrusted(chain, authType);
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType,
                Socket socket) throws CertificateException {
            tm.checkServerTrusted(chain, authType);
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType,
            SSLEngine engine) throws CertificateException {
            tm.checkClientTrusted(chain, authType);
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType,
            SSLEngine engine) throws CertificateException {
            tm.checkServerTrusted(chain, authType);
        }
    }

}

