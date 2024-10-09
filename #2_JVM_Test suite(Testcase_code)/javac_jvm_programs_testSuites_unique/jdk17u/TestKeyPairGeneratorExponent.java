import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;

public class TestKeyPairGeneratorExponent {

    private static int keyLen = 512;

    private static BigInteger[] validExponents = new BigInteger[] { RSAKeyGenParameterSpec.F0, RSAKeyGenParameterSpec.F4, BigInteger.ONE.shiftLeft(keyLen - 1).subtract(BigInteger.ONE) };

    private static BigInteger[] invalidExponents = new BigInteger[] { BigInteger.valueOf(-1), BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(4) };

    public static void testValidExponents(KeyPairGenerator kpg, BigInteger exponent) {
        System.out.println("Testing exponent = " + exponent.toString(16));
        try {
            kpg.initialize(new RSAKeyGenParameterSpec(keyLen, exponent));
            kpg.generateKeyPair();
            System.out.println("OK, key pair generated");
        } catch (InvalidAlgorithmParameterException iape) {
            throw new RuntimeException("Error: Unexpected Exception: " + iape);
        }
    }

    public static void testInvalidExponents(KeyPairGenerator kpg, BigInteger exponent) {
        System.out.println("Testing exponent = " + exponent.toString(16));
        try {
            kpg.initialize(new RSAKeyGenParameterSpec(keyLen, exponent));
            kpg.generateKeyPair();
            throw new RuntimeException("Error: Expected IAPE not thrown.");
        } catch (InvalidAlgorithmParameterException iape) {
            System.out.println("OK, expected IAPE thrown");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error: unexpected exception " + e);
        }
    }

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "SunRsaSign");
        for (BigInteger validExponent : validExponents) {
            testValidExponents(kpg, validExponent);
        }
        for (BigInteger invalidExponent : invalidExponents) {
            testInvalidExponents(kpg, invalidExponent);
        }
    }
}
