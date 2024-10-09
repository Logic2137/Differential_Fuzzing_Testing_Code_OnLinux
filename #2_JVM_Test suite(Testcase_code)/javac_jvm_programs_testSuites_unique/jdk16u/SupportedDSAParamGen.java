


import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;

public class SupportedDSAParamGen {

    public static void main(String[] args) throws Exception {
        AlgorithmParameterGenerator apg =
            AlgorithmParameterGenerator.getInstance("DSA", "SUN");

        DSAGenParameterSpec spec = new DSAGenParameterSpec(
                Integer.valueOf(args[0]).intValue(),
                Integer.valueOf(args[1]).intValue());

        System.out.println("Generating (" + spec.getPrimePLength() +
                ", " + spec.getSubprimeQLength() + ") DSA Parameters");
        long start = System.currentTimeMillis();
        apg.init(spec, null);
        AlgorithmParameters param = apg.generateParameters();
        long stop = System.currentTimeMillis();
        System.out.println("Time: " + (stop - start) + " ms.");
        checkParamStrength(param, spec);
    }

    private static void checkParamStrength(AlgorithmParameters param,
            DSAGenParameterSpec genParam) throws Exception {

        String algo = param.getAlgorithm();
        if (!algo.equalsIgnoreCase("DSA")) {
            throw new Exception("Unexpected type of parameters: " + algo);
        }

        DSAParameterSpec spec = param.getParameterSpec(DSAParameterSpec.class);
        int valueL = spec.getP().bitLength();
        int strength = genParam.getPrimePLength();
        if (strength != valueL) {
            System.out.println(
                    "P: Expected " + strength + " but actual " + valueL);
            throw new Exception("Wrong P strength");
        }

        int valueN = spec.getQ().bitLength();
        strength = genParam.getSubprimeQLength();
        if (strength != valueN) {
            System.out.println(
                    "Q: Expected " + strength + " but actual " + valueN);
            throw new Exception("Wrong Q strength");
        }
    }
}
