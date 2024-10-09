

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class PBMacBuffer {

    private final int LARGE_SIZE = 500000;

    public static void main(String[] args) {
        String[] PBMAC1Algorithms = {
            "HmacPBESHA1",
            "PBEWithHmacSHA1",
            "PBEWithHmacSHA224",
            "PBEWithHmacSHA256",
            "PBEWithHmacSHA384",
            "PBEWithHmacSHA512"
        };

        String[] PBKDF2Algorithms = {
            "PBKDF2WithHmacSHA1",
            "PBKDF2WithHmacSHA224",
            "PBKDF2WithHmacSHA256",
            "PBKDF2WithHmacSHA384",
            "PBKDF2WithHmacSHA512"
        };

        PBMacBuffer testRunner = new PBMacBuffer();
        boolean failed = false;

        for (String thePBMacAlgo : PBMAC1Algorithms) {

            for (String thePBKDF2Algo : PBKDF2Algorithms) {

                System.out.println("Running test with " + thePBMacAlgo
                        + " and " + thePBKDF2Algo + ":");
                try {
                    if (!testRunner.doTest(thePBMacAlgo, thePBKDF2Algo)) {
                        failed = true;
                    }
                } catch (NoSuchAlgorithmException | InvalidKeyException |
                        InvalidKeySpecException e) {
                    failed = true;
                    e.printStackTrace(System.out);
                    System.out.println("Test FAILED.");
                }
            }
        }

        if (failed) {
            throw new RuntimeException("One or more tests failed....");
        }
    }

    
    protected boolean doTest(String theMacAlgo, String thePBKDF2Algo)
            throws NoSuchAlgorithmException, InvalidKeyException,
            InvalidKeySpecException {
        
        SecretKey key = getSecretKey(thePBKDF2Algo);

        
        Mac theMac = Mac.getInstance(theMacAlgo);
        theMac.init(key);

        
        if (!largeByteBufferTest(theMac)) {
            System.out.println("Large ByteBuffer test case failed.");
            return false;
        }

        
        if (!emptyByteBufferTest(theMac)) {
            System.out.println("Empty ByteBuffer test case failed.");
            return false;
        }

        
        if (!nullByteBufferTest(theMac)) {
            System.out.println("NULL ByteBuffer test case failed.");
            return false;
        }

        return true;
    }

    
    protected boolean largeByteBufferTest(Mac theMac) {
        ByteBuffer buf = generateRandomByteBuffer(LARGE_SIZE);
        int limitBefore = buf.limit();

        theMac.update(buf);
        theMac.doFinal();

        int limitAfter = buf.limit();
        int positonAfter = buf.position();

        if (limitAfter != limitBefore) {
            System.out.println("FAIL: Buffer's limit has been chenged.");
            return false;
        }

        if (positonAfter != limitAfter) {
            System.out.println("FAIL: "
                    + "Buffer's position isn't equal to its limit");
            return false;
        }

        return true;
    }

    
    protected boolean emptyByteBufferTest(Mac theMac) {
        ByteBuffer buf = generateRandomByteBuffer(0);
        theMac.update(buf);
        theMac.doFinal();
        return true;
    }

    
    protected boolean nullByteBufferTest(Mac theMac) {
        try {
            ByteBuffer buf = null;
            theMac.update(buf);
            theMac.doFinal();
        } catch (IllegalArgumentException e) {
            
            return true;
        }

        System.out.println("FAIL: "
                + "IllegalArgumentException hasn't been thrown as expected");

        return false;
    }

    
    protected SecretKey getSecretKey(String thePBKDF2Algorithm)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        
        byte[] salt = new byte[64]; 
        new SecureRandom().nextBytes(salt);

        
        PBEKeySpec pbeKeySpec = new PBEKeySpec(
                "A #pwd# implied to be hidden!".toCharArray(),
                salt, 1000, 128);
        SecretKeyFactory keyFactory
                = SecretKeyFactory.getInstance(thePBKDF2Algorithm);
        return keyFactory.generateSecret(pbeKeySpec);
    }

    
    private ByteBuffer generateRandomByteBuffer(int size) {
        
        byte[] data = new byte[size];
        new Random().nextBytes(data);

        
        ByteBuffer bb = ByteBuffer.wrap(data);

        return bb;
    }

}
