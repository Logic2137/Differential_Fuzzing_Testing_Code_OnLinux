



import java.math.BigInteger;

import java.security.*;
import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;

public class SupportedDHKeys {

    
    private enum SupportedKeySize {
        dhp512(512),   dhp768(768),    dhp832(832),
        dhp1024(1024), dhp1536(1536),  dhp2048(2048),
        dhp3072(3072), dhp4096(4096),  dhp6144(6144),
        dhp8192(8192);

        final int primeSize;

        SupportedKeySize(int primeSize) {
            this.primeSize = primeSize;
        }
    }

    public static void main(String[] args) throws Exception {
        for (SupportedKeySize keySize : SupportedKeySize.values()) {
            System.out.println("Checking " + keySize.primeSize + " ...");
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DH", "SunJCE");
            kpg.initialize(keySize.primeSize);
            KeyPair kp = kpg.generateKeyPair();
            checkKeyPair(kp, keySize.primeSize);

            DHPublicKey publicKey = (DHPublicKey)kp.getPublic();
            BigInteger p = publicKey.getParams().getP();
            BigInteger g = publicKey.getParams().getG();
            kpg.initialize(new DHParameterSpec(p, g));
            kp = kpg.generateKeyPair();
            checkKeyPair(kp, keySize.primeSize);
        }
    }

    private static void checkKeyPair(KeyPair kp, int pSize) throws Exception {

        DHPrivateKey privateKey = (DHPrivateKey)kp.getPrivate();
        BigInteger p = privateKey.getParams().getP();
        if (p.bitLength() != pSize) {
            throw new Exception(
                "Invalid modulus size: " + p.bitLength() + "/" + pSize);
        }

        
        if (!p.isProbablePrime(128)) {
            throw new Exception("Good luck, the modulus is composite!");
        }

        DHPublicKey publicKey = (DHPublicKey)kp.getPublic();
        p = publicKey.getParams().getP();
        if (p.bitLength() != pSize) {
            throw new Exception(
                "Invalid modulus size: " + p.bitLength() + "/" + pSize);
        }

        BigInteger leftOpen = BigInteger.ONE;
        BigInteger rightOpen = p.subtract(BigInteger.ONE);

        BigInteger x = privateKey.getX();
        if ((x.compareTo(leftOpen) <= 0) ||
                (x.compareTo(rightOpen) >= 0)) {
            throw new Exception(
                "X outside range [2, p - 2]:  x: " + x + " p: " + p);
        }

        BigInteger y = publicKey.getY();
        if ((y.compareTo(leftOpen) <= 0) ||
                (y.compareTo(rightOpen) >= 0)) {
            throw new Exception(
                "Y outside range [2, p - 2]:  x: " + x + " p: " + p);
        }
    }
}
