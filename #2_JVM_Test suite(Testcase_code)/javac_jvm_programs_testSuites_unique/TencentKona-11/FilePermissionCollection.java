



import java.io.FilePermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.SecurityPermission;
import java.util.Enumeration;

public class FilePermissionCollection {

    public static void main(String[] args) throws Exception {

        int testFail = 0;

        FilePermission perm = new FilePermission("/tmp/foo", "read");
        PermissionCollection perms = perm.newPermissionCollection();

        
        System.out.println
            ("test 1: add throws IllegalArgExc for wrong perm type");
        try {
            perms.add(new SecurityPermission("createAccessControlContext"));
            System.out.println("Expected IllegalArgumentException");
            testFail++;
        } catch (IllegalArgumentException iae) {}

        
        System.out.println("test 2: implies returns false for wrong perm type");
        if (perms.implies(new SecurityPermission("getPolicy"))) {
            System.out.println("Expected false, returned true");
            testFail++;
        }

        
        System.out.println("test 3: implies returns true for match on " +
                           "name and action");
        perms.add(new FilePermission("/tmp/foo", "read"));
        if (!perms.implies(new FilePermission("/tmp/foo", "read"))) {
            System.out.println("Expected true, returned false");
            testFail++;
        }

        
        System.out.println("test 4: implies returns false for match on " +
                           "name but not action");
        if (perms.implies(new FilePermission("/tmp/foo", "write"))) {
            System.out.println("Expected false, returned true");
            testFail++;
        }

        
        System.out.println("test 5: implies returns true for match on " +
                           "name and subset of actions");
        perms.add(new FilePermission("/tmp/bar", "read, write"));
        if (!perms.implies(new FilePermission("/tmp/bar", "write"))) {
            System.out.println("Expected true, returned false");
            testFail++;
        }

        
        System.out.println("test 6: implies returns true for aggregate " +
                           "match on name and action");
        perms.add(new FilePermission("/tmp/baz", "read"));
        perms.add(new FilePermission("/tmp/baz", "write"));
        if (!perms.implies(new FilePermission("/tmp/baz", "read"))) {
            System.out.println("Expected true, returned false");
            testFail++;
        }
        if (!perms.implies(new FilePermission("/tmp/baz", "write,read"))) {
            System.out.println("Expected true, returned false");
            testFail++;
        }

        
        System.out.println("test 7: implies returns true for wildcard " +
                           "and match on action");
        perms.add(new FilePermission("/usr/tmp/*", "read"));
        if (!perms.implies(new FilePermission("/usr/tmp/foo", "read"))) {
            System.out.println("Expected true, returned false");
            testFail++;
        }

        
        System.out.println
            ("test 8: implies returns false for non-match on wildcard");
        if (perms.implies(new FilePermission("/usr/tmp/bar/foo", "read"))) {
            System.out.println("Expected false, returned true");
            testFail++;
        }

        
        System.out.println
            ("test 9: implies returns true for deep wildcard match");
        perms.add(new FilePermission("/usr/tmp/-", "read"));
        if (!perms.implies(new FilePermission("/usr/tmp/bar/foo", "read"))) {
            System.out.println("Expected true, returned false");
            testFail++;
        }

        
        
        perms.add(new FilePermission(".", "read"));
        
        
        
        
        

        
        System.out.println("test 11: implies returns true for all " +
                           "wildcard and match on action");
        perms.add(new FilePermission("<<ALL FILES>>", "read"));
        if (!perms.implies(new FilePermission("/tmp/foobar", "read"))) {
            System.out.println("Expected true, returned false");
            testFail++;
        }

        
        System.out.println("test 12: implies returns false for wildcard " +
                           "and non-match on action");
        if (perms.implies(new FilePermission("/tmp/foobar", "write"))) {
            System.out.println("Expected false, returned true");
            testFail++;
        }

        
        System.out.println("test 13: elements returns correct number of perms");
        int numPerms = 0;
        Enumeration<Permission> e = perms.elements();
        while (e.hasMoreElements()) {
            numPerms++;
            System.out.println(e.nextElement());
        }
        
        if (numPerms != 7) {
            System.out.println("Expected 7, got " + numPerms);
            testFail++;
        }

        if (testFail > 0) {
            throw new Exception(testFail + " test(s) failed");
        }
    }
}
