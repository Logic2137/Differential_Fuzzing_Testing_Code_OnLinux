

 
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
        AlgoSpec aSpec = AlgoSpec.valueOf(AlgoSpec.class, kaAlgo);
        List<AlgorithmParameterSpec> specs = aSpec.getAlgorithmParameterSpecs();
        for (AlgorithmParameterSpec spec : specs) {
            testKeyAgreement(provider, kaAlgo, kpgAlgo, spec);
        }
    }

    
    private enum AlgoSpec {
        
        
        
        
        
        ECDH(
                
                "secp112r1", "secp112r2", "secp128r1", "secp128r2", "secp160k1",
                "secp160r1", "secp192k1", "secp192r1", "secp224k1", "secp224r1",
                "secp256k1", "secp256r1", "secp384r1", "secp521r1",
                
                "X9.62 prime192v2", "X9.62 prime192v3", "X9.62 prime239v1",
                "X9.62 prime239v2", "X9.62 prime239v3",
                
                "sect113r1", "sect113r2", "sect131r1", "sect131r2", "sect163k1",
                "sect163r1", "sect163r2", "sect193r1", "sect193r2", "sect233k1",
                "sect233r1", "sect239k1", "sect283k1", "sect283r1", "sect409k1",
                "sect409r1", "sect571k1", "sect571r1",
                
                "X9.62 c2tnb191v1", "X9.62 c2tnb191v2", "X9.62 c2tnb191v3",
                "X9.62 c2tnb239v1", "X9.62 c2tnb239v2", "X9.62 c2tnb239v3",
                "X9.62 c2tnb359v1", "X9.62 c2tnb431r1"
        ),
        XDH("X25519", "X448"),
        
        DiffieHellman(new String[]{});

        private final List<AlgorithmParameterSpec> specs = new ArrayList<>();

        private AlgoSpec(String... curves) {
            
            for (String crv : curves) {
                switch (this.name()) {
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
                        throw new RuntimeException("Invalid Algo name "
                                + this.name());
                }
            }
        }

        public List<AlgorithmParameterSpec> getAlgorithmParameterSpecs() {
            return this.specs;
        }
    }

    
    private static void testKeyAgreement(String provider, String kaAlgo,
            String kpgAlgo, AlgorithmParameterSpec spec) throws Exception {

        KeyPairGenerator kpg = KeyPairGenerator.getInstance(kpgAlgo, provider);
        kpg.initialize(spec);
        KeyPair kp1 = kpg.generateKeyPair();
        KeyPair kp2 = kpg.generateKeyPair();

        
        KeyAgreement ka1 = KeyAgreement.getInstance(kaAlgo);
        ka1.init(kp1.getPrivate());
        ka1.doPhase(kp2.getPublic(), true);
        byte[] secret1 = ka1.generateSecret();

        
        KeyAgreement ka2 = KeyAgreement.getInstance(kaAlgo, provider);
        ka2.init(kp2.getPrivate());
        ka2.doPhase(kp1.getPublic(), true);
        
        
        
        byte[] secret2 = "DiffieHellman".equals(kaAlgo)
                ? ka2.generateSecret("AES").getEncoded() : ka2.generateSecret();

        
        
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
