



import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NativeLibraryTest {
    static final Path CLASSES = Paths.get("classes");
    static int unloadedCount = 0;

    
    static void nativeLibraryUnloaded() {
        unloadedCount++;
    }

    public static void main(String... args) throws Exception {
        setup();

        for (int count=1; count <= 5; count++) {
            
            runTest();
            
            System.gc();
            
            Thread.sleep(100);

            
            if (count != unloadedCount) {
                throw new RuntimeException("Expected unloaded=" + count +
                    " but got=" + unloadedCount);
            }
        }
    }

    
    static void runTest() throws Exception {
        
        Runnable r = newTestRunnable();
        r.run();

        
        r.run();

        
        Runnable r1 = newTestRunnable();
        try {
            r1.run();
            throw new RuntimeException("should fail to load the native library" +
                    " by another class loader");
        } catch (UnsatisfiedLinkError e) {}
    }

    
    static Runnable newTestRunnable() throws Exception {
        TestLoader loader = new TestLoader();
        Class<?> c = Class.forName("p.Test", true, loader);
        return (Runnable) c.newInstance();
    }

    static class TestLoader extends URLClassLoader {
        static URL[] toURLs() {
            try {
                return new URL[] { CLASSES.toUri().toURL() };
            } catch (MalformedURLException e) {
                throw new Error(e);
            }
        }

        TestLoader() {
            super("testloader", toURLs(), ClassLoader.getSystemClassLoader());
        }
    }

    
    static void setup() throws IOException {
        String dir = System.getProperty("test.classes", ".");
        Path file = Paths.get("p", "Test.class");
        Files.createDirectories(CLASSES.resolve("p"));
        Files.move(Paths.get(dir).resolve(file),
                   CLASSES.resolve("p").resolve("Test.class"));
    }
}
