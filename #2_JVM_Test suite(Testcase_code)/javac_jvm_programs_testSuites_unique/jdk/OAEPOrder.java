

import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.spec.MGF1ParameterSpec;
import java.util.Arrays;



public class OAEPOrder {
    public static void main(String[] args) throws Exception {
        
        OAEPParameterSpec spec = new OAEPParameterSpec(
                "SHA-384", "MGF1", MGF1ParameterSpec.SHA384,
                new PSource.PSpecified(new byte[10]));
        AlgorithmParameters alg = AlgorithmParameters.getInstance("OAEP");
        alg.init(spec);
        byte[] encoded = alg.getEncoded();

        
        
        byte[] a0 = Arrays.copyOfRange(encoded, 2, encoded[3] + 4);
        
        byte[] a12 = Arrays.copyOfRange(encoded, 2 + a0.length, encoded.length);

        
        System.arraycopy(a12, 0, encoded, 2, a12.length);
        System.arraycopy(a0, 0, encoded, 2 + a12.length, a0.length);

        AlgorithmParameters alg2 = AlgorithmParameters.getInstance("OAEP");
        try {
            alg2.init(encoded);
            throw new RuntimeException("Should fail");
        } catch (IOException ioe) {
            
        }
    }
}
