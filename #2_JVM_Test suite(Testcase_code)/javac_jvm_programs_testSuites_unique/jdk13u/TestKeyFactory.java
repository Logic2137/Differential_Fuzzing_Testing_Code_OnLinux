import java.io.*;
import java.util.*;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;

public class TestKeyFactory {

    private final static String BASE = System.getProperty("test.src", ".");

    private static final char[] password = "test12".toCharArray();

    static KeyStore getKeyStore() throws Exception {
        InputStream in = new FileInputStream(new File(BASE, "rsakeys.ks"));
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(in, password);
        in.close();
        return ks;
    }

    private static void testKey(Key key1, Key key2) throws Exception {
        if (key2.getAlgorithm().equals("RSA") == false) {
            throw new Exception("Algorithm not RSA");
        }
        if (key1 instanceof PublicKey) {
            if (key2.getFormat().equals("X.509") == false) {
                throw new Exception("Format not X.509");
            }
        } else if (key1 instanceof PrivateKey) {
            if (key2.getFormat().equals("PKCS#8") == false) {
                throw new Exception("Format not PKCS#8");
            }
        }
        if (key1.equals(key2) == false) {
            throw new Exception("Keys not equal");
        }
        if (Arrays.equals(key1.getEncoded(), key2.getEncoded()) == false) {
            throw new Exception("Encodings not equal");
        }
    }

    private static void testPublic(KeyFactory kf, PublicKey key) throws Exception {
        System.out.println("Testing public key...");
        PublicKey key2 = (PublicKey) kf.translateKey(key);
        KeySpec rsaSpec = kf.getKeySpec(key, RSAPublicKeySpec.class);
        PublicKey key3 = kf.generatePublic(rsaSpec);
        KeySpec x509Spec = kf.getKeySpec(key, X509EncodedKeySpec.class);
        PublicKey key4 = kf.generatePublic(x509Spec);
        KeySpec x509Spec2 = new X509EncodedKeySpec(key.getEncoded());
        PublicKey key5 = kf.generatePublic(x509Spec2);
        testKey(key, key);
        testKey(key, key2);
        testKey(key, key3);
        testKey(key, key4);
        testKey(key, key5);
    }

    private static void testPrivate(KeyFactory kf, PrivateKey key) throws Exception {
        System.out.println("Testing private key...");
        PrivateKey key2 = (PrivateKey) kf.translateKey(key);
        KeySpec rsaSpec = kf.getKeySpec(key, RSAPrivateCrtKeySpec.class);
        PrivateKey key3 = kf.generatePrivate(rsaSpec);
        KeySpec pkcs8Spec = kf.getKeySpec(key, PKCS8EncodedKeySpec.class);
        PrivateKey key4 = kf.generatePrivate(pkcs8Spec);
        KeySpec pkcs8Spec2 = new PKCS8EncodedKeySpec(key.getEncoded());
        PrivateKey key5 = kf.generatePrivate(pkcs8Spec2);
        testKey(key, key);
        testKey(key, key2);
        testKey(key, key3);
        testKey(key, key4);
        testKey(key, key5);
        KeySpec rsaSpec2 = kf.getKeySpec(key, RSAPrivateKeySpec.class);
        PrivateKey key6 = kf.generatePrivate(rsaSpec2);
        RSAPrivateKey rsaKey = (RSAPrivateKey) key;
        KeySpec rsaSpec3 = new RSAPrivateKeySpec(rsaKey.getModulus(), rsaKey.getPrivateExponent());
        PrivateKey key7 = kf.generatePrivate(rsaSpec3);
        testKey(key6, key6);
        testKey(key6, key7);
    }

    private static void test(KeyFactory kf, Key key) throws Exception {
        if (key.getAlgorithm().equals("RSA") == false) {
            System.out.println("Not an RSA key, ignoring");
        }
        if (key instanceof PublicKey) {
            testPublic(kf, (PublicKey) key);
        } else if (key instanceof PrivateKey) {
            testPrivate(kf, (PrivateKey) key);
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        KeyStore ks = getKeyStore();
        KeyFactory kf = KeyFactory.getInstance("RSA", "SunRsaSign");
        for (Enumeration e = ks.aliases(); e.hasMoreElements(); ) {
            String alias = (String) e.nextElement();
            Key key = null;
            if (ks.isKeyEntry(alias)) {
                test(kf, ks.getKey(alias, password));
                test(kf, ks.getCertificate(alias).getPublicKey());
            }
        }
        long stop = System.currentTimeMillis();
        System.out.println("All tests passed (" + (stop - start) + " ms).");
    }
}
