



import javax.crypto.KeyAgreement;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;

public class ECKeyCheck {

    public static final void main(String args[]) throws Exception {
        ECGenParameterSpec spec = new ECGenParameterSpec("secp256r1");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(spec);

        ECPrivateKey privKey = (ECPrivateKey) kpg.generateKeyPair().getPrivate();
        ECPublicKey pubKey = (ECPublicKey) kpg.generateKeyPair().getPublic();
        generateECDHSecret(privKey, pubKey);
        generateECDHSecret(new newPrivateKeyImpl(privKey), pubKey);
    }

    private static byte[] generateECDHSecret(ECPrivateKey privKey,
        ECPublicKey pubKey) throws Exception {
        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
        ka.init(privKey);
        ka.doPhase(pubKey, true);
        return ka.generateSecret();
    }

    
    private static class newPrivateKeyImpl implements ECPrivateKey {
        private ECPrivateKey p;

        newPrivateKeyImpl(ECPrivateKey p) {
            this.p = p;
        }

        public BigInteger getS() {
            return p.getS();
        }

        public byte[] getEncoded() {
            return p.getEncoded();
        }

        public String getFormat() {
            return p.getFormat();
        }

        public String getAlgorithm() {
            return p.getAlgorithm();
        }

        public ECParameterSpec getParams() {
            return p.getParams();
        }
    }
}
