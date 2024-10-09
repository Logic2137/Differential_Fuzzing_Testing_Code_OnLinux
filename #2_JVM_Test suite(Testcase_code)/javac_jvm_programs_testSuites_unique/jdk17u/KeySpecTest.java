import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.NamedParameterSpec;
import java.security.spec.XECPublicKeySpec;
import java.security.spec.XECPrivateKeySpec;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.crypto.spec.DHPublicKeySpec;

public class KeySpecTest {

    public static void main(String[] args) throws Exception {
        String kaAlgo = args[0];
        String provider = args[1];
        String kpgAlgo = args[2];
        KeyPair kp = genKeyPair(provider, kpgAlgo, (args.length > 3) ? args[3] : kpgAlgo);
        testKeySpecs(provider, kaAlgo, kpgAlgo, kp);
        testEncodedKeySpecs(provider, kaAlgo, kpgAlgo, kp);
    }

    private static KeyPair genKeyPair(String provider, String kpgAlgo, String kpgInit) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(kpgAlgo, provider);
        switch(kpgInit) {
            case "DiffieHellman":
                kpg.initialize(512);
                break;
            case "EC":
                kpg.initialize(256);
                break;
            case "X25519":
                kpg.initialize(255);
                break;
            case "X448":
                kpg.initialize(448);
                break;
            default:
                throw new RuntimeException("Invalid Algo name " + kpgInit);
        }
        return kpg.generateKeyPair();
    }

    private static void testKeySpecs(String provider, String kaAlgo, String kpgAlgo, KeyPair kp) throws Exception {
        KeyFactory kf = KeyFactory.getInstance(kpgAlgo, provider);
        for (Class pubSpecType : getCompatibleKeySpecs(kpgAlgo, KeyType.PUBLIC)) {
            for (Class priSpecType : getCompatibleKeySpecs(kpgAlgo, KeyType.PRIVATE)) {
                KeySpec pubSpec = kf.getKeySpec(kp.getPublic(), pubSpecType);
                PublicKey pubKey = kf.generatePublic(pubSpec);
                KeySpec priSpec = kf.getKeySpec(kp.getPrivate(), priSpecType);
                PrivateKey priKey = kf.generatePrivate(priSpec);
                testKeyEquals(kp, pubKey, priKey);
                testSerialize(kp);
                if (!kaAlgo.equals("DiffieHellman")) {
                    testParamName(priSpec, pubSpec);
                }
                if (!Arrays.equals(getKeyAgreementSecret(provider, kaAlgo, kp.getPublic(), kp.getPrivate()), getKeyAgreementSecret(provider, kaAlgo, pubKey, priKey))) {
                    throw new RuntimeException("KeyAgreement secret mismatch.");
                }
            }
        }
    }

    private static void testEncodedKeySpecs(String provider, String kaAlgo, String kpgAlgo, KeyPair kp) throws Exception {
        KeyFactory kf = KeyFactory.getInstance(kpgAlgo, provider);
        PKCS8EncodedKeySpec priSpec = new PKCS8EncodedKeySpec(kp.getPrivate().getEncoded());
        PrivateKey priKey = kf.generatePrivate(priSpec);
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(kp.getPublic().getEncoded());
        PublicKey pubKey = kf.generatePublic(pubSpec);
        testKeyEquals(kp, pubKey, priKey);
        testSerialize(kp);
        if (!kaAlgo.equals("DiffieHellman")) {
            testParamName(priSpec, pubSpec);
        }
        if (!Arrays.equals(getKeyAgreementSecret(provider, kaAlgo, kp.getPublic(), kp.getPrivate()), getKeyAgreementSecret(provider, kaAlgo, pubKey, priKey))) {
            throw new RuntimeException("KeyAgreement secret mismatch.");
        }
    }

    private enum KeyType {

        PUBLIC, PRIVATE
    }

    private static List<Class> getCompatibleKeySpecs(String kpgAlgo, KeyType type) {
        List<Class> specs = new ArrayList<>();
        switch(kpgAlgo) {
            case "DiffieHellman":
                if (type == KeyType.PUBLIC) {
                    return Arrays.asList(X509EncodedKeySpec.class, DHPublicKeySpec.class);
                } else {
                    return Arrays.asList(PKCS8EncodedKeySpec.class, DHPrivateKeySpec.class);
                }
            case "EC":
                if (type == KeyType.PUBLIC) {
                    return Arrays.asList(X509EncodedKeySpec.class, ECPublicKeySpec.class);
                } else {
                    return Arrays.asList(PKCS8EncodedKeySpec.class, ECPrivateKeySpec.class);
                }
            case "XDH":
                if (type == KeyType.PUBLIC) {
                    return Arrays.asList(X509EncodedKeySpec.class, XECPublicKeySpec.class);
                } else {
                    return Arrays.asList(PKCS8EncodedKeySpec.class, XECPrivateKeySpec.class);
                }
        }
        return specs;
    }

    private static byte[] getKeyAgreementSecret(String provider, String kaAlgo, PublicKey pubKey, PrivateKey priKey) throws Exception {
        KeyAgreement ka = KeyAgreement.getInstance(kaAlgo, provider);
        ka.init(priKey);
        ka.doPhase(pubKey, true);
        return ka.generateSecret();
    }

    private static void testKeyEquals(KeyPair kp, PublicKey pubKey, PrivateKey priKey) {
        if (!kp.getPrivate().equals(priKey) && kp.getPrivate().hashCode() != priKey.hashCode()) {
            throw new RuntimeException("PrivateKey is not equal with PrivateKey" + " generated through KeySpec");
        }
        if (!kp.getPublic().equals(pubKey) && kp.getPublic().hashCode() != pubKey.hashCode()) {
            throw new RuntimeException("PublicKey is not equal with PublicKey" + " generated through KeySpec");
        }
    }

    private static void testParamName(KeySpec priSpec, KeySpec pubSpec) {
        if (priSpec instanceof XECPrivateKeySpec && pubSpec instanceof XECPublicKeySpec) {
            if (((NamedParameterSpec) ((XECPrivateKeySpec) priSpec).getParams()).getName() != ((NamedParameterSpec) ((XECPublicKeySpec) pubSpec).getParams()).getName()) {
                throw new RuntimeException("Curve name mismatch found");
            }
        }
    }

    private static void testSerialize(KeyPair keyPair) throws Exception {
        if (!keyPair.getPrivate().equals(deserializedCopy(keyPair.getPrivate(), PrivateKey.class))) {
            throw new RuntimeException("PrivateKey is not equal with PrivateKey" + " generated through Serialization");
        }
        if (!keyPair.getPublic().equals(deserializedCopy(keyPair.getPublic(), PublicKey.class))) {
            throw new RuntimeException("PublicKey is not equal with PublicKey" + " generated through Serialization");
        }
        KeyPair copy = deserializedCopy(keyPair, KeyPair.class);
        if (!keyPair.getPrivate().equals(copy.getPrivate())) {
            throw new RuntimeException("PrivateKey is not equal with PrivateKey" + " generated through Serialized KeyPair");
        }
        if (!keyPair.getPublic().equals(copy.getPublic())) {
            throw new RuntimeException("PublicKey is not equal with PublicKey" + " generated through Serialized KeyPair");
        }
    }

    private static <T extends Object> T deserializedCopy(T orig, Class<T> type) throws IOException, ClassNotFoundException {
        return deserialize(serialize(orig), type);
    }

    private static <T extends Object> T deserialize(byte[] serialized, Class<T> type) throws IOException, ClassNotFoundException {
        T key = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
            ObjectInputStream ois = new ObjectInputStream(bis)) {
            key = (T) ois.readObject();
        }
        return key;
    }

    private static <T extends Object> byte[] serialize(T key) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(key);
            return bos.toByteArray();
        }
    }
}
