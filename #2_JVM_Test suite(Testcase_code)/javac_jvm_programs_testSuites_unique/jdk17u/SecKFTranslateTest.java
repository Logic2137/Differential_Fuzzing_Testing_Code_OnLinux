import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.security.auth.DestroyFailedException;

public class SecKFTranslateTest {

    private static final String SUN_JCE = "SunJCE";

    public static void main(String[] args) throws Exception {
        SecKFTranslateTest test = new SecKFTranslateTest();
        test.run();
    }

    private void run() throws Exception {
        for (Algorithm algorithm : Algorithm.values()) {
            runTest(algorithm);
        }
    }

    private void runTest(Algorithm algo) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        AlgorithmParameterSpec[] aps = new AlgorithmParameterSpec[1];
        byte[] plainText = new byte[800];
        SecretKey key1 = algo.intSecurityKey(aps);
        Random random = new Random();
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algo.toString(), SUN_JCE);
        random.nextBytes(plainText);
        Cipher ci = Cipher.getInstance(algo.toString(), SUN_JCE);
        ci.init(Cipher.ENCRYPT_MODE, key1, aps[0]);
        byte[] cipherText = new byte[ci.getOutputSize(plainText.length)];
        int offset = ci.update(plainText, 0, plainText.length, cipherText, 0);
        ci.doFinal(cipherText, offset);
        SecretKey key2 = skf.translateKey(key1);
        ci.init(Cipher.DECRYPT_MODE, key2, aps[0]);
        byte[] recoveredText = new byte[ci.getOutputSize(plainText.length)];
        ci.doFinal(cipherText, 0, cipherText.length, recoveredText);
        if (!Arrays.equals(plainText, recoveredText)) {
            System.out.println("Key1:" + new String(key1.getEncoded()) + " Key2:" + new String(key2.getEncoded()));
            throw new RuntimeException("Testing translate key failed with " + algo);
        }
    }
}

class MyOwnSecKey implements SecretKey {

    private static final String DEFAULT_ALGO = "PBEWithMD5AndDES";

    private final byte[] key;

    private final String algorithm;

    private final int keySize;

    public MyOwnSecKey(byte[] key1, int offset, String algo) throws InvalidKeyException {
        algorithm = algo;
        if (algo.equalsIgnoreCase("DES")) {
            keySize = 8;
        } else if (algo.equalsIgnoreCase("DESede")) {
            keySize = 24;
        } else {
            throw new InvalidKeyException("Inappropriate key format and algorithm");
        }
        if (key1 == null || key1.length - offset < keySize) {
            throw new InvalidKeyException("Wrong key size");
        }
        key = new byte[keySize];
        System.arraycopy(key, offset, key, 0, keySize);
    }

    public MyOwnSecKey(PBEKeySpec ks) throws InvalidKeySpecException {
        algorithm = DEFAULT_ALGO;
        key = new String(ks.getPassword()).getBytes();
        keySize = key.length;
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public String getFormat() {
        return "RAW";
    }

    @Override
    public byte[] getEncoded() {
        byte[] copy = new byte[keySize];
        System.arraycopy(key, 0, copy, 0, keySize);
        return copy;
    }

    @Override
    public void destroy() throws DestroyFailedException {
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }
}

enum Algorithm {

    DES {

        @Override
        SecretKey intSecurityKey(AlgorithmParameterSpec[] spec) throws InvalidKeyException {
            int keyLength = 8;
            byte[] keyVal = new byte[keyLength];
            new SecureRandom().nextBytes(keyVal);
            SecretKey key1 = new MyOwnSecKey(keyVal, 0, this.toString());
            return key1;
        }
    }
    , DESEDE {

        @Override
        SecretKey intSecurityKey(AlgorithmParameterSpec[] spec) throws InvalidKeyException {
            int keyLength = 24;
            byte[] keyVal = new byte[keyLength];
            new SecureRandom().nextBytes(keyVal);
            SecretKey key1 = new MyOwnSecKey(keyVal, 0, this.toString());
            return key1;
        }
    }
    , PBEWithMD5ANDdes {

        @Override
        SecretKey intSecurityKey(AlgorithmParameterSpec[] spec) throws InvalidKeySpecException {
            byte[] salt = new byte[8];
            int iterCnt = 6;
            new Random().nextBytes(salt);
            spec[0] = new PBEParameterSpec(salt, iterCnt);
            PBEKeySpec pbeKS = new PBEKeySpec(new String("So far so good").toCharArray());
            SecretKey key1 = new MyOwnSecKey(pbeKS);
            return key1;
        }
    }
    ;

    abstract SecretKey intSecurityKey(AlgorithmParameterSpec[] spec) throws InvalidKeyException, InvalidKeySpecException;
}
