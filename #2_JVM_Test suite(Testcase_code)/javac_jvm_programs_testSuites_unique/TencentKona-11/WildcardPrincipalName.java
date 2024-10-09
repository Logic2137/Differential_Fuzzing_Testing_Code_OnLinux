



import java.security.AccessController;
import java.security.Permission;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.PropertyPermission;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;

public class WildcardPrincipalName {

    public static void main(String[] args) throws Exception {

        X500Principal duke = new X500Principal("CN=Duke");
        PropertyPermission pp = new PropertyPermission("user.home", "read");
        RunAsPrivilegedUserAction runAsPrivilegedUserAction
            = new RunAsPrivilegedUserAction(duke,
                                            new CheckPermissionAction(pp));
        AccessController.doPrivileged(runAsPrivilegedUserAction);
        System.out.println("test PASSED");
    }

    private static class RunAsPrivilegedUserAction
        implements PrivilegedAction<Void> {
        private final PrivilegedAction<Void> action;
        private final Principal principal;

        RunAsPrivilegedUserAction(Principal principal,
                                  PrivilegedAction<Void> action) {
            this.principal = principal;
            this.action = action;
        }

        @Override public Void run() {
            Set<Principal> principals = new HashSet<>();
            Set<Object> publicCredentials = new HashSet<>();
            Set<Object> privateCredentials = new HashSet<>();

            principals.add(principal);
            Subject subject = new Subject(true,
                                          principals,
                                          publicCredentials,
                                          privateCredentials);

            Subject.doAsPrivileged(subject, action, null);
            return null;
        }
    }

    private static class CheckPermissionAction
        implements PrivilegedAction<Void> {
        private final Permission permission;

        CheckPermissionAction(Permission permission) {
            this.permission = permission;
        }

        @Override public Void run() {
            AccessController.checkPermission(permission);
            return null;
        }
    }
}
