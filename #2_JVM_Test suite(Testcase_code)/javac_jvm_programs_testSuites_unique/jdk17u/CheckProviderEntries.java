import java.security.*;
import java.util.Iterator;
import java.security.Provider.Service;

public class CheckProviderEntries {

    private static boolean testResult = true;

    private static void error(String msg) {
        testResult = false;
        System.out.println(msg);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Provider p = Security.getProvider("SunJSSE");
        Iterator<Provider.Service> iter = p.getServices().iterator();
        while (iter.hasNext()) {
            Service s = iter.next();
            String type = s.getType();
            String algo = s.getAlgorithm();
            System.out.println("Type: " + type + " " + algo);
            try {
                if (algo.indexOf("RSA") != -1) {
                    if (type.equals("Signature") && algo.equals("MD5andSHA1withRSA")) {
                        s.newInstance(null);
                        continue;
                    }
                    error("Error: unexpected RSA services");
                }
            } catch (NoSuchAlgorithmException | InvalidParameterException e) {
                error("Error: cannot create obj " + e);
            }
        }
        if (testResult) {
            System.out.println("Test Passed");
        } else {
            throw new RuntimeException("One or more tests failed");
        }
    }
}
