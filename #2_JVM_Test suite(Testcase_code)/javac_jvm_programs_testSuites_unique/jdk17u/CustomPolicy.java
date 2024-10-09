import java.security.AccessController;
import java.security.Permission;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;

public class CustomPolicy extends Policy {

    private final ProtectionDomain policyPd;

    public CustomPolicy() {
        policyPd = AccessController.doPrivileged((PrivilegedAction<ProtectionDomain>) () -> this.getClass().getProtectionDomain());
    }

    @Override
    public boolean implies(ProtectionDomain pd, Permission perm) {
        System.out.println("CustomPolicy.implies");
        if (pd == policyPd) {
            return true;
        }
        String home = AccessController.doPrivileged((PrivilegedAction<String>) () -> System.getProperty("user.home"));
        return true;
    }
}
