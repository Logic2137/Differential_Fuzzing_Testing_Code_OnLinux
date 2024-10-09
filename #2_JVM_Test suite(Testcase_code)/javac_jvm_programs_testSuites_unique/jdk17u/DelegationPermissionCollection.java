import java.security.Permission;
import java.security.PermissionCollection;
import java.security.SecurityPermission;
import java.util.Enumeration;
import javax.security.auth.kerberos.DelegationPermission;

public class DelegationPermissionCollection {

    private static final String FOO = "\"host/foo.example.com@EXAMPLE.COM\"";

    private static final String BAR = "\"host/bar.example.com@EXAMPLE.COM\"";

    private static final String TGT = "\"krbtgt/EXAMPLE.COM@EXAMPLE.COM\"";

    public static void main(String[] args) throws Exception {
        int testFail = 0;
        DelegationPermission perm = new DelegationPermission(FOO + " " + TGT);
        PermissionCollection perms = perm.newPermissionCollection();
        System.out.println("test 1: add throws IllegalArgException for wrong perm type");
        try {
            perms.add(new SecurityPermission("createAccessControlContext"));
            System.err.println("Expected IllegalArgumentException");
            testFail++;
        } catch (IllegalArgumentException iae) {
        }
        System.out.println("test 2: implies returns false for wrong perm type");
        if (perms.implies(new SecurityPermission("getPolicy"))) {
            System.err.println("Expected false, returned true");
            testFail++;
        }
        System.out.println("test 3: implies returns true for match on name");
        perms.add(new DelegationPermission(FOO + " " + TGT));
        if (!perms.implies(new DelegationPermission(FOO + " " + TGT))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }
        System.out.println("test 4: implies returns false for non-match on name");
        if (perms.implies(new DelegationPermission(BAR + " " + TGT))) {
            System.err.println("Expected false, returned true");
            testFail++;
        }
        System.out.println("test 5: elements returns correct number of perms");
        int numPerms = 0;
        Enumeration<Permission> e = perms.elements();
        while (e.hasMoreElements()) {
            numPerms++;
            System.out.println(e.nextElement());
        }
        if (numPerms != 1) {
            System.err.println("Expected 1, got " + numPerms);
            testFail++;
        }
        if (testFail > 0) {
            throw new Exception(testFail + " test(s) failed");
        }
    }
}
