import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Arrays;
import java.util.stream.IntStream;
import static javax.crypto.Cipher.PRIVATE_KEY;
import static javax.crypto.Cipher.PUBLIC_KEY;

public class PSSParametersTest {

    private static final String PROVIDER = "SunRsaSign";

    private static final String PSS_ALGO = "RSASSA-PSS";

    private static final String PSS_OID = "1.2.840.113549.1.1.10";

    public static void main(String[] args) throws Exception {
        System.out.println("Testing against DEFAULT parameters");
        test(PSSParameterSpec.DEFAULT);
        System.out.println("Testing against custom parameters");
        test(new PSSParameterSpec("SHA-512/224", "MGF1", MGF1ParameterSpec.SHA384, 100, 1));
        test(new PSSParameterSpec("SHA3-256", "MGF1", new MGF1ParameterSpec("SHA3-256"), 256 >> 3, 1));
        System.out.println("Test Passed");
    }

    private static void test(PSSParameterSpec spec) throws Exception {
        System.out.println("Testing PSS spec: " + spec);
        String[] ALGORITHMS = { PSS_ALGO, PSS_OID };
        for (String alg : ALGORITHMS) {
            AlgorithmParameters params = AlgorithmParameters.getInstance(alg, PROVIDER);
            params.init(spec);
            byte[] encoded = params.getEncoded();
            AlgorithmParameters params2 = AlgorithmParameters.getInstance(alg, PROVIDER);
            params2.init(encoded);
            PSSParameterSpec spec2 = params2.getParameterSpec(PSSParameterSpec.class);
            if (!isEqual(spec, spec2)) {
                throw new RuntimeException("Spec check Failed for " + alg);
            }
        }
    }

    private static boolean isEqual(PSSParameterSpec spec, PSSParameterSpec spec2) throws Exception {
        if (spec == spec2)
            return true;
        if (spec == null || spec2 == null)
            return false;
        if (!spec.getDigestAlgorithm().equals(spec2.getDigestAlgorithm())) {
            System.out.println("Different digest algorithms: " + spec.getDigestAlgorithm() + " vs " + spec2.getDigestAlgorithm());
            return false;
        }
        if (!spec.getMGFAlgorithm().equals(spec2.getMGFAlgorithm())) {
            System.out.println("Different MGF algorithms: " + spec.getMGFAlgorithm() + " vs " + spec2.getMGFAlgorithm());
            return false;
        }
        if (spec.getSaltLength() != spec2.getSaltLength()) {
            System.out.println("Different Salt Length: " + spec.getSaltLength() + " vs " + spec2.getSaltLength());
            return false;
        }
        if (spec.getTrailerField() != spec2.getTrailerField()) {
            System.out.println("Different TrailerField: " + spec.getTrailerField() + " vs " + spec2.getTrailerField());
            return false;
        }
        AlgorithmParameterSpec mgfParams = spec.getMGFParameters();
        AlgorithmParameterSpec mgfParams2 = spec2.getMGFParameters();
        if (mgfParams == mgfParams2)
            return true;
        if (mgfParams == null || mgfParams2 == null) {
            System.out.println("Different MGF Parameters: " + mgfParams + " vs " + mgfParams2);
            return false;
        }
        if (mgfParams instanceof MGF1ParameterSpec) {
            if (mgfParams2 instanceof MGF1ParameterSpec) {
                boolean result = ((MGF1ParameterSpec) mgfParams).getDigestAlgorithm().equals(((MGF1ParameterSpec) mgfParams2).getDigestAlgorithm());
                if (!result) {
                    System.out.println("Different MGF1 digest algorithms: " + ((MGF1ParameterSpec) mgfParams).getDigestAlgorithm() + " vs " + ((MGF1ParameterSpec) mgfParams2).getDigestAlgorithm());
                }
                return result;
            } else {
                System.out.println("Different MGF Parameters types: " + mgfParams.getClass() + " vs " + mgfParams2.getClass());
                return false;
            }
        }
        throw new RuntimeException("Unrecognized MGFParameters: " + mgfParams);
    }
}
