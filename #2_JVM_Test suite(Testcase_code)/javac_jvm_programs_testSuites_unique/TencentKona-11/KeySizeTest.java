

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;


public class KeySizeTest {

    
    private static final String KEYALG = "RSA";

    
    private static final String PROVIDER_NAME = "SunRsaSign";

    public static void main(String[] args) throws Exception {
        int iKeyPairSize = Integer.parseInt(args[0]);
        int maxLoopCnt = Integer.parseInt(args[1]);

        int failCount = 0;
        KeyPairGenerator keyPairGen
                = KeyPairGenerator.getInstance(KEYALG, PROVIDER_NAME);
        keyPairGen.initialize(iKeyPairSize);
        
        KeyPair keyPair = keyPairGen.generateKeyPair();

        
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        try {
            if (!sizeTest(keyPair)) {
                failCount++;
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            failCount++;
        }

        for (int iCnt = 0; iCnt < maxLoopCnt; iCnt++) {

            
            KeyFactory keyFact = KeyFactory.getInstance(KEYALG, PROVIDER_NAME);

            
            RSAPrivateKeySpec privateKeySpec
                    = (RSAPrivateKeySpec) keyFact.getKeySpec(privateKey,
                            RSAPrivateKeySpec.class);
            int iPrivateKeySize = privateKeySpec.getModulus().bitLength();

            RSAPublicKeySpec publicKeySpec
                    = (RSAPublicKeySpec) keyFact.getKeySpec(publicKey,
                            RSAPublicKeySpec.class);
            int iPublicKeySize = publicKeySpec.getModulus().bitLength();

            if ((iKeyPairSize != iPublicKeySize) || (iKeyPairSize != iPrivateKeySize)) {
                System.err.println("iKeyPairSize : " + iKeyPairSize);
                System.err.println("Generated a " + iPrivateKeySize
                        + " bit RSA private key");
                System.err.println("Generated a " + iPublicKeySize
                        + " bit RSA public key");
                failCount++;
            }
        }

        if (failCount > 0) {
            throw new RuntimeException("There are " + failCount + " tests failed.");
        }
    }

    
    private static boolean sizeTest(KeyPair kpair) {
        RSAPrivateKey priv = (RSAPrivateKey) kpair.getPrivate();
        RSAPublicKey pub = (RSAPublicKey) kpair.getPublic();

        
        if ((priv instanceof RSAKey) && (pub instanceof RSAKey)) {
            if (!priv.getModulus().equals(pub.getModulus())) {
                System.err.println("priv.getModulus() = " + priv.getModulus());
                System.err.println("pub.getModulus() = " + pub.getModulus());
                return false;
            }
        }
        return true;
    }
}
