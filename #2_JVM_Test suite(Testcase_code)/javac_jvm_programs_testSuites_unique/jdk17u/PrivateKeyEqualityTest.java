import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;

public class PrivateKeyEqualityTest {

    private static final String KEYALG = "RSA";

    private static final String PROVIDER_NAME = "SunRsaSign";

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEYALG, PROVIDER_NAME);
        KeyPair keyPair = generator.generateKeyPair();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        if (!(rsaPrivateKey instanceof RSAPrivateCrtKey)) {
            System.err.println("rsaPrivateKey class : " + rsaPrivateKey.getClass().getName());
            throw new RuntimeException("rsaPrivateKey is not a RSAPrivateCrtKey instance");
        }
        KeyFactory factory = KeyFactory.getInstance(KEYALG, PROVIDER_NAME);
        RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(rsaPrivateKey.getModulus(), rsaPrivateKey.getPrivateExponent());
        RSAPrivateKey rsaPrivateKey2 = (RSAPrivateKey) factory.generatePrivate(rsaPrivateKeySpec);
        PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        RSAPrivateKey rsaPrivateKey3 = (RSAPrivateKey) factory.generatePrivate(encodedKeySpec);
        if (rsaPrivateKey.equals(rsaPrivateKey2)) {
            throw new RuntimeException("rsaPrivateKey should not equal to rsaPrivateKey2");
        }
        if (!rsaPrivateKey3.equals(rsaPrivateKey)) {
            throw new RuntimeException("rsaPrivateKey3 should equal to rsaPrivateKey");
        }
        if (rsaPrivateKey3.equals(rsaPrivateKey2)) {
            throw new RuntimeException("rsaPrivateKey3 should not equal to rsaPrivateKey2");
        }
        if (rsaPrivateKey2.equals(rsaPrivateKey3)) {
            throw new RuntimeException("rsaPrivateKey2 should not equal to rsaPrivateKey3");
        }
        RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey) rsaPrivateKey;
        RSAPrivateCrtKeySpec rsaPrivateCrtKeySpec = new RSAPrivateCrtKeySpec(rsaPrivateCrtKey.getModulus(), rsaPrivateCrtKey.getPublicExponent(), rsaPrivateCrtKey.getPrivateExponent(), rsaPrivateCrtKey.getPrimeP(), rsaPrivateCrtKey.getPrimeQ(), rsaPrivateCrtKey.getPrimeExponentP(), rsaPrivateCrtKey.getPrimeExponentQ(), rsaPrivateCrtKey.getCrtCoefficient());
        RSAPrivateCrtKey rsaPrivateKey4 = (RSAPrivateCrtKey) factory.generatePrivate(rsaPrivateCrtKeySpec);
        if (!rsaPrivateKey.equals(rsaPrivateKey4)) {
            throw new RuntimeException("rsaPrivateKey should equal to rsaPrivateKey4");
        }
    }
}
