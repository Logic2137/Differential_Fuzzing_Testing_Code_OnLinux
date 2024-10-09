



import java.security.NoSuchAlgorithmException;
import java.security.cert.CertStore;
import java.security.cert.LDAPCertStoreParameters;


public class NoLDAP {
    public static void main(String[] args) throws Exception {
        try {
            Class.forName("javax.naming.ldap.LdapName");
            System.out.println("LDAP is present, test skipped");
            return;
        } catch (ClassNotFoundException ignore) { }

        try {
            CertStore.getInstance("LDAP", new LDAPCertStoreParameters());
            throw new RuntimeException("NoSuchAlgorithmException expected");
        } catch (NoSuchAlgorithmException x) {
            System.out.println("NoSuchAlgorithmException thrown as expected");
        }
    }
}
