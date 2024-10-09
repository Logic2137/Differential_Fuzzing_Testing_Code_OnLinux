



import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;

public class ProbeLargeKeystore {

    private static final String DIR = System.getProperty("test.src", ".");
    private static final String CERT = DIR + "/trusted.pem";
    private static final String ALIAS = "test-entry-";
    private static final int COUNT = 100;
    private static final String KEYSTORE = "test-keystore.p12";
    private static final char[] PASSWORD = "passphrase".toCharArray();

    public static final void main(String[] args) throws Exception {

        

        new File(KEYSTORE).delete();
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(null, null);
        Certificate cert = loadCertificate(CERT);

        for (int i = 0; i < COUNT; i++) {
            keystore.setCertificateEntry(ALIAS + i, cert);
        }

        try (FileOutputStream out = new FileOutputStream(KEYSTORE)) {
            keystore.store(out, PASSWORD);
        }

        

        KeyStore largeKeystore =
           KeyStore.getInstance(new File(KEYSTORE), PASSWORD);

        if (largeKeystore.size() != COUNT) {
            throw new Exception("Error detecting a large PKCS12 keystore");
        }

        new File(KEYSTORE).delete();
        System.out.println("OK");
    }

    private static final Certificate loadCertificate(String certFile)
            throws Exception {
        try (FileInputStream certStream = new FileInputStream(certFile)) {
             CertificateFactory factory =
                 CertificateFactory.getInstance("X.509");
            return factory.generateCertificate(certStream);
        }
    }
}
