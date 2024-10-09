
package client;

import java.io.IOException;
import java.security.Principal;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.LoginContext;
import com.sun.security.auth.UserPrincipal;

public class JaasClient {

    private static final String USER_NAME = "testUser";

    private static final String PASSWORD = "testPassword";

    private static final String LOGIN_CONTEXT = "ModularLoginConf";

    public static void main(String[] args) {
        try {
            LoginContext lc = new LoginContext(LOGIN_CONTEXT, new MyCallbackHandler());
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

    private static class MyCallbackHandler implements CallbackHandler {

        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    ((NameCallback) callback).setName(USER_NAME);
                } else if (callback instanceof PasswordCallback) {
                    ((PasswordCallback) callback).setPassword(PASSWORD.toCharArray());
                } else {
                    throw new UnsupportedCallbackException(callback);
                }
            }
        }
    }
}
