import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class StoreSecretKeyTest {

    private final static String DIR = System.getProperty("test.src", ".");

    private static final char[] PASSWORD = "passphrase".toCharArray();

    private static final String KEYSTORE = "keystore.p12";

    private static final String CERT = DIR + "/trusted.pem";

    private static final String ALIAS = "my trusted cert";

    private static final String ALIAS2 = "my secret key";

    private enum ALGORITHM {

        DES(56), DESede(168), AES(128);

        final int len;

        ALGORITHM(int l) {
            len = l;
        }

        final int getLength() {
            return len;
        }
    }

    public static void main(String[] args) throws Exception {
        boolean isSecretkeyAlgSupported = false;
        try {
            SecretKeyFactory.getInstance("AES");
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println("AES is unavailable. Skipping test...");
            return;
        }
        for (ALGORITHM alg : ALGORITHM.values()) {
            isSecretkeyAlgSupported |= testSecretKeyAlgorithm(alg);
        }
        if (!isSecretkeyAlgSupported) {
            throw new Exception("None of the SecretKey algorithms is " + "supported");
        }
    }

    private static boolean testSecretKeyAlgorithm(ALGORITHM algorithm) throws Exception {
        System.out.println("Testing algorithm : " + algorithm.name());
        new File(KEYSTORE).delete();
        try {
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            keystore.load(null, null);
            Certificate cert = loadCertificate(CERT);
            keystore.setEntry(ALIAS, new KeyStore.TrustedCertificateEntry(cert), null);
            SecretKey secretKey = generateSecretKey(algorithm.name(), algorithm.len);
            if (secretKey == null) {
                return false;
            }
            keystore.setEntry(ALIAS2, new KeyStore.SecretKeyEntry(secretKey), new KeyStore.PasswordProtection(PASSWORD));
            try (FileOutputStream outStream = new FileOutputStream(KEYSTORE)) {
                System.out.println("Storing keystore to: " + KEYSTORE);
                keystore.store(outStream, PASSWORD);
            }
            try (FileInputStream inStream = new FileInputStream(KEYSTORE)) {
                System.out.println("Loading keystore from: " + KEYSTORE);
                keystore.load(inStream, PASSWORD);
                System.out.println("Loaded keystore with " + keystore.size() + " entries");
            }
            KeyStore.Entry entry = keystore.getEntry(ALIAS2, new KeyStore.PasswordProtection(PASSWORD));
            System.out.println("Retrieved entry: " + entry);
            if (entry instanceof KeyStore.SecretKeyEntry) {
                System.out.println("Retrieved secret key entry: " + entry);
            } else {
                throw new Exception("Not a secret key entry");
            }
        } catch (KeyStoreException | UnrecoverableKeyException ex) {
            System.out.println("Unable to check SecretKey algorithm due to " + "exception: " + ex.getMessage());
            return false;
        }
        return true;
    }

    private static SecretKey generateSecretKey(String algorithm, int size) throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance(algorithm);
        generator.init(size);
        return generator.generateKey();
    }

    private static Certificate loadCertificate(String certFile) throws Exception {
        X509Certificate cert = null;
        try (FileInputStream certStream = new FileInputStream(certFile)) {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            return factory.generateCertificate(certStream);
        }
    }
}
