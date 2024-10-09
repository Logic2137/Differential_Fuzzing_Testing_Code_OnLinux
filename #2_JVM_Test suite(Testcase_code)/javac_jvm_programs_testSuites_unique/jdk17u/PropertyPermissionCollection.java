import java.security.Permission;
import java.security.PermissionCollection;
import java.security.SecurityPermission;
import java.util.Enumeration;
import java.util.PropertyPermission;

public class PropertyPermissionCollection {

    public static void main(String[] args) throws Exception {
        int testFail = 0;
        PropertyPermission perm = new PropertyPermission("user.home", "read");
        PermissionCollection perms = perm.newPermissionCollection();
        System.out.println("test 1: add throws IllegalArgExc for wrong perm type");
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
        System.out.println("test 3: implies returns true for match on name and action");
        perms.add(new PropertyPermission("user.home", "read"));
        if (!perms.implies(new PropertyPermission("user.home", "read"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }
        System.out.println("test 4: implies returns false for match on name but not action");
        if (perms.implies(new PropertyPermission("user.home", "write"))) {
            System.err.println("Expected false, returned true");
            testFail++;
        }
        System.out.println("test 5: implies returns true for match " + "on name and subset of actions");
        perms.add(new PropertyPermission("java.home", "read, write"));
        if (!perms.implies(new PropertyPermission("java.home", "write"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }
        System.out.println("test 6: implies returns true for aggregate " + "match on name and action");
        perms.add(new PropertyPermission("user.name", "read"));
        perms.add(new PropertyPermission("user.name", "write"));
        if (!perms.implies(new PropertyPermission("user.name", "read"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }
        if (!perms.implies(new PropertyPermission("user.name", "write,read"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }
        System.out.println("test 7: implies returns true for wildcard " + "and match on action");
        perms.add(new PropertyPermission("foo.*", "read"));
        if (!perms.implies(new PropertyPermission("foo.bar", "read"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }
        System.out.println("test 8: implies returns true for deep " + "wildcard and match on action");
        if (!perms.implies(new PropertyPermission("foo.bar.baz", "read"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }
        System.out.println("test 8: implies returns false for invalid wildcard");
        perms.add(new PropertyPermission("baz*", "read"));
        if (perms.implies(new PropertyPermission("baz.foo", "read"))) {
            System.err.println("Expected false, returned true");
            testFail++;
        }
        System.out.println("test 9: implies returns true for all " + "wildcard and match on action");
        perms.add(new PropertyPermission("*", "read"));
        if (!perms.implies(new PropertyPermission("java.version", "read"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }
        System.out.println("test 10: implies returns false for wildcard " + "and non-match on action");
        if (perms.implies(new PropertyPermission("java.version", "write"))) {
            System.err.println("Expected false, returned true");
            testFail++;
        }
        System.out.println("test 11: elements returns correct number of perms");
        int numPerms = 0;
        Enumeration<Permission> e = perms.elements();
        while (e.hasMoreElements()) {
            numPerms++;
            System.out.println(e.nextElement());
        }
        if (numPerms != 6) {
            System.err.println("Expected 6, got " + numPerms);
            testFail++;
        }
        if (testFail > 0) {
            throw new Exception(testFail + " test(s) failed");
        }
    }
}
