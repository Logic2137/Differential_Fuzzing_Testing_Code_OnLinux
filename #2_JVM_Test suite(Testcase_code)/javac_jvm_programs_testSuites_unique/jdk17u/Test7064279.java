import java.beans.Introspector;
import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;

public class Test7064279 {

    public static void main(String[] args) throws Exception {
        WeakReference ref = new WeakReference(test("test.jar", "test.Test"));
        try {
            int[] array = new int[1024];
            while (true) {
                array = new int[array.length << 1];
            }
        } catch (OutOfMemoryError error) {
            System.gc();
        }
        if (null != ref.get()) {
            throw new Error("ClassLoader is not released");
        }
    }

    private static Object test(String jarName, String className) throws Exception {
        StringBuilder sb = new StringBuilder(256);
        sb.append("file:");
        sb.append(System.getProperty("test.src", "."));
        sb.append(File.separatorChar);
        sb.append(jarName);
        ClassLoader newLoader = new URLClassLoader(new URL[] { new URL(sb.toString()) });
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(newLoader);
        test(newLoader.loadClass(className));
        Thread.currentThread().setContextClassLoader(oldLoader);
        return newLoader;
    }

    private static void test(Class type) throws Exception {
        Introspector.getBeanInfo(type);
    }
}
