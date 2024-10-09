



import javax.naming.*;
import java.util.Hashtable;

public class DummyContext extends InitialContext {

    private Hashtable env;

    DummyContext(Hashtable env) throws NamingException {
        this.env = env;
    }

    public Hashtable getEnvironment() throws NamingException {
        return env;
    }
}
