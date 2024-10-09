



import java.io.File;
import java.net.URI;
import java.security.AccessControlException;
import java.security.Policy;
import java.security.URIParameter;

public class BadPolicyFile {

    public static void main(String[] args) throws Exception {
        URI uri = new File(System.getProperty("test.src", "."),
                           "BadPolicyFile.policy").toURI();
        Policy.setPolicy(Policy.getInstance("JavaPolicy", new URIParameter(uri)));
        System.setSecurityManager(new SecurityManager());
        try {
            String javahome = System.getProperty("java.home");
            throw new Exception("Expected AccessControlException");
        } catch (AccessControlException ace) {
            System.out.println("Test PASSED");
        }
    }
}
