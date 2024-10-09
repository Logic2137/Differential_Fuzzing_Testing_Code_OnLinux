


import java.security.AlgorithmParameterGenerator;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHGenParameterSpec;
import javax.crypto.spec.DHParameterSpec;

public class SameDHKeyStressTest {

    static final String[] ALGORITHMS = {"DH", "DiffieHellman", "dh", "diffieHELLMAN"};
    static final String[] SECRET_ALOGRITHMS = {"DES", "DESede", "blowfish"};
    static final int[] NUMBER_OF_PARTIES = {2, 3, 4};
    static final String[] PA_NAMES = {"Alice", "Bob", "Carol", "David"};

    public static void main(String args[]) {
        int failedCnt = 0;
        StringBuilder failedList = new StringBuilder("Failed List:");

        for (String algorithm : ALGORITHMS) {
            for (int numOfParties : NUMBER_OF_PARTIES) {
                for (String secretAlgorithm : SECRET_ALOGRITHMS) {
                    if (!runTest(algorithm, numOfParties, secretAlgorithm)) {
                        failedCnt++;
                        failedList.append("\n Altorightm = ").append(algorithm).
                                append(" Number of Parties = ").append(numOfParties).
                                append(" Secret Algorithm = ").append(secretAlgorithm);
                    }
                }
            }
        } 

        if (failedCnt > 0) {
            System.out.println(failedList);
            throw new RuntimeException("SameDHKeyStressTest Failed");
        }
    }

    public static boolean runTest(String algo, int numParties, String secretAlgo) {
        KAParticipant[] parties = new KAParticipant[numParties];
        Key[] keyArchives = new Key[numParties];
        try {
            
            AlgorithmParameterGenerator apg = AlgorithmParameterGenerator.getInstance("DH","SunJCE");
            AlgorithmParameterSpec aps = new DHGenParameterSpec(512, 64);
            apg.init(aps);
            DHParameterSpec spec = apg.generateParameters().
                    getParameterSpec(DHParameterSpec.class);

            
            for (int i = 0; i < numParties; i++) {
                parties[i] = new KAParticipant(PA_NAMES[i], algo);
                parties[i].initialize(spec);
                keyArchives[i] = parties[i].getPublicKey();
            }

            
            Key[] keyBuffer = new Key[numParties];
            boolean lastPhase = false;
            for (int j = 0; j < numParties - 1; j++) {
                if (j == numParties - 2) {
                    lastPhase = true;
                }
                for (int k = 0; k < numParties; k++) {
                    if (k == numParties - 1) {
                        keyBuffer[k] = parties[k].doPhase(keyArchives[0], lastPhase);
                    } else {
                        keyBuffer[k] = parties[k].doPhase(keyArchives[k + 1], lastPhase);
                    }
                }
                System.arraycopy(keyBuffer, 0, keyArchives, 0, numParties);
            }

            
            SecretKey[] sKeys = new SecretKey[numParties];
            for (int n = 0; n < numParties; n++) {
                sKeys[n] = parties[n].generateSecret(secretAlgo);
            }
            for (int q = 0; q < numParties - 1; q++) {
                if (!Arrays.equals(sKeys[q].getEncoded(), sKeys[q + 1].getEncoded())) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

}

class KAParticipant {

    private String name = null;
    private String algorithm = null;
    private KeyPairGenerator keyGen = null;
    private KeyPair keys = null;
    private KeyAgreement ka = null;

    public KAParticipant(String pName, String algo) throws NoSuchAlgorithmException, NoSuchProviderException {
        name = pName;
        algorithm = algo;
        keyGen = KeyPairGenerator.getInstance(algo,"SunJCE");
        ka = KeyAgreement.getInstance(algo,"SunJCE");
    }

    public void initialize(AlgorithmParameterSpec spec) throws InvalidAlgorithmParameterException, InvalidKeyException {
        keyGen.initialize(spec);
        keys = keyGen.generateKeyPair();
        ka.init(keys.getPrivate());
    }

    public Key doPhase(Key key, boolean lastPhase) throws InvalidKeyException {
        return ka.doPhase(key, lastPhase);
    }

    public Key getPublicKey() {
        return keys.getPublic();
    }

    public byte[] generateSecret() {
        return ka.generateSecret();
    }

    public SecretKey generateSecret(String algo) throws java.lang.IllegalStateException,
            java.security.NoSuchAlgorithmException,
            java.security.InvalidKeyException {
        return ka.generateSecret(algo);
    }
}
