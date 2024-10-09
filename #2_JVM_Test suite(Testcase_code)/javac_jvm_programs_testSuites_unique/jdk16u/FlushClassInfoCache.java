

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;


public final class FlushClassInfoCache {

    public static void main(String[] args) throws Exception {
        verify(getLoader("testClass"));
        verify(getLoader("testAll"));
        Reference<ClassLoader> loader = getLoader("test");
        
        Introspector.flushCaches();
        verify(loader);
    }

    private static void verify(Reference<?> loader) throws Exception {
        int attempt = 0;
        while (loader.get() != null) {
            if (++attempt > 10) {
                throw new RuntimeException("Too many attempts: " + attempt);
            }
            
            System.gc();
            Thread.sleep(1000);
            System.out.println("Not freed :(");
        }
    }

    public static void test() {
        try {
            Introspector.getBeanInfo(FlushClassInfoCache.class);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void testClass() {
        try {
            Introspector.getBeanInfo(FlushClassInfoCache.class);
            
            Introspector.flushFromCaches(FlushClassInfoCache.class);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void testAll() {
        try {
            Introspector.getBeanInfo(FlushClassInfoCache.class);
            
            Introspector.flushCaches();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    private static Reference<ClassLoader> getLoader(String m) throws Exception {
        URL url = FlushClassInfoCache.class.getProtectionDomain()
                                     .getCodeSource().getLocation();
        URLClassLoader loader = new URLClassLoader(new URL[]{url}, null);
        Class<?> cls = Class.forName("FlushClassInfoCache", true, loader);
        cls.getDeclaredMethod(m).invoke(null);
        loader.close();
        return new WeakReference<>(loader);
    }
}
