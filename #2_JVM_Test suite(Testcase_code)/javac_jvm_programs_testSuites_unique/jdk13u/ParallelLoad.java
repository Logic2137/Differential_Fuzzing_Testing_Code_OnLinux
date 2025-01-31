import java.io.*;
import java.net.*;
import java.lang.reflect.Field;

public class ParallelLoad {

    public static int MAX_CLASSES = 40;

    public static int NUM_THREADS = 4;

    public final static int SYSTEM_LOADER = 0;

    public final static int SINGLE_CUSTOM_LOADER = 1;

    public final static int MULTI_CUSTOM_LOADER = 2;

    public static final int FINGERPRINT_MODE = 1;

    public static final int API_MODE = 2;

    public static int loaderType = SYSTEM_LOADER;

    public static ClassLoader[] classLoaders;

    public static int mode = FINGERPRINT_MODE;

    public static float timeoutFactor = Float.parseFloat(System.getProperty("test.timeout.factor", "1.0"));

    public static void main(String[] args) throws Throwable {
        run(args, null);
    }

    public static void run(String[] args, ClassLoader[] loaders) throws Throwable {
        String customJar = null;
        System.out.println("ParallelLoad: timeoutFactor = " + timeoutFactor);
        if (args.length >= 1) {
            if ("SINGLE_CUSTOM_LOADER".equals(args[0])) {
                loaderType = SINGLE_CUSTOM_LOADER;
                customJar = args[2];
            } else if ("MULTI_CUSTOM_LOADER".equals(args[0])) {
                loaderType = MULTI_CUSTOM_LOADER;
                customJar = args[2];
            } else if ("SYSTEM_LOADER".equals(args[0])) {
                loaderType = SYSTEM_LOADER;
            } else {
                throw new RuntimeException("Unexpected loaderType" + args[0]);
            }
        }
        if (customJar != null) {
            if ("FINGERPRINT_MODE".equals(args[1])) {
                mode = FINGERPRINT_MODE;
                classLoaders = new ClassLoader[NUM_THREADS];
                for (int i = 0; i < NUM_THREADS; i++) {
                    URL url = new File(customJar).toURI().toURL();
                    URL[] urls = new URL[] { url };
                    classLoaders[i] = new URLClassLoader(urls);
                }
            } else {
                mode = API_MODE;
                classLoaders = loaders;
            }
        }
        System.out.println("Start Parallel Load ...");
        Thread[] thread = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread t = new ParallelLoadThread(i);
            t.start();
            thread[i] = t;
        }
        Thread watchdog = new ParallelLoadWatchdog();
        watchdog.setDaemon(true);
        watchdog.start();
        for (int i = 0; i < NUM_THREADS; i++) {
            thread[i].join();
        }
        System.out.println("Parallel Load ... done");
        System.exit(0);
    }
}

class ParallelLoadWatchdog extends Thread {

    public void run() {
        try {
            long timeout = (long) (20 * 1000 * ParallelLoad.timeoutFactor);
            Thread.sleep(timeout);
            System.out.println("ParallelLoadWatchdog: Timeout reached: timeout(ms) = " + timeout);
            System.exit(1);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
}

class ParallelLoadThread extends Thread {

    static int[] num_ready = new int[ParallelLoad.MAX_CLASSES];

    static Object lock = new Object();

    static String transformMode = System.getProperty("appcds.parallel.transform.mode", "none");

    int thread_id;

    ParallelLoadThread(int thread_id) {
        this.thread_id = thread_id;
    }

    public void run() {
        try {
            run0();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static void log(String msg, Object... args) {
        String msg0 = "ParallelLoadThread: " + String.format(msg, args);
        System.out.println(msg0);
    }

    private void run0() throws Throwable {
        for (int i = 0; i < ParallelLoad.MAX_CLASSES; i++) {
            synchronized (lock) {
                num_ready[i]++;
                while (num_ready[i] < ParallelLoad.NUM_THREADS) {
                    lock.wait();
                }
                lock.notifyAll();
            }
            log("this = %s %d", this, i);
            String className = "ParallelClass" + i;
            if (transformMode.equals("cflh"))
                className = "ParallelClassTr" + i;
            Class clazz = null;
            switch(ParallelLoad.loaderType) {
                case ParallelLoad.SYSTEM_LOADER:
                    clazz = Class.forName(className);
                    break;
                case ParallelLoad.SINGLE_CUSTOM_LOADER:
                    clazz = ParallelLoad.classLoaders[0].loadClass(className);
                    break;
                case ParallelLoad.MULTI_CUSTOM_LOADER:
                    clazz = ParallelLoad.classLoaders[thread_id].loadClass(className);
                    break;
            }
            log("clazz = %s", clazz);
            testTransformation(clazz);
        }
    }

    private void testTransformation(Class c) throws Exception {
        if (transformMode.equals("none"))
            return;
        if (!transformMode.equals("cflh")) {
            String msg = "wrong transform mode: " + transformMode;
            throw new IllegalArgumentException(msg);
        }
        Field[] fields = c.getFields();
        boolean fieldFound = false;
        for (Field f : fields) {
            if (f.getName().equals("testString")) {
                checkTransformationString(c, (String) f.get(null));
                fieldFound = true;
            }
        }
        if (!fieldFound)
            throw new RuntimeException("Expected field 'testString' not found");
    }

    private void checkTransformationString(Class c, String actual) throws Exception {
        String expected = "class-transform-check: this-has-been--transformed";
        if (!actual.equals(expected)) {
            String msg1 = "Transformation failed for class" + c.getName();
            String msg2 = String.format("Expected: %s, actual: %s", expected, actual);
            throw new RuntimeException(msg1 + "\n" + msg2);
        }
    }
}
