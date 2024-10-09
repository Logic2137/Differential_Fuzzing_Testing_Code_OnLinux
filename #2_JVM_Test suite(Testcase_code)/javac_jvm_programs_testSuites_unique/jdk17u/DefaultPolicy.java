import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.security.AllPermission;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.security.URIParameter;

public class DefaultPolicy {

    public static void main(String[] args) throws Exception {
        Policy p = Policy.getPolicy();
        checkPolicy(p);
        System.setProperty("java.security.policy", "Extra.policy");
        p.refresh();
        checkPolicy(p);
        System.setProperty("java.security.policy", "=Extra.policy");
        p.refresh();
        checkPolicy(p);
        URI policyURI = Paths.get(System.getProperty("test.src"), "Extra.policy").toUri();
        p = Policy.getInstance("JavaPolicy", new URIParameter(policyURI));
        checkPolicy(p);
    }

    private static void checkPolicy(Policy p) throws Exception {
        CodeSource cs = new CodeSource(new URL("jrt:/jdk.crypto.ec"), (CodeSigner[]) null);
        ProtectionDomain pd = new ProtectionDomain(cs, null, null, null);
        if (p.implies(pd, new AllPermission())) {
            throw new Exception("module should not be granted AllPermission");
        }
        if (!p.implies(pd, new RuntimePermission("loadLibrary.sunec"))) {
            throw new Exception("module should be granted RuntimePermission");
        }
    }
}
