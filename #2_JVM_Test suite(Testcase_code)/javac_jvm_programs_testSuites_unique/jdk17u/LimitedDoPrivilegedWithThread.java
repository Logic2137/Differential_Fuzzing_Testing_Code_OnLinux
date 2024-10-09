import java.io.FilePermission;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.PropertyPermission;

public class LimitedDoPrivilegedWithThread {

    private static final Permission PROPERTYPERM = new PropertyPermission("user.name", "read");

    private static final Permission FILEPERM = new FilePermission("*", "read");

    private static final AccessControlContext ACC = new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, null) });

    public static void main(String[] args) {
        AccessController.doPrivileged((PrivilegedAction) () -> {
            Thread ct = new Thread(new ChildThread(PROPERTYPERM, FILEPERM));
            ct.start();
            try {
                ct.join();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                ie.printStackTrace();
                throw new RuntimeException("Unexpected InterruptedException");
            }
            return null;
        }, ACC);
    }
}

class ChildThread implements Runnable {

    private final Permission P1;

    private final Permission P2;

    private boolean catchACE = false;

    public ChildThread(Permission p1, Permission p2) {
        this.P1 = p1;
        this.P2 = p2;
    }

    @Override
    public void run() {
        runTest(null, P1, false, 1);
        AccessControlContext childAcc = AccessController.getContext();
        runTest(childAcc, P1, true, 2);
        runTest(null, P2, true, 3);
    }

    public void runTest(AccessControlContext acc, Permission perm, boolean expectACE, int id) {
        AccessController.doPrivileged((PrivilegedAction) () -> {
            try {
                AccessController.getContext().checkPermission(P1);
            } catch (AccessControlException ace) {
                catchACE = true;
            }
            if (catchACE ^ expectACE) {
                throw new RuntimeException("test" + id + " failed");
            }
            return null;
        }, acc, perm);
    }
}
