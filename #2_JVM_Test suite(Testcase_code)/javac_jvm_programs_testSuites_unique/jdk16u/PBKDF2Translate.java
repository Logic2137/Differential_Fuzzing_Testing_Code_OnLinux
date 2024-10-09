

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.PBEKeySpec;


public class PBKDF2Translate {

    private static final String[] ALGO_TO_TEST = {
        "PBKDF2WithHmacSHA1",
        "PBKDF2WithHmacSHA224",
        "PBKDF2WithHmacSHA256",
        "PBKDF2WithHmacSHA384",
        "PBKDF2WithHmacSHA512"
    };

    private static final String PASS_PHRASE = "some hidden string";
    private static final int ITERATION_COUNT = 1000;
    private static final int KEY_SIZE = 128;

    private final String algoToTest;
    private final byte[] salt = new byte[8];

    public static void main(String[] args) throws Exception {

        boolean failed = false;

        for (String algo : ALGO_TO_TEST) {

            System.out.println("Testing " + algo + ":");
            PBKDF2Translate theTest = new PBKDF2Translate(algo);

            try {
                if (!theTest.testMyOwnSecretKey()
                        || !theTest.generateAndTranslateKey()
                        || !theTest.translateSpoiledKey()) {
                    
                    failed = true;
                }
            } catch (InvalidKeyException | NoSuchAlgorithmException |
                    InvalidKeySpecException e) {
                e.printStackTrace(System.err);
                failed = true;
            }
        }

        if (failed) {
            throw new RuntimeException("One or more tests failed....");
        }
    }

    public PBKDF2Translate(String algoToTest) {
        this.algoToTest = algoToTest;
        new Random().nextBytes(this.salt);
    }

    
    public boolean generateAndTranslateKey() throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException {
        
        SecretKey key1 = getSecretKeyForPBKDF2(algoToTest);

        
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algoToTest);
        SecretKey key2 = skf.translateKey(key1);

        
        if (!Arrays.equals(key1.getEncoded(), key2.getEncoded())) {
            System.err.println("generateAndTranslateKey test case failed: the "
                    + "key1 and key2 values in its primary encoding format are "
                    + "not the same for " + algoToTest + "algorithm.");
            return false;
        }

        return true;
    }

    
    public boolean testMyOwnSecretKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException {
        SecretKey key1 = getSecretKeyForPBKDF2(algoToTest);
        SecretKey key2 = getMyOwnSecretKey();

        
        if (!Arrays.equals(key1.getEncoded(), key2.getEncoded())) {
            System.err.println("We shouldn't be here. The key1 and key2 values "
                    + "in its primary encoding format have to be the same!");
            return false;
        }

        
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algoToTest);
        SecretKey key3 = skf.translateKey(key2);

        
        if (!Arrays.equals(key1.getEncoded(), key3.getEncoded())) {
            System.err.println("testMyOwnSecretKey test case failed: the key1 "
                    + "and key3 values in its primary encoding format are not "
                    + "the same for " + algoToTest + "algorithm.");
            return false;
        }

        return true;
    }

    
    public boolean translateSpoiledKey() throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        
        SecretKey key1 = getMyOwnSecretKey();

        
        ((MyPBKDF2SecretKey) key1).spoil();

        
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algoToTest);
        try {
            SecretKey key2 = skf.translateKey(key1);
        } catch (InvalidKeyException ike) {
            
            return true;
        }

        return false;
    }

    
    private SecretKey getSecretKeyForPBKDF2(String algoToDeriveKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algoToDeriveKey);

        PBEKeySpec spec = new PBEKeySpec(PASS_PHRASE.toCharArray(),
                this.salt, ITERATION_COUNT, KEY_SIZE);

        return skf.generateSecret(spec);
    }

    
    private SecretKey getMyOwnSecretKey() throws InvalidKeySpecException,
            NoSuchAlgorithmException {
        return new MyPBKDF2SecretKey(PASS_PHRASE, this.algoToTest, this.salt,
                ITERATION_COUNT, KEY_SIZE);
    }
}


class MyPBKDF2SecretKey implements PBEKey {

    private final byte[] key;
    private final byte[] salt;
    private final String algorithm;
    private final int keySize, keyLength;
    private int itereationCount;
    private final String pass;

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
        System.arraycopy(this.key, 0, copy, 0, keyLength);
        return copy;
    }

    
    public MyPBKDF2SecretKey(String passPhrase, String algo, byte[] salt,
            int iterationCount, int keySize)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.algorithm = algo;
        this.salt = salt;
        this.itereationCount = iterationCount;
        this.keySize = keySize;
        this.pass = passPhrase;

        PBEKeySpec spec = new PBEKeySpec(passPhrase.toCharArray(),
                this.salt, iterationCount, this.keySize);

        SecretKeyFactory keyFactory
                = SecretKeyFactory.getInstance(algo);

        SecretKey realKey = keyFactory.generateSecret(spec);

        this.keyLength = realKey.getEncoded().length;

        this.key = new byte[this.keyLength];
        System.arraycopy(realKey.getEncoded(), 0, this.key, 0,
                this.keyLength);
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
        return this.pass.toCharArray();
    }

    
    public void spoil() {
        this.itereationCount = -1;
    }

}
