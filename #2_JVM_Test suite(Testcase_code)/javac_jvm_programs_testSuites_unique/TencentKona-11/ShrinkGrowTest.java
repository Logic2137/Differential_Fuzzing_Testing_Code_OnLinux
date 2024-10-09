




package metaspace.shrink_grow.ShrinkGrowTest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;


public class ShrinkGrowTest {

    
    private final Map<String, ShrinkGrowTest.Foo> loadedClasses = new HashMap<>();

    private static int counter = 0;

    private String errorMessage = "not completed";

     
    private final String whoAmI;

    
    private final int maxClassesToLoad;

    public static void main(String[] args) {
        String name = args.length > 0 ? args[0] : "singleTest" ;
        new ShrinkGrowTest(name, 20000).run();
    }

    
    public ShrinkGrowTest(String name, int classesToLoad) {
        whoAmI = name;
        maxClassesToLoad = classesToLoad;

    }

    
    void log(String message) {
        System.out.println("%" + whoAmI + "% " + message);
    }

    void throwFault(String message) {
        throw new TestFault("%" + whoAmI + "% " + message);
    }

    void throwFault(String message, Throwable t) {
        throw new TestFault("%" + whoAmI + "% " + message, t);
    }

    
    public void run() {
        if (System.getProperty("requiresCompressedClassSpace") != null &&
                   !isCompressedClassSpaceAvailable()) {
                System.out.println("Not applicalbe, Compressed Class Space is required");
            return;
        }

        try {
            log("Bootstrapping string concatenation for " + whoAmI );
            go();
            
            setErrorMessage(null);
            log("passed");
        } catch (TestFault failure) {
            failure.printStackTrace(System.err);
            setErrorMessage(failure.getMessage());
            log("failed :" + errorMessage);
            throw failure;
        } catch (Throwable badThing) {
            setErrorMessage(badThing.toString());
            throw new TestFault(badThing);
        }
    }

    private void go() {
        
        log("eating metaspace");
        runOutOfMetaspace(maxClassesToLoad);

        
        
        try {
            eatALittleMemory();
            throwFault("We haven't cleaned metaspace yet!");
        } catch (OutOfMemoryError error) {
            if (!isMetaspaceError(error)) {
                throwFault("Hmm, we ran out metaspace. Metaspace error is still excpected here " + error, error);
            }
        }

        
        log("washing hands before meal");
        loadedClasses.clear();
        System.gc();
        try {
            log("one more try to eat");
            eatALittleMemory();
        } catch (OutOfMemoryError error) {
            throwFault("we already should be able to consume metaspace " + error, error);
        }
    }

    
    public boolean isPassed() {
        return errorMessage == null;
    }

    
    public String getErrorMessage() {
        return errorMessage;
    }

    
    void setErrorMessage(String msg) {
        errorMessage = msg;
    }

    
    private void runOutOfMetaspace(int times) {
        try {
            for (int i = 0; i < times; i++) {
                eatALittleMemory();
            }
        } catch (OutOfMemoryError error) {
            if (isMetaspaceError(error)) {
                return;
            }
            throwFault("We ran out of another space, not metaspace: " + error, error);
        }
        throwFault("OOM hasn't happened after " + times + " iterations. Might be too much space?..");
    }

    
    private void eatALittleMemory() {
        try {
            String jarUrl = "file:" + counter + ".jar";
            counter++;
            URL[] urls = new URL[]{new URL(jarUrl)};
            URLClassLoader cl = new URLClassLoader(urls);
            ShrinkGrowTest.Foo foo = (ShrinkGrowTest.Foo) Proxy.newProxyInstance(cl,
                    new Class[]{ShrinkGrowTest.Foo.class},
                    new ShrinkGrowTest.FooInvocationHandler(new ShrinkGrowTest.FooBar()));
            loadedClasses.put(jarUrl, foo);
        } catch (java.net.MalformedURLException badThing) {
            
            throwFault("Unexpeted error: " + badThing, badThing);
        }

    }

    
    boolean isMetaspaceError(OutOfMemoryError error) {
            String message = error.getMessage();
        return message != null && (message.contains("Metaspace") ||
                        message.contains("Compressed class space"));
    }

    boolean isCompressedClassSpaceAvailable() {
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (pool.getName().equalsIgnoreCase("Compressed class space")) {
                return true;
            }
        }
        return false;
    }

    
    public static class TestFault extends RuntimeException {
        public TestFault(String message) {
            super(message);
        }
        public TestFault(Throwable t) {
            super(t);
        }
        public TestFault(String message, Throwable t) {
            super(message, t);
        }
    }

    public static interface Foo {
    }

    public static class FooBar implements ShrinkGrowTest.Foo {
    }

    class FooInvocationHandler implements InvocationHandler {
        private final ShrinkGrowTest.Foo foo;

        FooInvocationHandler(ShrinkGrowTest.Foo foo) {
            this.foo = foo;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(foo, args);
        }
    }
}
