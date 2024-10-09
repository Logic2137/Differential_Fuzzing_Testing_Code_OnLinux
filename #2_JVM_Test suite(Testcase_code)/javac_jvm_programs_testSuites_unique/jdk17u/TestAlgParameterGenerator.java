import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.spec.DSAGenParameterSpec;
import java.security.spec.DSAParameterSpec;

public class TestAlgParameterGenerator {

    private static void checkParamStrength(AlgorithmParameters param, int strength) throws Exception {
        String algo = param.getAlgorithm();
        if (!algo.equalsIgnoreCase("DSA")) {
            throw new RuntimeException("Unexpected type of parameters: " + algo);
        }
        DSAParameterSpec spec = param.getParameterSpec(DSAParameterSpec.class);
        int valueL = spec.getP().bitLength();
        if (strength != valueL) {
            System.out.println("Expected " + strength + " but actual " + valueL);
            throw new RuntimeException("Wrong P strength");
        }
    }

    private static void checkParamStrength(AlgorithmParameters param, DSAGenParameterSpec genParam) throws Exception {
        String algo = param.getAlgorithm();
        if (!algo.equalsIgnoreCase("DSA")) {
            throw new RuntimeException("Unexpected type of parameters: " + algo);
        }
        DSAParameterSpec spec = param.getParameterSpec(DSAParameterSpec.class);
        int valueL = spec.getP().bitLength();
        int strength = genParam.getPrimePLength();
        if (strength != valueL) {
            System.out.println("P: Expected " + strength + " but actual " + valueL);
            throw new RuntimeException("Wrong P strength");
        }
        int valueN = spec.getQ().bitLength();
        strength = genParam.getSubprimeQLength();
        if (strength != valueN) {
            System.out.println("Q: Expected " + strength + " but actual " + valueN);
            throw new RuntimeException("Wrong Q strength");
        }
    }

    public static void main(String[] args) throws Exception {
        AlgorithmParameterGenerator apg = AlgorithmParameterGenerator.getInstance("DSA", "SUN");
        long start, stop;
        start = System.currentTimeMillis();
        AlgorithmParameters param = apg.generateParameters();
        stop = System.currentTimeMillis();
        System.out.println("Time: " + (stop - start) + " ms.");
        int[] strengths = { 512, 768, 1024 };
        for (int sizeP : strengths) {
            System.out.println("Generating " + sizeP + "-bit DSA Parameters");
            start = System.currentTimeMillis();
            apg.init(sizeP);
            param = apg.generateParameters();
            stop = System.currentTimeMillis();
            System.out.println("Time: " + (stop - start) + " ms.");
            checkParamStrength(param, sizeP);
        }
        DSAGenParameterSpec[] specSet = { new DSAGenParameterSpec(1024, 160), new DSAGenParameterSpec(2048, 224), new DSAGenParameterSpec(2048, 256) };
        for (DSAGenParameterSpec genParam : specSet) {
            System.out.println("Generating (" + genParam.getPrimePLength() + ", " + genParam.getSubprimeQLength() + ") DSA Parameters");
            start = System.currentTimeMillis();
            apg.init(genParam, null);
            param = apg.generateParameters();
            stop = System.currentTimeMillis();
            System.out.println("Time: " + (stop - start) + " ms.");
            checkParamStrength(param, genParam);
        }
    }
}
