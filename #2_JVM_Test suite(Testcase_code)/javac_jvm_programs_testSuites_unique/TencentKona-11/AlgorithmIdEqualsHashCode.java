



import java.io.*;
import java.security.AlgorithmParameters;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;

import sun.security.util.DerValue;
import sun.security.x509.*;

public class AlgorithmIdEqualsHashCode {

    public static void main(String[] args) throws Exception {

        AlgorithmId ai1 = AlgorithmId.get("DH");
        AlgorithmId ai2 = AlgorithmId.get("DH");
        AlgorithmId ai3 = AlgorithmId.get("DH");

        
        
        
        

        if ( (ai1.equals(ai2)) == (ai2.equals(ai3)) == (ai1.equals(ai3)))
            System.out.println("PASSED transitivity test");
        else
            throw new Exception("Failed equals transitivity() contract");

        if ( (ai1.equals(ai2)) == (ai1.hashCode()==ai2.hashCode()) )
            System.out.println("PASSED equals()/hashCode() test");
        else
            throw new Exception("Failed equals()/hashCode() contract");

        
        
        AlgorithmParameters algParams1 =
            AlgorithmParameters.getInstance("RSASSA-PSS");
        AlgorithmParameters algParams2 =
            AlgorithmParameters.getInstance("RSASSA-PSS");
        algParams1.init(new PSSParameterSpec("SHA-1", "MGF1",
            MGF1ParameterSpec.SHA1, 20, PSSParameterSpec.TRAILER_FIELD_BC));
        algParams2.init(new PSSParameterSpec("SHA-256", "MGF1",
            MGF1ParameterSpec.SHA1, 20, PSSParameterSpec.TRAILER_FIELD_BC));
        ai1 = new AlgorithmId(AlgorithmId.RSASSA_PSS_oid, algParams1);
        ai2 = new AlgorithmId(AlgorithmId.RSASSA_PSS_oid, algParams2);
        if (ai1.equals(ai2)) {
            throw new Exception("Failed equals() contract");
        } else {
            System.out.println("PASSED equals() test");
        }

        
        
        
        byte[] encoded = ai1.encode();
        ai3 = AlgorithmId.parse(new DerValue(encoded));
        if (!ai1.equals(ai3)) {
            throw new Exception("Failed equals() contract");
        } else {
            System.out.println("PASSED equals() test");
        }

        
        
        
        if (ai2.equals(ai3)) {
            throw new Exception("Failed equals() contract");
        } else {
            System.out.println("PASSED equals() test");
        }
    }
}
