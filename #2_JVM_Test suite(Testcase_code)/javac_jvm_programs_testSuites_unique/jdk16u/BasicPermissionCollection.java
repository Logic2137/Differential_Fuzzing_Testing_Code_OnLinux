



import java.security.BasicPermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.SecurityPermission;
import java.util.Enumeration;

public class BasicPermissionCollection {

    public static void main(String[] args) throws Exception {

        int testFail = 0;

        TestPermission perm = new TestPermission("foo");
        PermissionCollection perms = perm.newPermissionCollection();

        
        System.out.println("test 1: add throws IllegalArgumentExc");
        try {
            perms.add(new SecurityPermission("createAccessControlContext"));
            System.err.println("Expected IllegalArgumentException");
            testFail++;
        } catch (IllegalArgumentException iae) {}

        
        System.out.println("test 2: implies returns false for wrong class");
        if (perms.implies(new SecurityPermission("getPolicy"))) {
            System.err.println("Expected false, returned true");
            testFail++;
        }

        
        System.out.println("test 3: implies returns true for match on name");
        perms.add(new TestPermission("foo"));
        if (!perms.implies(new TestPermission("foo"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }

        
        System.out.println("test 4: implies returns true for wildcard match");
        perms.add(new TestPermission("bar.*"));
        if (!perms.implies(new TestPermission("bar.foo"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }

        
        System.out.println
            ("test 5: implies returns false for invalid wildcard");
        perms.add(new TestPermission("baz*"));
        if (perms.implies(new TestPermission("baz.foo"))) {
            System.err.println("Expected false, returned true");
            testFail++;
        }

        
        System.out.println
            ("test 6: implies returns true for deep wildcard match");
        if (!perms.implies(new TestPermission("bar.foo.baz"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }

        
        System.out.println
            ("test 7: implies returns true for all wildcard match");
        perms.add(new TestPermission("*"));
        if (!perms.implies(new TestPermission("yes"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }

        
        System.out.println("test 8: elements returns correct number of perms");
        int numPerms = 0;
        Enumeration<Permission> e = perms.elements();
        while (e.hasMoreElements()) {
            numPerms++;
            System.out.println(e.nextElement());
        }
        if (numPerms != 4) {
            System.err.println("Expected 4, got " + numPerms);
            testFail++;
        }

        if (testFail > 0) {
            throw new Exception(testFail + " test(s) failed");
        }
    }

    private static class TestPermission extends BasicPermission {
        TestPermission(String name) {
            super(name);
        }
    }
}
