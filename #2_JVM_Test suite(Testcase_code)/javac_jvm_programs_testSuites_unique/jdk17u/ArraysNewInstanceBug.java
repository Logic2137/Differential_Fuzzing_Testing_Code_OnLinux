import java.lang.reflect.Array;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class ArraysNewInstanceBug implements Runnable {

    static Class<?>[] classes;

    int start;

    ArraysNewInstanceBug(int start) {
        this.start = start;
    }

    String[] result;

    public void run() {
        result = new String[classes.length];
        System.err.print('.');
        for (int i = start; i < classes.length; i++) {
            result[i] = Array.newInstance(classes[i], 0).getClass().getName();
        }
    }

    public static void main(String[] args) throws Throwable {
        Class<?> c = ArraysNewInstanceBug.class;
        ClassLoader apploader = c.getClassLoader();
        File testClasses = new File(System.getProperty("test.classes"));
        for (int iter = 0; iter < 10; iter++) {
            System.err.print('[');
            classes = new Class<?>[1000];
            for (int i = 0; i < classes.length; i++) {
                ClassLoader loader = new URLClassLoader(new URL[] { testClasses.toURI().toURL() }, apploader.getParent());
                classes[i] = loader.loadClass(c.getSimpleName());
            }
            System.err.print(']');
            System.err.print('(');
            int threadCount = 64;
            Thread[] threads = new Thread[threadCount];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(new ArraysNewInstanceBug(i));
            }
            for (int i = 0; i < threads.length; i++) {
                threads[i].start();
            }
            for (int i = 0; i < threads.length; i++) {
                threads[i].join();
            }
            System.err.print(')');
        }
    }
}
