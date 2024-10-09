

package version;

import java.security.AccessControlException;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;

public class Version {
    private static final Permission PERM1 = new RuntimePermission("setIO");
    private static final Permission PERM2 = new RuntimePermission("setFactory");

    public int getVersion() {
        checkPermission(PERM1, false);
        checkPermission(PERM2, true);
        return 9;
    }

    private void checkPermission(Permission perm, boolean expectException) {
        boolean getException = (Boolean) AccessController
                .doPrivileged((PrivilegedAction) () -> {
            try {
                AccessController.checkPermission(perm);
                return (Boolean) false;
            } catch (AccessControlException ex) {
                return (Boolean) true;
            }
        });

        if (expectException ^ getException) {
            String message = "Check Permission :" + perm + "\n ExpectException = "
                    + expectException + "\n getException = " + getException;
            throw new RuntimeException(message);
        }
    }
}
