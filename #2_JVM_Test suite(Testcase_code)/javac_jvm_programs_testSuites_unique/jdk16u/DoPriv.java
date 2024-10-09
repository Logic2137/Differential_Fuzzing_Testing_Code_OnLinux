


import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.DomainCombiner;
import java.security.ProtectionDomain;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DoPriv {

    static void go(final DomainCombiner dc0, final AccessControlContext co, final int index) throws Exception {
        final AccessControlContext ci = new AccessControlContext(co, dc0);
        AccessController.doPrivileged((PrivilegedExceptionAction<Integer>)() -> {
            AccessControlContext c1 = AccessController.getContext();
            DomainCombiner dc = c1.getDomainCombiner();
            if (dc != dc0 || dc == null) {
                throw new AssertionError("iteration " + index + " " + dc + " != " + dc0);
            }
            return 0;
        }, ci);
    }

    public static void main(String[] args) throws Exception {
        final DomainCombiner dc0 = new DomainCombiner() {
            public ProtectionDomain[] combine(ProtectionDomain[] currentDomains,
                                            ProtectionDomain[] assignedDomains) {
                return null;
            }
        };

        final AccessControlContext co = AccessController.getContext();

        for (int i = 0; i < 500_000; ++i) {
            go(dc0, co, i);
        }
    }
}
