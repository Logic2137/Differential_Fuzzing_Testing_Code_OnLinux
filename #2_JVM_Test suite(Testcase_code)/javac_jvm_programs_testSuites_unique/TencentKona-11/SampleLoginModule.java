

import static java.lang.System.out;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;



public class SampleLoginModule implements LoginModule {

    private final String name;

    public SampleLoginModule() {
        name = this.getClass().getName();
    }

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
            Map<String, ?> sharedState, Map<String, ?> options) {
    }

    @Override
    public boolean login() throws LoginException {
        out.println(name + " Login method of AbstractLoginModule is called ");
        out.println(name + ":login:PASS");
        return true;
    }

    @Override
    public boolean commit() throws LoginException {
        out.println("Commit of AbstractLoginModule is called");
        out.println(name + ":commit:PASS");
        return true;

    }

    @Override
    public boolean abort() throws LoginException {
        out.println("Abourt is called in AbstractLoginModule");
        out.println(name + ":abort:PASS");
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        out.println("logout is called in AbstractLoginModule");
        out.println(name + ":logout:PASS");
        return true;
    }
}
