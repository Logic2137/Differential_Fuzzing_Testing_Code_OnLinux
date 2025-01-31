

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.DSAGenParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.security.spec.InvalidParameterSpecException;


public class TestDSAGenParameterSpec {

    private static final String ALGORITHM_NAME = "DSA";
    private static final String PROVIDER_NAME = "SUN";

    private static void testDSAGenParameterSpec(DataTuple dataTuple)
            throws NoSuchAlgorithmException, NoSuchProviderException,
            InvalidParameterSpecException, InvalidAlgorithmParameterException {
        System.out.printf("Test case: primePLen=%d, " + "subprimeQLen=%d%n",
                dataTuple.primePLen, dataTuple.subprimeQLen);

        AlgorithmParameterGenerator apg
                = AlgorithmParameterGenerator.getInstance(ALGORITHM_NAME,
                        PROVIDER_NAME);

        DSAGenParameterSpec genParamSpec = createGenParameterSpec(dataTuple);
        
        if (genParamSpec == null) {
            return;
        }

        try {
            apg.init(genParamSpec, null);
            AlgorithmParameters param = apg.generateParameters();

            checkParam(param, genParamSpec);
            System.out.println("Test case passed");
        } catch (InvalidParameterException ipe) {
            throw new RuntimeException("Test case failed.", ipe);
        }
    }

    private static void checkParam(AlgorithmParameters param,
            DSAGenParameterSpec genParam) throws InvalidParameterSpecException,
            NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException {
        String algorithm = param.getAlgorithm();
        if (!algorithm.equalsIgnoreCase(ALGORITHM_NAME)) {
            throw new RuntimeException(
                    "Unexpected type of parameters: " + algorithm);
        }

        DSAParameterSpec spec = param.getParameterSpec(DSAParameterSpec.class);
        int valueL = spec.getP().bitLength();
        int strengthP = genParam.getPrimePLength();
        if (strengthP != valueL) {
            System.out.printf("P: Expected %d but actual %d%n", strengthP,
                    valueL);
            throw new RuntimeException("Wrong P strength");
        }

        int valueN = spec.getQ().bitLength();
        int strengthQ = genParam.getSubprimeQLength();
        if (strengthQ != valueN) {
            System.out.printf("Q: Expected %d but actual %d%n", strengthQ,
                    valueN);
            throw new RuntimeException("Wrong Q strength");
        }

        if (genParam.getSubprimeQLength() != genParam.getSeedLength()) {
            System.out.println("Defaut seed length should be the same as Q.");
            throw new RuntimeException("Wrong seed length");
        }

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM_NAME,
                PROVIDER_NAME);
        keyGen.initialize(spec);
    }

    private static DSAGenParameterSpec createGenParameterSpec(
            DataTuple dataTuple) {
        DSAGenParameterSpec genParamSpec = null;
        try {
            genParamSpec = new DSAGenParameterSpec(dataTuple.primePLen,
                    dataTuple.subprimeQLen);
            if (!dataTuple.isDSASpecSupported) {
                throw new RuntimeException(
                        "Test case failed: the key length must not supported");
            }
        } catch (IllegalArgumentException e) {
            if (!dataTuple.isDSASpecSupported) {
                System.out.println("Test case passed: expected "
                        + "IllegalArgumentException is caught");
            } else {
                throw new RuntimeException("Test case failed: unexpected "
                        + "IllegalArgumentException is thrown", e);
            }
        }

        return genParamSpec;
    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            throw new RuntimeException("Invalid number of arguments to generate"
                    + " DSA parameter.");
        }
        DataTuple dataTuple = null;
        switch (args.length) {
            case 3:
                dataTuple = new DataTuple(Integer.valueOf(args[0]),
                        Integer.valueOf(args[1]), Boolean.valueOf(args[2]));
                break;
            case 2:
                dataTuple = new DataTuple(Integer.valueOf(args[0]),
                        Integer.valueOf(args[1]), false);
                break;
            default:
                throw new RuntimeException("Unsupported arguments found.");
        }
        testDSAGenParameterSpec(dataTuple);

    }

    private static class DataTuple {

        private int primePLen;
        private int subprimeQLen;
        private boolean isDSASpecSupported;

        private DataTuple(int primePLen, int subprimeQLen,
                boolean isDSASpecSupported) {
            this.primePLen = primePLen;
            this.subprimeQLen = subprimeQLen;
            this.isDSASpecSupported = isDSASpecSupported;
        }
    }
}
