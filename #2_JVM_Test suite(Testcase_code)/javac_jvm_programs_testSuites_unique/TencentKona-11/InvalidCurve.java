



import java.security.*;
import java.security.spec.*;
import java.math.*;

public class InvalidCurve {

    public static void main(String[] args) {

        KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance("EC", "SunEC");
            ECGenParameterSpec brainpoolSpec =
                new ECGenParameterSpec("brainpoolP160r1");
            keyGen.initialize(brainpoolSpec);
        } catch (InvalidAlgorithmParameterException ex) {
            System.out.println(ex.getMessage());
            
            return;
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            throw new RuntimeException(ex);
        }

        keyGen.generateKeyPair();

        
        throw new RuntimeException("The expected exception was not thrown.");

    }

}

