import java.io.*;
import java.util.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;

public class TestPSSKeySupport {

    private static final String ALGO = "RSASSA-PSS";

    private static void testKey(Key key1, Key key2) throws Exception {
        if (key2.getAlgorithm().equals(ALGO) == false) {
            throw new Exception("Algorithm not " + ALGO);
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
        RSAPrivateCrtKey rsaKey = (RSAPrivateCrtKey) key;
        KeySpec rsaSpec3 = new RSAPrivateCrtKeySpec(rsaKey.getModulus(), rsaKey.getPublicExponent(), rsaKey.getPrivateExponent(), rsaKey.getPrimeP(), rsaKey.getPrimeQ(), rsaKey.getPrimeExponentP(), rsaKey.getPrimeExponentQ(), rsaKey.getCrtCoefficient(), rsaKey.getParams());
        PrivateKey key7 = kf.generatePrivate(rsaSpec3);
        testKey(key6, key6);
        testKey(key6, key7);
    }

    private static void test(KeyFactory kf, Key key) throws Exception {
        if (key.getAlgorithm().equals(ALGO) == false) {
            throw new Exception("Error: key algo should be " + ALGO);
        }
        if (key instanceof PublicKey) {
            testPublic(kf, (PublicKey) key);
        } else if (key instanceof PrivateKey) {
            testPrivate(kf, (PrivateKey) key);
        }
    }

    private static void checkKeyPair(KeyPair kp) throws Exception {
        PublicKey pubKey = kp.getPublic();
        if (!(pubKey instanceof RSAPublicKey)) {
            throw new Exception("Error: public key should be RSAPublicKey");
        }
        PrivateKey privKey = kp.getPrivate();
        if (!(privKey instanceof RSAPrivateKey)) {
            throw new Exception("Error: private key should be RSAPrivateKey");
        }
    }

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGO, "SunRsaSign");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        checkKeyPair(kp);
        BigInteger pubExp = ((RSAPublicKey) kp.getPublic()).getPublicExponent();
        PSSParameterSpec params = new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1);
        kpg.initialize(new RSAKeyGenParameterSpec(2048, pubExp, params));
        KeyPair kp2 = kpg.generateKeyPair();
        checkKeyPair(kp2);
        params = new PSSParameterSpec("SHA3-256", "MGF1", new MGF1ParameterSpec("SHA3-256"), 32, 1);
        kpg.initialize(new RSAKeyGenParameterSpec(2048, pubExp, params));
        KeyPair kp3 = kpg.generateKeyPair();
        checkKeyPair(kp3);
        KeyFactory kf = KeyFactory.getInstance(ALGO, "SunRsaSign");
        test(kf, kp.getPublic());
        test(kf, kp.getPrivate());
        test(kf, kp2.getPublic());
        test(kf, kp2.getPrivate());
        test(kf, kp3.getPublic());
        test(kf, kp3.getPrivate());
    }
}
