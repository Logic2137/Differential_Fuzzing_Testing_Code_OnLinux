import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class SharedState {

    static final String NAME = "name";

    static final String VALUE = "shared";

    public static void main(String[] args) throws LoginException {
        System.setProperty("java.security.auth.login.config", System.getProperty("test.src") + System.getProperty("file.separator") + "shared.config");
        new LoginContext("SharedState").login();
    }

    public static abstract class Module implements LoginModule {

        @Override
        public boolean login() throws LoginException {
            return true;
        }

        @Override
        public boolean commit() throws LoginException {
            return true;
        }

        @Override
        public boolean abort() throws LoginException {
            return true;
        }

        @Override
        public boolean logout() throws LoginException {
            return true;
        }
    }

    public static class FirstModule extends Module {

        @Override
        public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
            ((Map) sharedState).put(NAME, VALUE);
        }
    }

    public static class SecondModule extends Module {

        @Override
        public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
            Object shared = sharedState.get(NAME);
            if (!VALUE.equals(shared)) {
                throw new RuntimeException("Unexpected shared object: " + shared);
            }
        }
    }
}
