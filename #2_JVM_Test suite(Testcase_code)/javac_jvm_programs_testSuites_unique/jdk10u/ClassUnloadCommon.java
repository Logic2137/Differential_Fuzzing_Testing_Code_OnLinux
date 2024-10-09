




import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ClassUnloadCommon {
    public static class TestFailure extends RuntimeException {
        TestFailure(String msg) {
            super(msg);
        }
    }

    public static void failIf(boolean value, String msg) {
        if (value) throw new TestFailure("Test failed: " + msg);
    }

    private static volatile Object dummy = null;
    private static void allocateMemory(int kilobytes) {
        ArrayList<byte[]> l = new ArrayList<>();
        dummy = l;
        for (int i = kilobytes; i > 0; i -= 1) {
            l.add(new byte[1024]);
        }
        l = null;
        dummy = null;
    }

    public static void triggerUnloading() {
        allocateMemory(16 * 1024); 
        System.gc();
    }

    
    public static ClassLoader newClassLoader() {
        String cp = System.getProperty("test.class.path", ".");
        URL[] urls = Stream.of(cp.split(File.pathSeparator))
                .map(Paths::get)
                .map(ClassUnloadCommon::toURL)
                .toArray(URL[]::new);
        return new URLClassLoader(urls) {
            @Override
            public Class<?> loadClass(String cn, boolean resolve)
                throws ClassNotFoundException
            {
                synchronized (getClassLoadingLock(cn)) {
                    Class<?> c = findLoadedClass(cn);
                    if (c == null) {
                        try {
                            c = findClass(cn);
                        } catch (ClassNotFoundException e) {
                            c = getParent().loadClass(cn);
                        }

                    }
                    if (resolve) {
                        resolveClass(c);
                    }
                    return c;
                }
            }
        };
    }

    static URL toURL(Path path) {
        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
