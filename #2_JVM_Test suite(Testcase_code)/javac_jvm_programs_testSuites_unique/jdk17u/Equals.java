import javax.security.auth.*;

public class Equals {

    public static void main(String[] args) {
        PrivateCredentialPermission pcp1 = new PrivateCredentialPermission("a b \"pcp1\" c \"pcp2\"", "read");
        PrivateCredentialPermission pcp2 = new PrivateCredentialPermission("a b \"pcp1\" c \"pcp2\"", "read");
        if (!pcp1.equals(pcp2) || !pcp2.equals(pcp1))
            throw new SecurityException("Equals test failed: #1");
        if (!pcp1.implies(pcp2) || !pcp2.implies(pcp1))
            throw new SecurityException("Equals test failed: #2");
        pcp1 = new PrivateCredentialPermission("a c \"pcp2\" b \"pcp1\"", "read");
        pcp2 = new PrivateCredentialPermission("a b \"pcp1\" c \"pcp2\"", "read");
        if (!pcp1.equals(pcp2) || !pcp2.equals(pcp1))
            throw new SecurityException("Equals test failed: #3");
        if (!pcp1.implies(pcp2) || !pcp2.implies(pcp1))
            throw new SecurityException("Equals test failed: #4");
        pcp1 = new PrivateCredentialPermission("a b \"pcp1\"", "read");
        if (pcp1.equals(pcp2) || pcp2.equals(pcp1))
            throw new SecurityException("Equals test failed: #5");
        if (!pcp1.implies(pcp2) || pcp2.implies(pcp1))
            throw new SecurityException("Equals test failed: #6");
        pcp1 = new PrivateCredentialPermission("* b \"pcp1\" c \"pcp2\"", "read");
        if (pcp1.equals(pcp2) || pcp2.equals(pcp1))
            throw new SecurityException("Equals test failed: #7");
        if (!pcp1.implies(pcp2) || pcp2.implies(pcp1))
            throw new SecurityException("Equals test failed: #8");
        pcp1 = new PrivateCredentialPermission("a c \"pcp2\" b \"*\"", "read");
        if (pcp1.equals(pcp2) || pcp2.equals(pcp1))
            throw new SecurityException("Equals test failed: #9");
        if (!pcp1.implies(pcp2) || pcp2.implies(pcp1))
            throw new SecurityException("Equals test failed: #10");
        pcp2 = new PrivateCredentialPermission("a b \"*\" c \"pcp2\"", "read");
        if (!pcp1.equals(pcp2) || !pcp2.equals(pcp1))
            throw new SecurityException("Equals test failed: #11");
        if (!pcp1.implies(pcp2) || !pcp2.implies(pcp1))
            throw new SecurityException("Equals test failed: #12");
        System.out.println("Equals test passed");
    }
}
