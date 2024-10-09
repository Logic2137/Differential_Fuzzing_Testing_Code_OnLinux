



import sun.security.x509.PolicyConstraintsExtension;
import sun.security.x509.PolicyMappingsExtension;

public class DefaultCriticality {
    public static void main(String [] args) throws Exception {
        PolicyConstraintsExtension pce = new PolicyConstraintsExtension(-1,-1);
        if (!pce.isCritical()) {
            throw new Exception("PolicyConstraintsExtension should be " +
                                "critical by default");
        }

        PolicyMappingsExtension pme = new PolicyMappingsExtension();
        if (!pme.isCritical()) {
            throw new Exception("PolicyMappingsExtension should be " +
                                "critical by default");
        }

        System.out.println("Test passed.");
    }
}
