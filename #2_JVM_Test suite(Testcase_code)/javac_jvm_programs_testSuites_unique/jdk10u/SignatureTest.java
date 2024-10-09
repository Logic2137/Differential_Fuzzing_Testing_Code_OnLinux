
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.stream.IntStream;
import static javax.crypto.Cipher.PRIVATE_KEY;
import static javax.crypto.Cipher.PUBLIC_KEY;


public class SignatureTest {
    
    private static final String KEYALG = "RSA";

    
    private static final String PROVIDER = "SunRsaSign";

    
    private static final int UPDATE_TIMES_FIFTY = 50;

    
    private static final int UPDATE_TIMES_HUNDRED = 100;

    
    private static final String[] SIGN_ALG = {"MD2withRSA", "MD5withRSA",
        "SHA1withRSA", "SHA256withRSA"};

    public static void main(String[] args) throws Exception {
        int testSize = Integer.parseInt(args[0]);

        byte[] data = new byte[100];
        IntStream.range(0, data.length).forEach(j -> {
            data[j] = (byte) j;
        });

        
        KeyPair kpair = generateKeys(KEYALG, testSize);
        Key[] privs = manipulateKey(PRIVATE_KEY, kpair.getPrivate());
        Key[] pubs = manipulateKey(PUBLIC_KEY, kpair.getPublic());
        

        Arrays.stream(privs).forEach(priv
                -> Arrays.stream(pubs).forEach(pub
                -> Arrays.stream(SIGN_ALG).forEach(testAlg -> {
            try {
                checkSignature(data, (PublicKey) pub, (PrivateKey) priv,
                        testAlg);
            } catch (NoSuchAlgorithmException | InvalidKeyException |
                    SignatureException | NoSuchProviderException ex) {
                throw new RuntimeException(ex);
            }
        }
        )));

    }

    private static KeyPair generateKeys(String keyalg, int size)
            throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(keyalg);
        kpg.initialize(size);
        return kpg.generateKeyPair();
    }

    private static Key[] manipulateKey(int type, Key key)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        KeyFactory kf = KeyFactory.getInstance(KEYALG, PROVIDER);

        switch (type) {
            case PUBLIC_KEY:
                try {
                    kf.getKeySpec(key, RSAPrivateKeySpec.class);
                    throw new RuntimeException("Expected InvalidKeySpecException "
                            + "not thrown");
                } catch (InvalidKeySpecException expected) {
                }

                return new Key[]{
                    kf.generatePublic(kf.getKeySpec(key, RSAPublicKeySpec.class)),
                    kf.generatePublic(new X509EncodedKeySpec(key.getEncoded())),
                    kf.generatePublic(new RSAPublicKeySpec(
                    ((RSAPublicKey) key).getModulus(),
                    ((RSAPublicKey) key).getPublicExponent()))
                };
            case PRIVATE_KEY:
                try {
                    kf.getKeySpec(key, RSAPublicKeySpec.class);
                    throw new RuntimeException("Expected InvalidKeySpecException"
                            + " not thrown");
                } catch (InvalidKeySpecException expected) {
                }
                return new Key[]{
                    kf.generatePrivate(kf.getKeySpec(key,
                    RSAPrivateKeySpec.class)),
                    kf.generatePrivate(new PKCS8EncodedKeySpec(
                    key.getEncoded())),
                    kf.generatePrivate(new RSAPrivateKeySpec(((RSAPrivateKey) key).getModulus(),
                    ((RSAPrivateKey) key).getPrivateExponent()))
                };
        }
        throw new RuntimeException("We shouldn't reach here");
    }

    private static void checkSignature(byte[] data, PublicKey pub,
            PrivateKey priv, String sigalg) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException, NoSuchProviderException {
        Signature sig = Signature.getInstance(sigalg, PROVIDER);
        sig.initSign(priv);
        for (int i = 0; i < UPDATE_TIMES_HUNDRED; i++) {
            sig.update(data);
        }
        byte[] signedData = sig.sign();

        
        sig.initVerify(pub);
        for (int i = 0; i < UPDATE_TIMES_HUNDRED; i++) {
            sig.update(data);
        }
        if (!sig.verify(signedData)) {
            throw new RuntimeException("Failed to verify " + sigalg
                    + " signature");
        }

        
        
        sig.initVerify(pub);
        for (int i = 0; i < UPDATE_TIMES_FIFTY; i++) {
            sig.update(data);
        }

        if (sig.verify(signedData)) {
            throw new RuntimeException("Failed to detect bad " + sigalg
                    + " signature");
        }
    }
}
