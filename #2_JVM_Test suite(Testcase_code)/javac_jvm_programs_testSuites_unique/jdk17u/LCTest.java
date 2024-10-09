import com.sun.security.auth.UnixPrincipal;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LCTest {

    private static final String USER_NAME = "testUser";

    private static final String PASSWORD = "testPassword";

    private static final List<String> loggedActions = new ArrayList<>();

    static {
        System.setProperty("java.security.auth.login.config", System.getProperty("test.src") + System.getProperty("file.separator") + "LCTest.jaas.config");
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Incorrect test params");
        }
        String nameOfContext = args[0];
        boolean isPositive = Boolean.parseBoolean(args[1]);
        String actionName = null;
        if (args.length == 3) {
            actionName = args[2];
        }
        try {
            LoginContext lc = new LoginContext(nameOfContext, new MyCallbackHandler());
            lc.login();
            checkPrincipal(lc, true);
            lc.logout();
            checkPrincipal(lc, false);
            if (!isPositive) {
                throw new RuntimeException("Test failed. Exception expected.");
            }
        } catch (LoginException le) {
            if (isPositive) {
                throw new RuntimeException("Test failed. Unexpected " + "exception", le);
            }
            System.out.println("Expected exception: " + le.getMessage());
        }
        checkActions(actionName);
        System.out.println("Test passed.");
    }

    private static void logAction(String actionName) {
        loggedActions.add(actionName);
    }

    private static void checkActions(String actionName) {
        if (actionName == null) {
            if (loggedActions.size() != 0) {
                throw new RuntimeException("No logged actions expected");
            }
        } else {
            int loggedActionsFound = 0;
            System.out.println("Logged actions : " + loggedActions);
            for (String s : loggedActions) {
                if (s.equals(actionName)) {
                    loggedActionsFound++;
                }
            }
            if (loggedActionsFound != 3) {
                throw new RuntimeException("Incorrect number of actions " + actionName + " : " + loggedActionsFound);
            }
        }
    }

    private static void checkPrincipal(LoginContext loginContext, boolean principalShouldExist) {
        if (!principalShouldExist) {
            if (loginContext.getSubject().getPrincipals().size() != 0) {
                throw new RuntimeException("Test failed. Principal was not " + "cleared.");
            }
        } else {
            for (Principal p : loginContext.getSubject().getPrincipals()) {
                if (p instanceof UnixPrincipal && USER_NAME.equals(p.getName())) {
                    return;
                }
            }
            throw new RuntimeException("Test failed. UnixPrincipal " + USER_NAME + " expected.");
        }
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

    public static class LoginModuleAllPass extends LoginModuleBase {
    }

    public static class LoginModuleWithAbortException extends LoginModuleBase {

        @Override
        public boolean abort() throws LoginException {
            super.abort();
            throw new LoginException("Abort failed!");
        }
    }

    public static class LoginModuleWithLoginException extends LoginModuleBase {

        @Override
        public boolean login() throws LoginException {
            super.login();
            throw new FailedLoginException("Login failed!");
        }
    }

    public static class LoginModuleWithLogoutException extends LoginModuleBase {

        @Override
        public boolean logout() throws LoginException {
            super.logout();
            throw new FailedLoginException("Logout failed!");
        }
    }

    public static abstract class LoginModuleBase implements LoginModule {

        private Subject subject;

        private CallbackHandler callbackHandler;

        private Map sharedState;

        private Map options;

        private UnixPrincipal userPrincipal;

        private String username;

        private String password;

        private boolean succeeded = false;

        private boolean commitSucceeded = false;

        @Override
        public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
            this.subject = subject;
            this.callbackHandler = callbackHandler;
            this.sharedState = sharedState;
            this.options = options;
            System.out.println("Login module initialized.");
        }

        @Override
        public boolean login() throws LoginException {
            LCTest.logAction("login");
            if (callbackHandler == null) {
                throw new LoginException("No CallbackHandler available");
            }
            Callback[] callbacks = new Callback[2];
            callbacks[0] = new NameCallback("Username: ");
            callbacks[1] = new PasswordCallback("Password: ", false);
            try {
                callbackHandler.handle(callbacks);
                username = ((NameCallback) callbacks[0]).getName();
                password = new String(((PasswordCallback) callbacks[1]).getPassword());
                if (username.equals(LCTest.USER_NAME) && password.equals(LCTest.PASSWORD)) {
                    succeeded = true;
                    return true;
                }
                throw new FailedLoginException("Incorrect username/password!");
            } catch (IOException | UnsupportedCallbackException e) {
                throw new LoginException("Login failed: " + e.getMessage());
            }
        }

        @Override
        public boolean commit() throws LoginException {
            LCTest.logAction("commit");
            if (succeeded == false) {
                return false;
            }
            userPrincipal = new UnixPrincipal(username);
            final Subject s = subject;
            final UnixPrincipal up = userPrincipal;
            java.security.AccessController.doPrivileged((java.security.PrivilegedAction) () -> {
                if (!s.getPrincipals().contains(up)) {
                    s.getPrincipals().add(up);
                }
                return null;
            });
            password = null;
            commitSucceeded = true;
            return true;
        }

        @Override
        public boolean abort() throws LoginException {
            LCTest.logAction("abort");
            if (succeeded == false) {
                return false;
            }
            clearState();
            return true;
        }

        @Override
        public boolean logout() throws LoginException {
            LCTest.logAction("logout");
            clearState();
            return true;
        }

        private void clearState() {
            if (commitSucceeded) {
                final Subject s = subject;
                final UnixPrincipal up = userPrincipal;
                java.security.AccessController.doPrivileged((java.security.PrivilegedAction) () -> {
                    s.getPrincipals().remove(up);
                    return null;
                });
            }
            username = null;
            password = null;
            userPrincipal = null;
        }
    }
}
