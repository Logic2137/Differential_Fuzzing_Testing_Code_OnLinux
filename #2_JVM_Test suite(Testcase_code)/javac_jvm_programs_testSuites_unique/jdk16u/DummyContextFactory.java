

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.NamingManager;
import java.util.Hashtable;

public class DummyContextFactory implements InitialContextFactory {
    static final String DUMMY_FACTORY = "DummyContextFactory";
    static final String DUMMY_FACTORY2 = "DummyContextFactory2";
    static final String MISSING_FACTORY = "NonExistant";
    static int counter = 0;
    ClassLoader origContextLoader = Thread.currentThread().getContextClassLoader();

    public static void main(String[] s) throws Exception {
        DummyContextFactory dcf = new DummyContextFactory();
        dcf.runTest();
    }

    private void runTest() throws Exception {
        final String classes = System.getProperty("url.dir", ".");
        final URL curl = new File(classes).toURI().toURL();
        URLClassLoader testLoader = new URLClassLoader(new URL[] {curl}, null);
        WeakReference<URLClassLoader> weakRef = new WeakReference<>(testLoader);
        Thread.currentThread().setContextClassLoader(testLoader);
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, DUMMY_FACTORY);
        testContextCalls(env);

        
        Thread.currentThread().setContextClassLoader(testLoader);
        env.put(Context.INITIAL_CONTEXT_FACTORY, DUMMY_FACTORY2);
        testContextCalls(env);

        
        
        if (counter != 2) {
            throw new RuntimeException("wrong count: " + counter);
        }

        
        env.put(Context.INITIAL_CONTEXT_FACTORY, MISSING_FACTORY);
        testBadContextCall(env);

        
        testLoader = null;
        System.gc();
        while (weakRef.get() != null) {
            Thread.sleep(100);
            System.gc();
        }
    }

    private void testContextCalls(Hashtable<String, String> env) throws Exception {
        
        

        
        
        Context cxt = NamingManager.getInitialContext(env);

        
        cxt = NamingManager.getInitialContext(env);

        Thread.currentThread().setContextClassLoader(origContextLoader);

        
        
        cxt = NamingManager.getInitialContext(env);

        
        
        
        Thread.currentThread().setContextClassLoader(null);
        cxt = NamingManager.getInitialContext(env);
    }

    private void testBadContextCall(Hashtable<String, String> env) throws Exception {
        try {
            Context cxt = NamingManager.getInitialContext(env);
            throw new RuntimeException("Expected NoInitialContextException");
        } catch (NoInitialContextException e) {
            if (!(e.getCause() instanceof ClassNotFoundException)) {
                throw new RuntimeException("unexpected cause", e.getCause());
            }
        }
    }

    public DummyContextFactory() {
        System.out.println("New DummyContextFactory " + (++counter));
        
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
