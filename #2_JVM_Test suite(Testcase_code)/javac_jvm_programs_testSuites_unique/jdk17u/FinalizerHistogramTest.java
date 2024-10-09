import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class FinalizerHistogramTest {

    static ReentrantLock lock = new ReentrantLock();

    static volatile int wasInitialized = 0;

    static volatile int wasTrapped = 0;

    static final int objectsCount = 1000;

    static class MyObject {

        public MyObject() {
            wasInitialized += 1;
        }

        protected void finalize() {
            wasTrapped += 1;
            lock.lock();
        }
    }

    public static void main(String[] argvs) {
        try {
            lock.lock();
            for (int i = 0; i < objectsCount; ++i) {
                new MyObject();
            }
            System.out.println("Objects intialized: " + objectsCount);
            System.gc();
            while (wasTrapped < 1) ;
            Class<?> klass = Class.forName("java.lang.ref.FinalizerHistogram");
            Method m = klass.getDeclaredMethod("getFinalizerHistogram");
            m.setAccessible(true);
            Object[] entries = (Object[]) m.invoke(null);
            Class<?> entryKlass = Class.forName("java.lang.ref.FinalizerHistogram$Entry");
            Field name = entryKlass.getDeclaredField("className");
            name.setAccessible(true);
            Field count = entryKlass.getDeclaredField("instanceCount");
            count.setAccessible(true);
            System.out.println("Unreachable instances waiting for finalization");
            System.out.println("#instances  class name");
            System.out.println("-----------------------");
            boolean found = false;
            for (Object entry : entries) {
                Object e = entryKlass.cast(entry);
                System.out.printf("%10d %s\n", count.get(e), name.get(e));
                if (((String) name.get(e)).indexOf("MyObject") != -1) {
                    found = true;
                }
            }
            if (!found) {
                throw new RuntimeException("MyObject is not found in test output");
            }
            System.out.println("Test PASSED");
        } catch (Exception e) {
            System.err.println("Test failed with " + e);
            e.printStackTrace(System.err);
            throw new RuntimeException("Test failed");
        } finally {
            lock.unlock();
        }
    }
}
