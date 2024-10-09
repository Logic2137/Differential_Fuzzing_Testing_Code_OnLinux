import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.NamedParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHGenParameterSpec;

public class KeyAgreementTest {

    public static void main(String[] args) throws Exception {
        String kaAlgo = args[0];
        String kpgAlgo = args[1];
        String provider = args[2];
        System.out.println("Testing " + kaAlgo);
        AlgoSpec aSpec = AlgoSpec.valueOf(AlgoSpec.class, kaAlgo);
        List<AlgorithmParameterSpec> specs = aSpec.getAlgorithmParameterSpecs();
        for (AlgorithmParameterSpec spec : specs) {
            testKeyAgreement(provider, kaAlgo, kpgAlgo, spec);
        }
    }

    private enum AlgoSpec {

        ECDH("secp256r1", "secp384r1", "secp521r1"), XDH("X25519", "X448", "x25519"), DiffieHellman(new String[] {});

        private final List<AlgorithmParameterSpec> specs = new ArrayList<>();

        private AlgoSpec(String... curves) {
            for (String crv : curves) {
                switch(this.name()) {
                    case "ECDH":
                        specs.add(new ECGenParameterSpec(crv));
                        break;
                    case "XDH":
                        specs.add(new NamedParameterSpec(crv));
                        break;
                    case "DiffieHellman":
                        specs.add(new DHGenParameterSpec(512, 64));
                        break;
                    default:
                        throw new RuntimeException("Invalid Algo name " + this.name());
                }
            }
        }

        public List<AlgorithmParameterSpec> getAlgorithmParameterSpecs() {
            return this.specs;
        }
    }

    private static void testKeyAgreement(String provider, String kaAlgo, String kpgAlgo, AlgorithmParameterSpec spec) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(kpgAlgo, provider);
        kpg.initialize(spec);
        if (spec instanceof ECGenParameterSpec) {
            System.out.println("Testing curve: " + ((ECGenParameterSpec) spec).getName());
        } else if (spec instanceof NamedParameterSpec) {
            System.out.println("Testing curve: " + ((NamedParameterSpec) spec).getName());
        }
        KeyPair kp1 = kpg.generateKeyPair();
        KeyPair kp2 = kpg.generateKeyPair();
        KeyAgreement ka1 = KeyAgreement.getInstance(kaAlgo);
        ka1.init(kp1.getPrivate());
        ka1.doPhase(kp2.getPublic(), true);
        byte[] secret1 = ka1.generateSecret();
        KeyAgreement ka2 = KeyAgreement.getInstance(kaAlgo, provider);
        ka2.init(kp2.getPrivate());
        ka2.doPhase(kp1.getPublic(), true);
        byte[] secret2 = "DiffieHellman".equals(kaAlgo) ? ka2.generateSecret("AES").getEncoded() : ka2.generateSecret();
        if (!Arrays.equals(secret1, secret2)) {
            throw new Exception("KeyAgreement secret mismatch.");
        }
        try {
            ka2.generateSecret();
            throw new RuntimeException("state not reset");
        } catch (IllegalStateException ex) {
        }
        ka2.doPhase(kp1.getPublic(), true);
        ka2.generateSecret();
    }
}
