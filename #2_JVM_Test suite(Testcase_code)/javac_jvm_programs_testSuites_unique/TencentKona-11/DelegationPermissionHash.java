

import javax.security.auth.kerberos.DelegationPermission;


public class DelegationPermissionHash {

    static final String princ1 = "backup/bar.example.com@EXAMPLE.COM";
    static final String princ2 = "backup/foo.example.com@EXAMPLE.COM";
    static final String ONE_SPACE = " ";
    static final String TWO_SPACES = "  ";
    static final String QUOTE = "\"";

    public static void main(String[] args) {
        DelegationPermission one = new DelegationPermission(
                QUOTE + princ1 + QUOTE + ONE_SPACE + QUOTE + princ2 + QUOTE);
        DelegationPermission two = new DelegationPermission(
                QUOTE + princ1 + QUOTE + TWO_SPACES + QUOTE + princ2 + QUOTE);

        System.out.println("one.getName() = " + one.getName());
        System.out.println("two.getName() = " + two.getName());

        if (!one.implies(two) || !two.implies(one)) {
            throw new RuntimeException("Test failed: "
                    + "one and two don't imply each other");
        }

        if (!one.equals(two)) {
            throw new RuntimeException("Test failed: one is not equal to two");
        }

        System.out.println("one.hashCode() = " + one.hashCode());
        System.out.println("two.hashCode() = " + two.hashCode());
        if (one.hashCode() != two.hashCode()) {
            throw new RuntimeException("Test failed: hash codes are not equal");
        }

        System.out.println("Test passed");
    }
}
