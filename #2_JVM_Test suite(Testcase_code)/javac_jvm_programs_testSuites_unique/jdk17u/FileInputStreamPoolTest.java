import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.HashSet;

public class FileInputStreamPoolTest {

    static final byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };

    static FilterInputStream testCaching(File file) throws IOException {
        InputStream in1 = TestProxy.FileInputStreamPool_getInputStream(file);
        InputStream in2 = TestProxy.FileInputStreamPool_getInputStream(file);
        assertTrue(in1 == in2, "1st InputStream: " + in1 + " is not same as 2nd: " + in2);
        byte[] readBytes = new byte[bytes.length];
        int nread = in1.read(readBytes);
        assertTrue(bytes.length == nread, "short read: " + nread + " bytes of expected: " + bytes.length);
        assertTrue(Arrays.equals(readBytes, bytes), "readBytes: " + Arrays.toString(readBytes) + " not equal to expected: " + Arrays.toString(bytes));
        return (FilterInputStream) in1;
    }

    static void assertTrue(boolean test, String message) {
        if (!test) {
            throw new AssertionError(message);
        }
    }

    static void processReferences(FilterInputStream in1) throws InterruptedException, IOException {
        FileInputStream fis = TestProxy.FilterInputStream_getInField(in1);
        FileDescriptor fd = fis.getFD();
        System.out.printf("fis: %s, fd: %s%n", fis, fd);
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        HashSet<Reference<?>> pending = new HashSet<>();
        pending.add(new WeakReference<>(in1, queue));
        pending.add(new WeakReference<>(fis, queue));
        pending.add(new WeakReference<>(fd, queue));
        Reference<?> r;
        while (((r = queue.remove(10L)) != null) || !pending.isEmpty()) {
            System.out.printf("r: %s, pending: %d%n", r, pending.size());
            if (r != null) {
                pending.remove(r);
            } else {
                fd = null;
                fis = null;
                in1 = null;
                System.gc();
            }
            Thread.sleep(10L);
        }
        Reference.reachabilityFence(fd);
        Reference.reachabilityFence(fis);
        Reference.reachabilityFence(in1);
    }

    public static void main(String[] args) throws Exception {
        File file = File.createTempFile("test", ".dat");
        try (AutoCloseable acf = () -> {
            assertTrue(file.delete(), "Can't delete: " + file + " (is it still open?)");
        }) {
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(bytes);
            }
            processReferences(testCaching(file));
            processReferences(testCaching(file));
        }
    }

    static class TestProxy {

        private static final Method getInputStreamMethod;

        private static final Method waitForReferenceProcessingMethod;

        private static final Field inField;

        static {
            try {
                Class<?> fileInputStreamPoolClass = Class.forName("sun.security.provider.FileInputStreamPool");
                getInputStreamMethod = fileInputStreamPoolClass.getDeclaredMethod("getInputStream", File.class);
                getInputStreamMethod.setAccessible(true);
                waitForReferenceProcessingMethod = Reference.class.getDeclaredMethod("waitForReferenceProcessing");
                waitForReferenceProcessingMethod.setAccessible(true);
                inField = FilterInputStream.class.getDeclaredField("in");
                inField.setAccessible(true);
            } catch (Exception e) {
                throw new Error(e);
            }
        }

        static InputStream FileInputStreamPool_getInputStream(File file) throws IOException {
            try {
                return (InputStream) getInputStreamMethod.invoke(null, file);
            } catch (InvocationTargetException e) {
                Throwable te = e.getTargetException();
                if (te instanceof IOException) {
                    throw (IOException) te;
                } else if (te instanceof RuntimeException) {
                    throw (RuntimeException) te;
                } else if (te instanceof Error) {
                    throw (Error) te;
                } else {
                    throw new UndeclaredThrowableException(te);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        static boolean Reference_waitForReferenceProcessing() {
            try {
                return (boolean) waitForReferenceProcessingMethod.invoke(null);
            } catch (InvocationTargetException e) {
                Throwable te = e.getTargetException();
                if (te instanceof InterruptedException) {
                    return true;
                } else if (te instanceof RuntimeException) {
                    throw (RuntimeException) te;
                } else if (te instanceof Error) {
                    throw (Error) te;
                } else {
                    throw new UndeclaredThrowableException(te);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        static FileInputStream FilterInputStream_getInField(FilterInputStream fis) {
            try {
                return (FileInputStream) inField.get(fis);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
