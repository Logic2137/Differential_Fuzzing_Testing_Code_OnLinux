import java.security.*;
import java.util.*;

public class LimitedDoPrivileged {

    private static final ProtectionDomain domain = new ProtectionDomain(null, null, null, null);

    private static final AccessControlContext acc = new AccessControlContext(new ProtectionDomain[] { domain });

    private static final PropertyPermission pathPerm = new PropertyPermission("path.separator", "read");

    private static final PropertyPermission filePerm = new PropertyPermission("file.separator", "read");

    public static void main(String[] args) throws Exception {
        AccessController.getContext().checkPermission(filePerm);
        AccessController.getContext().checkPermission(pathPerm);
        System.out.println("test 1 passed");
        AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                try {
                    AccessController.getContext().checkPermission(pathPerm);
                } catch (AccessControlException ace) {
                    System.out.println("test 2 passed");
                }
                AccessController.doPrivileged(new PrivilegedAction() {

                    public Object run() {
                        AccessController.getContext().checkPermission(pathPerm);
                        return null;
                    }
                }, null, new PropertyPermission("path.*", "read"));
                System.out.println("test 3 passed");
                try {
                    AccessController.doPrivileged(new PrivilegedAction() {

                        public Object run() {
                            AccessController.getContext().checkPermission(filePerm);
                            return null;
                        }
                    }, null, new PropertyPermission("path.*", "read"));
                } catch (AccessControlException ace) {
                    System.out.println("test 4 passed");
                }
                final AccessControlContext context = AccessController.getContext();
                try {
                    AccessController.doPrivileged(new PrivilegedAction() {

                        public Object run() {
                            AccessController.getContext().checkPermission(pathPerm);
                            return null;
                        }
                    }, context, new PropertyPermission("path.*", "read"));
                } catch (AccessControlException ace) {
                    System.out.println("test 5 passed");
                }
                AccessController.doPrivileged(new PrivilegedAction() {

                    public Object run() {
                        AccessController.getContext().checkPermission(pathPerm);
                        return null;
                    }
                });
                System.out.println("test 6 passed");
                try {
                    AccessController.doPrivileged(new PrivilegedAction() {

                        public Object run() {
                            AccessController.getContext().checkPermission(pathPerm);
                            return null;
                        }
                    }, context);
                } catch (AccessControlException ace) {
                    System.out.println("test 7 passed");
                }
                AccessController.doPrivileged(new PrivilegedAction() {

                    public Object run() {
                        final AccessControlContext limitedContext = AccessController.getContext();
                        AccessController.doPrivileged(new PrivilegedAction() {

                            public Object run() {
                                AccessController.getContext().checkPermission(pathPerm);
                                return null;
                            }
                        }, limitedContext);
                        return null;
                    }
                }, null, new PropertyPermission("path.*", "read"));
                System.out.println("test 8 passed");
                AccessController.doPrivileged(new PrivilegedAction() {

                    public Object run() {
                        final AccessControlContext limitedContext = AccessController.getContext();
                        try {
                            AccessController.doPrivileged(new PrivilegedAction() {

                                public Object run() {
                                    AccessController.getContext().checkPermission(filePerm);
                                    return null;
                                }
                            }, limitedContext);
                        } catch (AccessControlException ace) {
                            System.out.println("test 9 passed");
                        }
                        return null;
                    }
                }, null, new PropertyPermission("path.*", "read"));
                return null;
            }
        }, acc);
    }
}
