




import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;

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
        try {
            return new URLClassLoader(new URL[] {
                Paths.get(System.getProperty("test.classes",".") + File.separatorChar + "classes").toUri().toURL(),
            }, null);
        } catch (MalformedURLException e){
            throw new RuntimeException("Unexpected URL conversion failure", e);
        }
    }

}
