

import java.security.Principal;

import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXAuthenticator;
import javax.management.remote.JMXPrincipal;
import javax.security.auth.Subject;

public final class TestJMXAuthenticator implements JMXAuthenticator {

    private String protocol = "";
    private MBeanServer mbs = null;

    public TestJMXAuthenticator() {
    }

    public TestJMXAuthenticator(String protocol) {
        this.protocol = protocol;
    }

    public TestJMXAuthenticator(String protocol, MBeanServer mbs) {
        this.protocol = protocol;
        this.mbs = mbs;
    }

    public Subject authenticate(Object credentials) {

        String credentials_username = "";
        String credentials_password = "";
        Principal aPrincipal = null;

        credentials_username = ((String[]) credentials)[0];
        credentials_password = ((String[]) credentials)[1];

        String authenticated_username = System.getProperty("susername");
        String authenticated_password = System.getProperty("spassword");
        String principal = System.getProperty("principal");

        System.out.println("TestJMXAuthenticator::authenticate: Start");
        System.out.println("TestJMXAuthenticator::authenticate: credentials username = " +
                credentials_username);
        System.out.println("TestJMXAuthenticator::authenticate: credentials password = " +
                credentials_password);
        System.out.println("TestJMXAuthenticator::authenticate: authenticated username = " +
                authenticated_username);
        System.out.println("TestJMXAuthenticator::authenticate: authenticated password = " +
                authenticated_password);
        System.out.println("TestJMXAuthenticator::authenticate: principal used for " +
                "authorization = " + principal);

        if (credentials_username.equals(authenticated_username) &&
                credentials_password.equals(authenticated_password)) {
            System.out.println("TestJMXAuthenticator::authenticate: " +
                    "Authenticator should succeed");
        } else {
            System.out.println("TestJMXAuthenticator::authenticate: " +
                    "Authenticator should reject");
            throw new SecurityException("TestJMXAuthenticator throws EXCEPTION");
        }

        
        
        
        
        
        
        
        
        
        
        
        
        Subject subject = new Subject();

        if (principal != null) {
            System.out.println("TestJMXAuthenticator::authenticate: " +
                    "Add " + principal + " principal to the returned subject");
            subject.getPrincipals().add(new JMXPrincipal(principal));
        }

        return subject;
    }
}
