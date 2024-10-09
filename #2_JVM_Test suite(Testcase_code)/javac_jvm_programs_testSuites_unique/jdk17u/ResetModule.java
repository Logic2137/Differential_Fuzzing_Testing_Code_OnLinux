import javax.security.auth.*;
import javax.security.auth.login.*;
import javax.security.auth.spi.*;
import javax.security.auth.callback.*;
import java.util.*;

public class ResetModule implements LoginModule {

    public ResetModule() {
    }

    public void initialize(Subject s, CallbackHandler h, Map<String, ?> ss, Map<String, ?> options) {
        throw new SecurityException("INITIALIZE");
    }

    public boolean login() throws LoginException {
        return true;
    }

    public boolean commit() throws LoginException {
        return true;
    }

    public boolean abort() throws LoginException {
        return true;
    }

    public boolean logout() throws LoginException {
        return true;
    }
}
