import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.PBEKeySpec;
import javax.security.auth.DestroyFailedException;
import static java.lang.System.out;

public class PBKDF2TranslateTest {

    private static final String PASS_PHRASE = "some hidden string";

    private static final int ITERATION_COUNT = 1000;

    private static final int KEY_SIZE = 128;

    private static final String[] TEST_ALGOS = { "PBKDF2WithHmacSHA1", "PBKDF2WithHmacSHA224", "PBKDF2WithHmacSHA256", "PBKDF2WithHmacSHA384", "PBKDF2WithHmacSHA512" };

    private final String algoForTest;

    public static void main(String[] args) throws Exception {
        for (String algo : TEST_ALGOS) {
            PBKDF2TranslateTest theTest = new PBKDF2TranslateTest(algo);
            byte[] salt = new byte[8];
            new Random().nextBytes(salt);
            theTest.testMyOwnSecretKey(salt);
            theTest.generateAndTranslateKey(salt);
            theTest.translateSpoiledKey(salt);
        }
    }

    public PBKDF2TranslateTest(String algo) {
        algoForTest = algo;
    }

    public void generateAndTranslateKey(byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        SecretKey key1 = getSecretKeyForPBKDF2(algoForTest, salt);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algoForTest);
        SecretKey key2 = skf.translateKey(key1);
        if (!Arrays.equals(key1.getEncoded(), key2.getEncoded())) {
            System.out.println("Key1=" + new String(key1.getEncoded()) + " key2=" + new String(key2.getEncoded()) + " salt=" + new String(salt));
            throw new RuntimeException("generateAndTranslateKey test case failed: the  key1 and" + " key2 values in its primary encoding format are" + " not the same for " + algoForTest + " algorithm.");
        }
    }

    private void testMyOwnSecretKey(byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        SecretKey key1 = getSecretKeyForPBKDF2(algoForTest, salt);
        SecretKey key2 = getMyOwnSecretKey(salt);
        if (!Arrays.equals(key1.getEncoded(), key2.getEncoded())) {
            throw new RuntimeException("We shouldn't be here. The key1 and key2 values in its" + " primary encoding format have to be the same!");
        }
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algoForTest);
        SecretKey key3 = skf.translateKey(key2);
        if (!Arrays.equals(key1.getEncoded(), key3.getEncoded())) {
            System.out.println("Key1=" + new String(key1.getEncoded()) + " key3=" + new String(key3.getEncoded()) + " salt=" + new String(salt));
            throw new RuntimeException("testMyOwnSecretKey test case failed: the key1  and key3" + " values in its primary encoding format are not" + " the same for " + algoForTest + " algorithm.");
        }
    }

    public void translateSpoiledKey(byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKey key1 = getMyOwnSecretKey(salt);
        ((MyPBKDF2SecretKey) key1).spoil();
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algoForTest);
        try {
            skf.translateKey(key1);
            throw new RuntimeException("translateSpoiledKey test case failed, should throw" + " InvalidKeyException when spoil the key");
        } catch (InvalidKeyException ike) {
            out.println("Expected exception when spoil the key");
        }
    }

    private SecretKey getSecretKeyForPBKDF2(String algoDeriveKey, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algoDeriveKey);
        PBEKeySpec spec = new PBEKeySpec(PASS_PHRASE.toCharArray(), salt, ITERATION_COUNT, KEY_SIZE);
        return skf.generateSecret(spec);
    }

    private SecretKey getMyOwnSecretKey(byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return new MyPBKDF2SecretKey(PASS_PHRASE, algoForTest, salt, ITERATION_COUNT, KEY_SIZE);
    }

    class MyPBKDF2SecretKey implements PBEKey {

        private final byte[] key;

        private final byte[] salt;

        private final String algorithm;

        private final int keyLength;

        private final String pass;

        private int itereationCount;

        public MyPBKDF2SecretKey(String passPhrase, String algo, byte[] salt1, int iterationCount, int keySize) throws InvalidKeySpecException, NoSuchAlgorithmException {
            algorithm = algo;
            salt = salt1;
            itereationCount = iterationCount;
            pass = passPhrase;
            PBEKeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount, keySize);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algo);
            SecretKey realKey = keyFactory.generateSecret(spec);
            keyLength = realKey.getEncoded().length;
            key = new byte[keyLength];
            System.arraycopy(realKey.getEncoded(), 0, key, 0, keyLength);
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
            byte[] copy = new byte[keyLength];
            System.arraycopy(key, 0, copy, 0, keyLength);
            return copy;
        }

        @Override
        public int getIterationCount() {
            return itereationCount;
        }

        @Override
        public byte[] getSalt() {
            return salt;
        }

        @Override
        public char[] getPassword() {
            return pass.toCharArray();
        }

        public void spoil() {
            itereationCount = -1;
        }

        @Override
        public void destroy() throws DestroyFailedException {
        }

        @Override
        public boolean isDestroyed() {
            return false;
        }
    }
}
