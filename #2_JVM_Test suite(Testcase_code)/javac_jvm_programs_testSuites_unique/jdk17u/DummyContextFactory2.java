import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

public class DummyContextFactory2 implements InitialContextFactory {

    static int counter = 0;

    public DummyContextFactory2() {
        System.out.println("New DummyContextFactory2 " + (++counter));
    }

    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        return new DummyContext(environment);
    }

    public class DummyContext extends InitialContext {

        private Hashtable<?, ?> env;

        DummyContext(Hashtable<?, ?> env) throws NamingException {
            this.env = env;
        }

        public Hashtable<?, ?> getEnvironment() {
            return env;
        }
    }
}
