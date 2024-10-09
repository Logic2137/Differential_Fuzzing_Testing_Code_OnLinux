import java.security.*;

public class Root {

    public static void main(String[] args) {
        Policy p = Policy.getPolicy();
        if (p.implies(Root.class.getProtectionDomain(), new AllPermission())) {
            System.out.println("Test succeeded");
        } else {
            throw new SecurityException("Test failed");
        }
    }
}
