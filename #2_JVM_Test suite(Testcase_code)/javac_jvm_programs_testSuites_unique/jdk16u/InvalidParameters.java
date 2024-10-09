


import java.security.InvalidAlgorithmParameterException;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.TrustAnchor;
import java.util.Collections;
import java.util.Set;

public class InvalidParameters {

    public static void main(String[] args) throws Exception {

        
        try {
            PKIXBuilderParameters p =
                new PKIXBuilderParameters(Collections.EMPTY_SET, null);
            throw new Exception("should have thrown InvalidAlgorithmParameterExc");
        } catch (InvalidAlgorithmParameterException iape) { }

        
        try {
            PKIXBuilderParameters p = new PKIXBuilderParameters((Set) null, null);
            throw new Exception("should have thrown NullPointerException");
        } catch (NullPointerException npe) { }

        
        try {
            @SuppressWarnings("unchecked") 
            Set<TrustAnchor> badSet = (Set<TrustAnchor>) (Set) Collections.singleton(new String());
            PKIXBuilderParameters p =
                new PKIXBuilderParameters(badSet, null);
            throw new Exception("should have thrown ClassCastException");
        } catch (ClassCastException cce) { }
    }
}
