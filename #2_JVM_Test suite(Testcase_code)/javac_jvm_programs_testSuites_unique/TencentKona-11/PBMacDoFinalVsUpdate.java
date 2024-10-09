

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class PBMacDoFinalVsUpdate {

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

        PBMacDoFinalVsUpdate testRunner = new PBMacDoFinalVsUpdate();
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
        int OFFSET = 5;

        
        byte[] plain = new byte[25];
        new SecureRandom().nextBytes(plain);

        
        byte[] tail = new byte[plain.length - OFFSET];
        System.arraycopy(plain, OFFSET, tail, 0, tail.length);

        
        SecretKey key = getSecretKey(thePBKDF2Algo);

        
        Mac theMac = Mac.getInstance(theMacAlgo);
        theMac.init(key);
        byte[] result1 = theMac.doFinal(plain);

        if (!isMacLengthExpected(theMacAlgo, result1.length)) {
            return false;
        }

        
        theMac.reset();
        theMac.update(plain[0]);
        theMac.update(plain, 1, OFFSET - 1);
        byte[] result2 = theMac.doFinal(tail);

        
        if (!java.util.Arrays.equals(result1, result2)) {
            System.out.println("result1 and result2 are not the same:");
            System.out.println("result1: " + dumpByteArray(result1));
            System.out.println("result2: " + dumpByteArray(result2));
            return false;
        } else {
            System.out.println("Resulted MAC with update and doFinal is same");
        }

        return true;
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

    
    protected boolean isMacLengthExpected(String MACAlgo, int lengthToCheck) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("(\\d+)",
                java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher m = p.matcher(MACAlgo);
        int val = 0;

        if (m.find()) {
            val = Integer.parseInt(m.group(1));
        }

        
        if ((val == 1) && (lengthToCheck == 20)) {
            return true;
        }

        return (val / 8) == lengthToCheck;
    }

    
    protected String dumpByteArray(byte[] theByteArray) {
        StringBuilder buf = new StringBuilder();

        for (byte b : theByteArray) {
            buf.append(Integer.toHexString(b));
        }

        return buf.toString();
    }

}
