import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class ConcurrentClientAccessTest {

    private static final int THREADS = 50;

    public static void main(String[] args) throws Exception {
        Security.setProperty("jdk.tls.disabledAlgorithms", "");
        for (String tlsProtocol : new String[] { "TLSv1.3", "TLSv1.2", "TLSv1.1", "TLSv1" }) {
            System.out.printf("Protocol: %s%n", tlsProtocol);
            CountDownLatch tillServerReady = new CountDownLatch(1);
            Server server = new Server(tlsProtocol, tillServerReady);
            server.start();
            tillServerReady.await();
            CountDownLatch tillClientComplete = new CountDownLatch(THREADS);
            ExecutorService executor = null;
            try {
                executor = newExecutorService();
                for (int count = 1; count <= THREADS; count++) {
                    Client client = new Client(tlsProtocol, server.port, tillClientComplete);
                    executor.execute(client);
                    if (client.exception != null) {
                        throw new RuntimeException(client.exception);
                    }
                }
                tillClientComplete.await();
                System.out.println("All client processed successfully.");
            } finally {
                if (executor != null) {
                    executor.shutdown();
                }
                server.stopServer();
            }
            if (server.exception != null) {
                throw new RuntimeException(server.exception);
            }
            System.out.println();
        }
    }

    public static class Server implements Runnable {

        private volatile int port = 0;

        private final String tlsProtocol;

        private final CountDownLatch tillServerReady;

        private volatile Exception exception;

        private SSLServerSocket sslServerSocket;

        public Server(String tlsProtocol, CountDownLatch tillServerReady) {
            this.tlsProtocol = tlsProtocol;
            this.tillServerReady = tillServerReady;
        }

        public void start() {
            ExecutorService executor = null;
            try {
                executor = newExecutorService();
                executor.execute(this);
            } finally {
                if (executor != null) {
                    executor.shutdown();
                }
            }
        }

        void doServerSide() throws Exception {
            SSLContext ctx = getSSLContext(tlsProtocol);
            SSLServerSocketFactory sslssf = ctx.getServerSocketFactory();
            sslServerSocket = (SSLServerSocket) sslssf.createServerSocket(port);
            port = sslServerSocket.getLocalPort();
            System.out.println("Server listening on port: " + port);
            sslServerSocket.setEnabledProtocols(new String[] { tlsProtocol });
            sslServerSocket.setSoTimeout(20000);
            tillServerReady.countDown();
            while (sslServerSocket != null && !sslServerSocket.isClosed()) {
                try (SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept()) {
                    try (InputStream sslIS = sslSocket.getInputStream();
                        OutputStream sslOS = sslSocket.getOutputStream()) {
                        sslIS.read();
                        sslOS.write(85);
                        sslOS.flush();
                    }
                } catch (SocketTimeoutException | SocketException e) {
                    return;
                }
            }
        }

        @Override
        public void run() {
            try {
                doServerSide();
            } catch (Exception e) {
                this.exception = e;
            } finally {
                stopServer();
            }
        }

        public void stopServer() {
            if (sslServerSocket != null && !sslServerSocket.isClosed()) {
                System.out.println("Stopping Server.");
                try {
                    sslServerSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class Client implements Runnable {

        private final int serverPort;

        private final String tlsProtocol;

        private final CountDownLatch tillClientComplete;

        private volatile Exception exception;

        public Client(String tlsProtocol, int serverPort, CountDownLatch tillClientComplete) {
            this.tlsProtocol = tlsProtocol;
            this.serverPort = serverPort;
            this.tillClientComplete = tillClientComplete;
        }

        void doClientSide() throws Exception {
            SSLContext ctx = getSSLContext(this.tlsProtocol);
            SSLSocketFactory sslsf = ctx.getSocketFactory();
            try (SSLSocket sslSocket = (SSLSocket) sslsf.createSocket("localhost", serverPort)) {
                sslSocket.setEnabledProtocols(new String[] { this.tlsProtocol });
                try (InputStream sslIS = sslSocket.getInputStream();
                    OutputStream sslOS = sslSocket.getOutputStream()) {
                    sslOS.write(86);
                    sslOS.flush();
                    sslIS.read();
                }
            } finally {
                tillClientComplete.countDown();
            }
        }

        @Override
        public void run() {
            try {
                doClientSide();
            } catch (Exception e) {
                e.printStackTrace(System.out);
                this.exception = e;
            }
        }
    }

    protected static SSLContext getSSLContext(String tlsProtocol) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        KeyStore ts = KeyStore.getInstance("PKCS12");
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ts.load(null, null);
        ks.load(null, null);
        char[] passphrase = "passphrase".toCharArray();
        ts.setCertificateEntry("trusted-cert-" + KeyType.rsa_pkcs1_sha256.getKeyType(), cf.generateCertificate(new ByteArrayInputStream(KeyType.rsa_pkcs1_sha256.getTrustedCert().getBytes())));
        boolean hasKeyMaterials = KeyType.rsa_pkcs1_sha256.getEndCert() != null && KeyType.rsa_pkcs1_sha256.getPrivateKey() != null;
        if (hasKeyMaterials) {
            PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(KeyType.rsa_pkcs1_sha256.getPrivateKey()));
            KeyFactory kf = KeyFactory.getInstance(KeyType.rsa_pkcs1_sha256.getKeyType());
            PrivateKey priKey = kf.generatePrivate(priKeySpec);
            Certificate keyCert = cf.generateCertificate(new ByteArrayInputStream(KeyType.rsa_pkcs1_sha256.getEndCert().getBytes()));
            Certificate[] chain = new Certificate[] { keyCert };
            ks.setKeyEntry("cert-" + KeyType.rsa_pkcs1_sha256.getKeyType(), priKey, passphrase, chain);
        }
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
        tmf.init(ts);
        SSLContext context = SSLContext.getInstance(tlsProtocol);
        if (hasKeyMaterials) {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("NewSunX509");
            kmf.init(ks, passphrase);
            context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } else {
            context.init(null, tmf.getTrustManagers(), null);
        }
        return context;
    }

    private static ExecutorService newExecutorService() {
        return Executors.newCachedThreadPool((Runnable r) -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
    }
}

enum KeyType {

    rsa_pkcs1_sha256("RSA", "-----BEGIN CERTIFICATE-----\n" + "MIIDBjCCAe6gAwIBAgIUc8yTYekR2LuXkkCJYqWlS/pBMKIwDQYJKoZIhvcNAQEL\n" + "BQAwFDESMBAGA1UEAwwJbG9jYWxob3N0MB4XDTE4MDYwNDE1MjIwNFoXDTM4MDUz\n" + "MDE1MjIwNFowFDESMBAGA1UEAwwJbG9jYWxob3N0MIIBIjANBgkqhkiG9w0BAQEF\n" + "AAOCAQ8AMIIBCgKCAQEA2jDPGMogc9dq2w5b+FHqbfaGPokRmyObiU8y/l/dqkM5\n" + "9IV+qj8VQUI4NtpdCTWr16812z4AjXrk5HIBrECfQbHPUcm1rme5YVZ0WxV0+Ufy\n" + "hDmrGwDLhkxGqc3hOhRrlF2wdXeUfjIzhvS9+S/401++t/jvq+cqFF1BHnzYOu+l\n" + "nbi/o95Oqo8MlwiRqg3xy3fNRfqXk7DWy+QT8s+Vc3Pcj1EW6K0iJJ23BVTdv6YT\n" + "Ja5IKiWL4XsLht3fWvZwF+PoYfKb+JYflt0rafpxg9xkowe7GnGh2SpV7bJaH/QN\n" + "3PTFEKQWgWHjWwjR171GOzSaEgaklvKde6+zNWeYKwIDAQABo1AwTjAdBgNVHQ4E\n" + "FgQUqCtGe8/Ky4O6pH7xeTUy9yrv4n0wHwYDVR0jBBgwFoAUqCtGe8/Ky4O6pH7x\n" + "eTUy9yrv4n0wDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEAuqch30im\n" + "M09sARarbfK3OExqYK2xoyuUscgTqQNDpNL2gMdXY9e0lTmGVgw9pVYtNZPZRxem\n" + "jR5an2XegvG9qVU6vLENDwLCqZgsTb2gvmXngiG8NVcYd81GNqD228mkgBosNJku\n" + "6BR+C8zlURzsNEt657eVvIp9ObGomdAbWhpdqihBD180PP18DIBWopyfHfJtT5FA\n" + "U2kSPBp+P1EtdceW0zfwv3rF8hwRbnQBzuoYrZfn2PiMYaGUqOgbqUltCMD/Dp9G\n" + "xK0nfAXEwIqHWWnijGwAd6YrszMjBUcSGmlehdF+XZK6jHNlw64RB4WTfavr+rY0\n" + "dTe6g4o5GYr9nQ==\n" + "-----END CERTIFICATE-----\n", "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDaMM8YyiBz12rb\n" + "Dlv4Uept9oY+iRGbI5uJTzL+X92qQzn0hX6qPxVBQjg22l0JNavXrzXbPgCNeuTk\n" + "cgGsQJ9Bsc9RybWuZ7lhVnRbFXT5R/KEOasbAMuGTEapzeE6FGuUXbB1d5R+MjOG\n" + "9L35L/jTX763+O+r5yoUXUEefNg676WduL+j3k6qjwyXCJGqDfHLd81F+peTsNbL\n" + "5BPyz5Vzc9yPURborSIknbcFVN2/phMlrkgqJYvhewuG3d9a9nAX4+hh8pv4lh+W\n" + "3Stp+nGD3GSjB7sacaHZKlXtslof9A3c9MUQpBaBYeNbCNHXvUY7NJoSBqSW8p17\n" + "r7M1Z5grAgMBAAECggEAHs/7vw10TcejEHJTrJqs14CT7qresKDzqw1jLycMn6nE\n" + "unJLs/EaqE+Yrq5hqxZIQTo+CcsUuuYbAuPStqedleJtW6h3nryJImTaI67BCR8O\n" + "8XtPXY3cMAf/hqVLZC9UDey5Ka2Ma9HdEvbnCRSsN/VycnqWJhmMCLouowaQZqoE\n" + "VopscUix8GqELv0vEo2CszZfUjtSVbNTlNgwDf5U7eSKXMuFsnSn/LE7AMvHsEyo\n" + "HatxogwlM/WjpTnf/WIeJY3VhaK10IsP6OEgUn/p4VtI2DQ/TJdgYrvD5vhjY8ip\n" + "XuUPuPILRvJWo8dRXJqa4diXB12q5YhP8iiOp4BgkQKBgQD1GtlOR+JVgOzpQ11h\n" + "s5/iJOsczee80pQscbSRJnzSsIaP9WM8CyJgvbPxIQxLUQeYnxM/bxNKkpJtzxRK\n" + "pob+v4NoRn8PTpqbOp1obmWJT7uHTaoeavQo7r7uZI4i3eEgHCCQkMzpqzz7UFTY\n" + "2Yst7bBTPUivlSVQQBEc8bLpeQKBgQDj47EjpAlh8DmJRTElg58t+XJehXGTqmlx\n" + "nYu8DQLSzGbOQ/Z4srakC1mkM0LHCmULIIWk3KhV1GBCeArv7DlZ9A1SkI95bsq9\n" + "GBeQpovL0PXKkOOWMJBklP/CTECO4eyA8r6c1d8wytBb6MrJ8bi74DdT+JlFjK5A\n" + "zNoeNx6JwwKBgQCehIPABeuSYvRVlDTDqFkh98B6+4wBaatc5xjhuyOFW5dbaVeJ\n" + "kKXmLSpAK6B44WnpQhA/uUWfuBWtoPy9nt+1yARjnxwzuSFyfUEqNiPC32coBYmd\n" + "bIyGIIopQa1PTXJ4wtgoxw1PnmitHHITYPaLeKrN2te0fuAH+7dVodeU+QKBgAct\n" + "VJbaw7Dh7+3yz+lui8TW5lMzwK/13fxGCfCSOFSLO3Gjkk+a0UW5VclmE+RQ333K\n" + "OGtIx8RsO9vcC/wiZGwA06qWAu7AHoJ2D8fudtikbBlFFuXUAbgpOSTVYfMeCmTF\n" + "QFuQIMdYm9dJLZnOkxLXrOZoHeui0poX2Ya6FawhAoGAAI/QCyDbuvnJzGmjSbvl\n" + "5Ndr9lNAansCXaUzXuVLp6dD6PnB8HVCE8tdETZrcXseyTBeltaxAhj+tCybJvDO\n" + "sV8UmPR0w9ibExmUIVGX5BpoRlB/KWxEG3ar/wJbUZVZ2oSdIAZvCvdbN956SLDg\n" + "Pg5M5wrRqs71s2EiIJd0HrU=");

    private final String keyType;

    private final String trustedCert;

    private final String endCert;

    private final String privateKey;

    private KeyType(String keyType, String selfCert, String privateKey) {
        this.keyType = keyType;
        this.trustedCert = selfCert;
        this.endCert = selfCert;
        this.privateKey = privateKey;
    }

    public String getKeyType() {
        return keyType;
    }

    public String getTrustedCert() {
        return trustedCert;
    }

    public String getEndCert() {
        return endCert;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
