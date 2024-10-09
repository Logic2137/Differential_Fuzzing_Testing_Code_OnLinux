import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Arrays;
import java.util.stream.IntStream;
import static javax.crypto.Cipher.PRIVATE_KEY;
import static javax.crypto.Cipher.PUBLIC_KEY;

public class SignatureTest2 {

    private static final String KEYALG = "RSASSA-PSS";

    private static final String PROVIDER = "SunRsaSign";

    private static final int UPDATE_TIMES_TWO = 2;

    private static final int UPDATE_TIMES_TEN = 10;

    private static final String[] DIGEST_ALG = { "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512", "SHA-512/224", "SHA-512/256" };

    private static final String SIG_ALG = "RSASSA-PSS";

    private static PSSParameterSpec genPSSParameter(String digestAlgo, int digestLen, int keySize) {
        int saltLength = keySize / 8 - digestLen - 2;
        if (saltLength < 0) {
            System.out.println("keysize: " + keySize / 8 + ", digestLen: " + digestLen);
            return null;
        }
        return new PSSParameterSpec(digestAlgo, "MGF1", new MGF1ParameterSpec(digestAlgo), saltLength, 1);
    }

    public static void main(String[] args) throws Exception {
        final int testSize = Integer.parseInt(args[0]);
        byte[] data = new byte[100];
        IntStream.range(0, data.length).forEach(j -> {
            data[j] = (byte) j;
        });
        KeyPair kpair = generateKeys(KEYALG, testSize);
        Key[] privs = manipulateKey(PRIVATE_KEY, kpair.getPrivate());
        Key[] pubs = manipulateKey(PUBLIC_KEY, kpair.getPublic());
        Arrays.stream(privs).forEach(priv -> Arrays.stream(pubs).forEach(pub -> Arrays.stream(DIGEST_ALG).forEach(testAlg -> {
            checkSignature(data, (PublicKey) pub, (PrivateKey) priv, testAlg, testSize);
        })));
    }

    private static KeyPair generateKeys(String keyalg, int size) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(keyalg);
        kpg.initialize(size);
        return kpg.generateKeyPair();
    }

    private static Key[] manipulateKey(int type, Key key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        KeyFactory kf = KeyFactory.getInstance(KEYALG, PROVIDER);
        switch(type) {
            case PUBLIC_KEY:
                return new Key[] { kf.generatePublic(kf.getKeySpec(key, RSAPublicKeySpec.class)), kf.generatePublic(new X509EncodedKeySpec(key.getEncoded())), kf.generatePublic(new RSAPublicKeySpec(((RSAPublicKey) key).getModulus(), ((RSAPublicKey) key).getPublicExponent())) };
            case PRIVATE_KEY:
                return new Key[] { kf.generatePrivate(kf.getKeySpec(key, RSAPrivateKeySpec.class)), kf.generatePrivate(new PKCS8EncodedKeySpec(key.getEncoded())), kf.generatePrivate(new RSAPrivateKeySpec(((RSAPrivateKey) key).getModulus(), ((RSAPrivateKey) key).getPrivateExponent())) };
        }
        throw new RuntimeException("We shouldn't reach here");
    }

    private static void checkSignature(byte[] data, PublicKey pub, PrivateKey priv, String digestAlg, int keySize) throws RuntimeException {
        try {
            Signature sig = Signature.getInstance(SIG_ALG, PROVIDER);
            int digestLen = MessageDigest.getInstance(digestAlg).getDigestLength();
            PSSParameterSpec params = genPSSParameter(digestAlg, digestLen, keySize);
            if (params == null) {
                System.out.println("Skip test due to short key size");
                return;
            }
            sig.setParameter(params);
            sig.initSign(priv);
            for (int i = 0; i < UPDATE_TIMES_TEN; i++) {
                sig.update(data);
            }
            byte[] signedDataTen = sig.sign();
            sig.update(data);
            byte[] signedDataOne = sig.sign();
            System.out.println("Verify using params " + sig.getParameters());
            sig.initVerify(pub);
            sig.setParameter(params);
            for (int i = 0; i < UPDATE_TIMES_TEN; i++) {
                sig.update(data);
            }
            if (!sig.verify(signedDataTen)) {
                throw new RuntimeException("Signature verification test#1 failed w/ " + digestAlg);
            }
            sig.update(data);
            if (!sig.verify(signedDataOne)) {
                throw new RuntimeException("Signature verification test#2 failed w/ " + digestAlg);
            }
            for (int i = 0; i < UPDATE_TIMES_TWO; i++) {
                sig.update(data);
            }
            if (sig.verify(signedDataOne)) {
                throw new RuntimeException("Bad signature accepted w/ " + digestAlg);
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
