








import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyFactory;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class ECCurvesconstraints {

    

    
    static boolean separateServerThread = false;

    
    
    
    
    static String trustedCertStr =
        "-----BEGIN CERTIFICATE-----\n" +
        "MIIBCzCBugIEVz2lcjAKBggqhkjOPQQDAjAaMRgwFgYDVQQDDA93d3cuZXhhbXBs\n" +
        "ZS5vcmcwHhcNMTYwNTE5MTEzNzM5WhcNMTcwNTE5MTEzNzM5WjAaMRgwFgYDVQQD\n" +
        "DA93d3cuZXhhbXBsZS5vcmcwTjAQBgcqhkjOPQIBBgUrgQQAIAM6AAT68uovMZ8f\n" +
        "KARn5NOjvieJaq6h8zHYkM9w5DuN0kkOo4KBhke06EkQj0nvQQcSvppTV6RoDLY4\n" +
        "djAKBggqhkjOPQQDAgNAADA9AhwMNIujM0R0llpPH6d89d1S3VRGH/78ovc+zw51\n" +
        "Ah0AuZ1YlQkUbrJIzkuPSICxz5UfCWPe+7w4as+wiA==\n" +
        "-----END CERTIFICATE-----";

    
    static String targetPrivateKey =
        "MIGCAgEAMBAGByqGSM49AgEGBSuBBAAgBGswaQIBAQQdAPbckc86mgW/zexB1Ajq\n" +
        "38HntWOjdxL6XSoiAsWgBwYFK4EEACChPAM6AAT68uovMZ8fKARn5NOjvieJaq6h\n" +
        "8zHYkM9w5DuN0kkOo4KBhke06EkQj0nvQQcSvppTV6RoDLY4dg==";

    static String[] serverCerts = {trustedCertStr};
    static String[] serverKeys  = {targetPrivateKey};
    static String[] clientCerts = {trustedCertStr};
    static String[] clientKeys  = {targetPrivateKey};

    static char passphrase[] = "passphrase".toCharArray();

    
    volatile static boolean serverReady = false;

    
    static boolean debug = false;

    
    void doServerSide() throws Exception {
        SSLContext context = generateSSLContext(false);
        SSLServerSocketFactory sslssf = context.getServerSocketFactory();
        SSLServerSocket sslServerSocket =
            (SSLServerSocket)sslssf.createServerSocket(serverPort);
        serverPort = sslServerSocket.getLocalPort();

        
        serverReady = true;

        SSLSocket sslSocket = (SSLSocket)sslServerSocket.accept();
        try {
            sslSocket.setSoTimeout(5000);
            sslSocket.setSoLinger(true, 5);

            InputStream sslIS = sslSocket.getInputStream();
            OutputStream sslOS = sslSocket.getOutputStream();

            sslIS.read();
            sslOS.write('A');
            sslOS.flush();

            throw new Exception("EC curve secp224k1 should be disabled");
        } catch (IOException she) {
            
            System.out.println("Expected exception: " + she);
        } finally {
            sslSocket.close();
            sslServerSocket.close();
        }
    }

    
    void doClientSide() throws Exception {

        
        while (!serverReady) {
            Thread.sleep(50);
        }

        SSLContext context = generateSSLContext(true);
        SSLSocketFactory sslsf = context.getSocketFactory();

        SSLSocket sslSocket =
            (SSLSocket)sslsf.createSocket("localhost", serverPort);

        try {
            sslSocket.setSoTimeout(5000);
            sslSocket.setSoLinger(true, 5);

            InputStream sslIS = sslSocket.getInputStream();
            OutputStream sslOS = sslSocket.getOutputStream();

            sslOS.write('B');
            sslOS.flush();
            sslIS.read();

            throw new Exception("EC curve secp224k1 should be disabled");
        } catch (IOException she) {
            
            System.out.println("Expected exception: " + she);
        } finally {
            sslSocket.close();
        }
    }

    
    private static String tmAlgorithm;             

    private static void parseArguments(String[] args) {
        tmAlgorithm = args[0];
    }

    private static SSLContext generateSSLContext(boolean isClient)
            throws Exception {

        
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, null);

        
        ByteArrayInputStream is =
                    new ByteArrayInputStream(trustedCertStr.getBytes());
        Certificate trusedCert = cf.generateCertificate(is);
        is.close();

        ks.setCertificateEntry("Export Signer", trusedCert);

        String[] certStrs = null;
        String[] keyStrs = null;
        if (isClient) {
            certStrs = clientCerts;
            keyStrs = clientKeys;
        } else {
            certStrs = serverCerts;
            keyStrs = serverKeys;
        }

        for (int i = 0; i < certStrs.length; i++) {
            
            String keySpecStr = keyStrs[i];
            PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(
                                Base64.getMimeDecoder().decode(keySpecStr));
            KeyFactory kf = KeyFactory.getInstance("EC");
            ECPrivateKey priKey =
                    (ECPrivateKey)kf.generatePrivate(priKeySpec);

            
            String keyCertStr = certStrs[i];
            is = new ByteArrayInputStream(keyCertStr.getBytes());
            Certificate keyCert = cf.generateCertificate(is);
            is.close();

            Certificate[] chain = new Certificate[2];
            chain[0] = keyCert;
            chain[1] = trusedCert;

            
            ks.setKeyEntry("key-entry-" + i, priKey, passphrase, chain);
        }

        
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmAlgorithm);
        tmf.init(ks);

        SSLContext ctx = SSLContext.getInstance("TLS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("NewSunX509");
        kmf.init(ks, passphrase);

        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        ks = null;

        return ctx;
    }

    
    volatile int serverPort = 0;

    volatile Exception serverException = null;
    volatile Exception clientException = null;

    public static void main(String[] args) throws Exception {
        if (debug) {
            System.setProperty("javax.net.debug", "all");
        }

        
        parseArguments(args);

        
        new ECCurvesconstraints();
    }

    Thread clientThread = null;
    Thread serverThread = null;

    
    ECCurvesconstraints() throws Exception {
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
                        
                        System.err.println("Server died, because of " + e);
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
                        
                        System.err.println("Client died, because of " + e);
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
