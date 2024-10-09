
package login;

import java.security.Principal;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import com.sun.security.auth.UserPrincipal;

public class JaasClientWithDefaultHandler {

    private static final String USER_NAME = "testUser";

    private static final String LOGIN_CONTEXT = "ModularLoginConf";

    private static final String CBH_PROP = "auth.login.defaultCallbackHandler";

    public static void main(String[] args) {
        try {
            java.security.Security.setProperty(CBH_PROP, args[0]);
            LoginContext lc = new LoginContext(LOGIN_CONTEXT);
            lc.login();
            checkPrincipal(lc, true);
            lc.logout();
            checkPrincipal(lc, false);
        } catch (LoginException le) {
            throw new RuntimeException(le);
        }
        System.out.println("Test passed.");
    }

    private static void checkPrincipal(LoginContext loginContext, boolean principalShouldExist) {
        if (!principalShouldExist) {
            if (loginContext.getSubject().getPrincipals().size() != 0) {
                throw new RuntimeException("Test failed. Principal was not " + "cleared.");
            }
            return;
        }
        for (Principal p : loginContext.getSubject().getPrincipals()) {
            if (p instanceof UserPrincipal && USER_NAME.equals(p.getName())) {
                return;
            }
        }
        throw new RuntimeException("Test failed. UserPrincipal " + USER_NAME + " expected.");
    }
}
