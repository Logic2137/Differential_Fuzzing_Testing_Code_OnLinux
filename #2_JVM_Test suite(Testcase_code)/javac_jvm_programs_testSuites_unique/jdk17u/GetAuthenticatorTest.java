import java.lang.ref.Reference;
import java.net.Authenticator;
import java.net.NetPermission;
import java.net.PasswordAuthentication;
import java.security.AccessControlException;

public class GetAuthenticatorTest {

    static final class MyAuthenticator extends Authenticator {

        MyAuthenticator() {
            super();
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            System.out.println("Auth called");
            return (new PasswordAuthentication("user", "passwordNotCheckedAnyway".toCharArray()));
        }
    }

    public static void main(String[] args) throws Exception {
        Authenticator defaultAuth = Authenticator.getDefault();
        if (defaultAuth != null) {
            throw new RuntimeException("Unexpected authenticator: null expected");
        }
        MyAuthenticator auth = new MyAuthenticator();
        Authenticator.setDefault(auth);
        defaultAuth = Authenticator.getDefault();
        if (defaultAuth != auth) {
            throw new RuntimeException("Unexpected authenticator: auth expected");
        }
        System.setSecurityManager(new SecurityManager());
        try {
            defaultAuth = Authenticator.getDefault();
            throw new RuntimeException("Expected security exception not raised");
        } catch (AccessControlException s) {
            System.out.println("Got expected exception: " + s);
            if (!s.getPermission().equals(new NetPermission("requestPasswordAuthentication"))) {
                throw new RuntimeException("Unexpected permission check: " + s.getPermission());
            }
        }
        System.out.println("Test passed with default authenticator " + defaultAuth);
    }
}
