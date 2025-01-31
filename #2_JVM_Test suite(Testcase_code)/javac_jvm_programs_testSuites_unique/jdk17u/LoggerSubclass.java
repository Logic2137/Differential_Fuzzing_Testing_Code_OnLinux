import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LoggerSubclass {

    void test(String[] args) {
        final String name = "myLogger";
        final String message = "myMessage";
        final AtomicInteger getHandlerCount = new AtomicInteger(0);
        final AtomicLong lastSequenceNumber = new AtomicLong(-1L);
        final AtomicInteger lastThreadID = new AtomicInteger(-1);
        final Logger logger = new Logger(name, null) {

            public Handler[] getHandlers() {
                getHandlerCount.getAndIncrement();
                return super.getHandlers();
            }
        };
        equal(logger.getName(), name);
        equal(logger.getResourceBundle(), null);
        equal(logger.getFilter(), null);
        equal(logger.getLevel(), null);
        check(logger.isLoggable(Level.WARNING));
        logger.addHandler(new Handler() {

            public void close() {
            }

            public void flush() {
            }

            public void publish(LogRecord l) {
                equal(l.getLoggerName(), name);
                equal(l.getMessage(), message);
                equal(l.getResourceBundle(), null);
                equal(l.getSourceClassName(), "LoggerSubclass");
                equal(l.getSourceMethodName(), "test");
                equal(l.getThrown(), null);
                equal(l.getLevel(), Level.WARNING);
                if (lastSequenceNumber.get() != -1) {
                    equal(lastSequenceNumber.get() + 1, l.getSequenceNumber());
                    equal(lastThreadID.get(), l.getThreadID());
                    equal((int) Thread.currentThread().getId(), l.getThreadID());
                }
                lastSequenceNumber.set(l.getSequenceNumber());
                lastThreadID.set(l.getThreadID());
            }
        });
        for (int i = 1; i < 4; i++) {
            logger.warning(message);
            equal(i, getHandlerCount.get());
        }
    }

    volatile int passed = 0, failed = 0;

    void pass() {
        passed++;
    }

    void fail() {
        failed++;
        Thread.dumpStack();
    }

    void fail(String msg) {
        System.err.println(msg);
        fail();
    }

    void unexpected(Throwable t) {
        failed++;
        t.printStackTrace();
    }

    void check(boolean cond) {
        if (cond)
            pass();
        else
            fail();
    }

    void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y))
            pass();
        else
            fail(x + " not equal to " + y);
    }

    public static void main(String[] args) throws Throwable {
        try {
            new LoggerSubclass().instanceMain(args);
        } catch (Throwable e) {
            throw e.getCause() == null ? e : e.getCause();
        }
    }

    public void instanceMain(String[] args) throws Throwable {
        try {
            test(args);
        } catch (Throwable t) {
            unexpected(t);
        }
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0)
            throw new AssertionError("Some tests failed");
    }
}
