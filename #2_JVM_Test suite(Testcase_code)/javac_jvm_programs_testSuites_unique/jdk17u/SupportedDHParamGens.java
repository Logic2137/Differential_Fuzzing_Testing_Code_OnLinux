import java.math.BigInteger;
import java.security.*;
import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;

public class SupportedDHParamGens {

    public static void main(String[] args) throws Exception {
        int primeSize = Integer.valueOf(args[0]).intValue();
        System.out.println("Checking " + primeSize + " ...");
        AlgorithmParameterGenerator apg = AlgorithmParameterGenerator.getInstance("DH", "SunJCE");
        apg.init(primeSize);
        AlgorithmParameters ap = apg.generateParameters();
        DHParameterSpec spec = ap.getParameterSpec(DHParameterSpec.class);
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DH", "SunJCE");
        kpg.initialize(spec);
        KeyPair kp = kpg.generateKeyPair();
        checkKeyPair(kp, primeSize);
    }

    private static void checkKeyPair(KeyPair kp, int pSize) throws Exception {
        DHPrivateKey privateKey = (DHPrivateKey) kp.getPrivate();
        BigInteger p = privateKey.getParams().getP();
        if (p.bitLength() != pSize) {
            throw new Exception("Invalid modulus size: " + p.bitLength() + "/" + pSize);
        }
        if (!p.isProbablePrime(128)) {
            throw new Exception("Good luck, the modulus is composite!");
        }
        DHPublicKey publicKey = (DHPublicKey) kp.getPublic();
        p = publicKey.getParams().getP();
        if (p.bitLength() != pSize) {
            throw new Exception("Invalid modulus size: " + p.bitLength() + "/" + pSize);
        }
        BigInteger leftOpen = BigInteger.ONE;
        BigInteger rightOpen = p.subtract(BigInteger.ONE);
        BigInteger x = privateKey.getX();
        if ((x.compareTo(leftOpen) <= 0) || (x.compareTo(rightOpen) >= 0)) {
            throw new Exception("X outside range [2, p - 2]:  x: " + x + " p: " + p);
        }
        BigInteger y = publicKey.getY();
        if ((y.compareTo(leftOpen) <= 0) || (y.compareTo(rightOpen) >= 0)) {
            throw new Exception("Y outside range [2, p - 2]:  x: " + x + " p: " + p);
        }
    }
}
