



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class SerializationDeadlock {
    public static void main(final String[] args) throws Exception {
        
        final Hashtable<Object, Object> h1 = new Hashtable<>();
        final Hashtable<Object, Object> h2 = new Hashtable<>();
        final TestBarrier testStart = new TestBarrier(3);

        
        h1.put(testStart, h2);
        h2.put(testStart, h1);

        final CyclicBarrier testEnd = new CyclicBarrier(3);
        final TestThread t1 = new TestThread(h1, testEnd);
        final TestThread t2 = new TestThread(h2, testEnd);

        t1.start();
        t2.start();

        
        
        testStart.await();

        
        
        System.out.println("Waiting for Hashtable serialization to complete ...");
        System.out.println("(This test will hang if serialization deadlocks)");
        testEnd.await();
        System.out.println("Test PASSED: serialization completed successfully");

        TestThread.handleExceptions();
    }

    static final class TestBarrier extends CyclicBarrier
            implements Serializable {
        public TestBarrier(final int count) {
            super(count);
        }

        private void writeObject(final ObjectOutputStream oos)
                throws IOException {
            oos.defaultWriteObject();
            
            try {
                await();
            } catch (final Exception e) {
                throw new IOException("Test ERROR: Unexpected exception caught", e);
            }
        }
    }

    static final class TestThread extends Thread {
        private static final List<Exception> exceptions = new ArrayList<>();

        private final Hashtable<Object, Object> hashtable;
        private final CyclicBarrier testEnd;

        public TestThread(final Hashtable<Object, Object> hashtable,
                final CyclicBarrier testEnd) {
            this.hashtable = hashtable;
            this.testEnd = testEnd;
            setDaemon(true);
        }

        public void run() {
            try {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final ObjectOutputStream oos = new ObjectOutputStream(baos);

                oos.writeObject(hashtable);
                oos.close();
            } catch (final IOException ioe) {
                addException(ioe);
            } finally {
                try {
                    testEnd.await();
                } catch (Exception e) {
                    addException(e);
                }
            }
        }

        private static synchronized void addException(final Exception exception) {
            exceptions.add(exception);
        }

        public static synchronized void handleExceptions() {
            if (false == exceptions.isEmpty()) {
                throw new RuntimeException(getErrorText(exceptions));
            }
        }

        private static String getErrorText(final List<Exception> exceptions) {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);

            pw.println("Test ERROR: Unexpected exceptions thrown on test threads:");
            for (Exception exception : exceptions) {
                pw.print("\t");
                pw.println(exception);
                for (StackTraceElement element : exception.getStackTrace()) {
                    pw.print("\t\tat ");
                    pw.println(element);
                }
            }

            pw.close();
            return sw.toString();
        }
    }
}

