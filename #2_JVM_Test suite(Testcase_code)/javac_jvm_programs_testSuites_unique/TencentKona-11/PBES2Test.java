


import java.security.*;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.*;

public class PBES2Test {

    private static final String[] algos = {
        "PBEWithHmacSHA1AndAES_128",
        "PBEWithHmacSHA224AndAES_128",
        "PBEWithHmacSHA256AndAES_128",
        "PBEWithHmacSHA384AndAES_128",
        "PBEWithHmacSHA512AndAES_128"
    };
    private static final byte[] ivBytes = {
        0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,
        0x19,0x1A,0x1B,0x1C,0x1D,0x1E,0x1F,0x20,
    };

    public static final void main(String[] args) throws Exception {
        for (String algo : algos) {
            test(algo, true);  
            test(algo, false); 
        }
    }

    private static final void test(String algo, boolean suppliedParams)
        throws Exception {

        System.out.println("***********************************************");
        System.out.println(algo +
            (suppliedParams ? "  [algorithm parameters are supplied]\n"
                            : "  [algorithm parameters are generated]\n"));
        int iterationCount = 1000;
        byte[] salt = new byte[]{ 0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08 };

        
        PBEKeySpec pbeKeySpec = new PBEKeySpec("mypassword".toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algo);
        SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
        byte[] pbeKeyBytes = pbeKey.getEncoded();
        System.out.println("   key[" + pbeKeyBytes.length + "]: " +
            String.format("0x%0" + (pbeKeyBytes.length * 2) + "x",
                new java.math.BigInteger(1, pbeKeyBytes)));

        
        System.out.println("Encrypting...");
        Cipher pbeCipher = Cipher.getInstance(algo);
        if (suppliedParams) {
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey,
                new PBEParameterSpec(salt, iterationCount,
                    new IvParameterSpec(ivBytes)));
        } else {
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey);
        }

        
        byte[] cleartext = "This is just an example".getBytes();
        System.out.println("  text[" + cleartext.length + "]: " +
            String.format("0x%0" + (cleartext.length * 2) + "x",
                new java.math.BigInteger(1, cleartext)));

        byte[] ciphertext = pbeCipher.doFinal(cleartext);
        System.out.println("c'text[" + ciphertext.length + "]: " +
            String.format("0x%0" + (ciphertext.length * 2) + "x",
                new java.math.BigInteger(1, ciphertext)));

        AlgorithmParameters aps = pbeCipher.getParameters();

        byte[] iv;
        if (suppliedParams) {
            iv = ivBytes;
        } else {
            PBEParameterSpec pbeSpec =
                aps.getParameterSpec(PBEParameterSpec.class);
            salt = pbeSpec.getSalt();
            iterationCount = pbeSpec.getIterationCount();
            IvParameterSpec ivSpec =
                (IvParameterSpec) pbeSpec.getParameterSpec();
            iv = ivSpec.getIV();
        }
        System.out.println("  salt[" + salt.length + "]: " +
            String.format("0x%0" + (salt.length * 2) + "x",
                new java.math.BigInteger(1, salt)));
        System.out.println("iterationCount=" + iterationCount);
        System.out.println("    iv[" + iv.length + "]: " +
            String.format("0x%0" + (iv.length * 2) + "x",
                new java.math.BigInteger(1, iv)));

        
        System.out.println("Decrypting...");
        Cipher pbeCipher2 = Cipher.getInstance(algo);
        pbeCipher2.init(Cipher.DECRYPT_MODE, pbeKey, aps);
        byte[] cleartext2 = pbeCipher2.doFinal(ciphertext);
        System.out.println("  text[" + cleartext2.length + "]: " +
            String.format("0x%0" + (cleartext2.length * 2) + "x",
                new java.math.BigInteger(1, cleartext2)));

        if (Arrays.equals(cleartext, cleartext2)) {
            System.out.println(
                "\nPass: decrypted ciphertext matches the original text\n");
        } else {
            throw new Exception(
                "Fail: decrypted ciphertext does NOT match the original text");
        }
    }
}
