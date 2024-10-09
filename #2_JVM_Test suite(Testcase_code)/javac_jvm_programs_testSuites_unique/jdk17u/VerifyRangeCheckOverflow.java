import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.Signature;

public class VerifyRangeCheckOverflow {

    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(1024);
        KeyPair keys = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keys.getPublic();
        byte[] sigBytes = new byte[100];
        Signature signature = Signature.getInstance("SHA1withDSA");
        signature.initVerify(publicKey);
        try {
            signature.verify(sigBytes, Integer.MAX_VALUE, 1);
        } catch (IllegalArgumentException ex) {
        }
    }
}
