



import java.net.SocketPermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.SecurityPermission;
import java.util.Enumeration;

public class SocketPermissionCollection {

    public static void main(String[] args) throws Exception {

        int testFail = 0;

        SocketPermission perm = new SocketPermission("www.example.com",
                                                     "connect");
        PermissionCollection perms = perm.newPermissionCollection();

        
        System.out.println
            ("test 1: add throws IllegalArgExc for wrong perm type");
        try {
            perms.add(new SecurityPermission("createAccessControlContext"));
            System.err.println("Expected IllegalArgumentException");
            testFail++;
        } catch (IllegalArgumentException iae) {}

        
        System.out.println("test 2: implies returns false for wrong perm type");
        if (perms.implies(new SecurityPermission("getPolicy"))) {
            System.err.println("Expected false, returned true");
            testFail++;
        }

        
        System.out.println
            ("test 3: implies returns true for match on name and action");
        perms.add(new SocketPermission("www.example.com", "connect"));
        if (!perms.implies(new SocketPermission("www.example.com", "connect"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }

        
        System.out.println
            ("test 4: implies returns false for match on name but not action");
        if (perms.implies(new SocketPermission("www.example.com", "accept"))) {
            System.err.println("Expected false, returned true");
            testFail++;
        }

        
        System.out.println("test 5: implies returns true for match on " +
                           "name and subset of actions");
        perms.add(new SocketPermission("www.example.org", "accept, connect"));
        if (!perms.implies(new SocketPermission("www.example.org", "connect"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }

        
        System.out.println("test 6: implies returns true for aggregate " +
                           "match on name and action");
        perms.add(new SocketPermission("www.example.us", "accept"));
        perms.add(new SocketPermission("www.example.us", "connect"));
        if (!perms.implies(new SocketPermission("www.example.us", "accept"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }
        if (!perms.implies(new SocketPermission("www.example.us",
                                                "connect,accept"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }

        
        System.out.println("test 7: implies returns true for wildcard " +
                           "and match on action");
        perms.add(new SocketPermission("*.example.edu", "resolve"));
        if (!perms.implies(new SocketPermission("foo.example.edu", "resolve"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }

        
        System.out.println("test 8: implies returns false for non-match " +
                           "on wildcard");
        if (perms.implies(new SocketPermission("foo.example.edu", "connect"))) {
            System.err.println("Expected false, returned true");
            testFail++;
        }

        
        System.out.println("test 9: implies returns true for matching " +
                           "port range and action");
        perms.add(new SocketPermission("204.160.241.0:1024-65535", "connect"));
        if (!perms.implies(new SocketPermission("204.160.241.0:1025", "connect"))) {
            System.err.println("Expected true, returned false");
            testFail++;
        }


        
        System.out.println("test 10: elements returns correct number of perms");
        perms.add(new SocketPermission("www.example.us", "resolve"));
        int numPerms = 0;
        Enumeration<Permission> e = perms.elements();
        while (e.hasMoreElements()) {
            numPerms++;
            System.out.println(e.nextElement());
        }
        
        if (numPerms != 5) {
            System.err.println("Expected 5, got " + numPerms);
            testFail++;
        }

        if (testFail > 0) {
            throw new Exception(testFail + " test(s) failed");
        }
    }
}
