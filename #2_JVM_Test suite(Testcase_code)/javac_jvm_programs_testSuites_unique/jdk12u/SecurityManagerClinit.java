



import java.io.*;
import java.security.*;

public class SecurityManagerClinit {
    private static class Policy extends java.security.Policy {
        private Permissions perms;

        public Policy(Permission... permissions) {
            perms = new Permissions();
            for (Permission permission : permissions)
                perms.add(permission);
        }

        public boolean implies(ProtectionDomain pd, Permission p) {
            return perms.implies(p);
        }
    }

    public static void main(String[] args) throws Throwable {
        String javaExe =
            System.getProperty("java.home") +
            File.separator + "bin" + File.separator + "java";

        final Policy policy =
            new Policy
            (new FilePermission("<<ALL FILES>>", "execute"),
             new RuntimePermission("setSecurityManager"));
        Policy.setPolicy(policy);

        System.setSecurityManager(new SecurityManager());

        try {
            String[] cmd = { javaExe, "-version" };
            Process p = Runtime.getRuntime().exec(cmd);
            p.getOutputStream().close();
            p.getInputStream().close();
            p.getErrorStream().close();
            p.waitFor();
        } finally {
            System.setSecurityManager(null);
        }
    }
}
