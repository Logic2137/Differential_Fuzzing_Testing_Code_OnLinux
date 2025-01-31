import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.security.Permission;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class TestLoggerBundleSync {

    static final boolean VERBOSE = false;

    static volatile Exception thrown = null;

    static volatile boolean goOn = true;

    static final int READERS = 3;

    static final long TIME = 4 * 1000;

    static final long STEP = 1 * 1000;

    static final int LCOUNT = 50;

    static final AtomicLong ignoreLogCount = new AtomicLong(0);

    static final AtomicLong setRBcount = new AtomicLong(0);

    static final AtomicLong setRBNameCount = new AtomicLong(0);

    static final AtomicLong getRBcount = new AtomicLong(0);

    static final AtomicLong checkCount = new AtomicLong(0);

    static final AtomicLong nextLong = new AtomicLong(0);

    public static class MyBundle extends ListResourceBundle {

        @Override
        protected Object[][] getContents() {
            return new Object[][] { { "dummy", "foo" } };
        }
    }

    public static final class MyBundle1 extends MyBundle {
    }

    public static final class MyBundle2 extends MyBundle {
    }

    public static final class MyBundle3 extends MyBundle {
    }

    public static final class LoggerRB {

        public final String resourceBundleName;

        public final ResourceBundle userBundle;

        public LoggerRB(String name, ResourceBundle bundle) {
            resourceBundleName = name;
            userBundle = bundle;
        }
    }

    static final List<Class<? extends ResourceBundle>> classes = new ArrayList<>();

    static {
        classes.add(MyBundle1.class);
        classes.add(MyBundle2.class);
        classes.add(MyBundle3.class);
    }

    public static void main(String[] args) throws Exception {
        try {
            System.out.println("No security");
            test();
            System.out.println("\nWith security");
            Policy.setPolicy(new Policy() {

                @Override
                public boolean implies(ProtectionDomain domain, Permission permission) {
                    if (super.implies(domain, permission))
                        return true;
                    return true;
                }
            });
            System.setSecurityManager(new SecurityManager());
            test();
        } finally {
            SetRB.executor.shutdownNow();
            SetRBName.executor.shutdownNow();
        }
    }

    public static void test() throws Exception {
        goOn = true;
        thrown = null;
        long sGetRBCount = getRBcount.get();
        long sSetRBCount = setRBcount.get();
        long sSetRBNameCount = setRBNameCount.get();
        long sCheckCount = checkCount.get();
        long sNextLong = nextLong.get();
        long sIgnoreLogCount = ignoreLogCount.get();
        List<Thread> threads = new ArrayList<>();
        for (Class<? extends ResourceBundle> type : classes) {
            threads.add(new SetRB(type));
            threads.add(new SetRBName(type));
        }
        for (int i = 0; i < READERS; i++) {
            threads.add(new GetRB());
        }
        threads.add(new DeadlockDetector());
        threads.add(0, new Stopper(TIME));
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (Exception x) {
                fail(x);
            }
        }
        if (thrown != null) {
            throw thrown;
        }
        System.out.println("Passed: " + (nextLong.longValue() - sNextLong) + " unique loggers created");
        System.out.println("\t " + (getRBcount.get() - sGetRBCount) + " loggers tested by " + READERS + " Thread(s),");
        System.out.println("\t " + (setRBcount.get() - sSetRBCount) + " resource bundles set by " + classes.size() + " Thread(s),");
        System.out.println("\t " + (setRBNameCount.get() - sSetRBNameCount) + " resource bundle names set by " + classes.size() + " Thread(s),");
        System.out.println("\t " + (ignoreLogCount.get() - sIgnoreLogCount) + " log messages emitted by other GetRB threads were ignored" + " to ensure MT test consistency,");
        System.out.println("\t ThreadMXBean.findDeadlockedThreads called " + (checkCount.get() - sCheckCount) + " times by 1 Thread.");
    }

    static final class GetRB extends Thread {

        final class MyHandler extends Handler {

            volatile ResourceBundle rb;

            volatile String rbName;

            volatile int count = 0;

            @Override
            public synchronized void publish(LogRecord record) {
                Object[] params = record.getParameters();
                if (params.length == 1) {
                    if (params[0] == GetRB.this) {
                        count++;
                        rb = record.getResourceBundle();
                        rbName = record.getResourceBundleName();
                    } else {
                        if (VERBOSE) {
                            System.out.println("Ignoring message logged by " + params[0]);
                        }
                        ignoreLogCount.incrementAndGet();
                    }
                } else {
                    ignoreLogCount.incrementAndGet();
                    System.err.println("Unexpected message received");
                }
            }

            void reset() {
                rbName = null;
                rb = null;
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        }

        final MyHandler handler = new MyHandler();

        @Override
        public void run() {
            try {
                handler.setLevel(Level.FINEST);
                while (goOn) {
                    Logger l;
                    Logger foo = Logger.getLogger("foo");
                    Logger bar = Logger.getLogger("foo.bar");
                    for (long i = 0; i < nextLong.longValue() + 100; i++) {
                        if (!goOn)
                            break;
                        l = Logger.getLogger("foo.bar.l" + i);
                        final ResourceBundle b = l.getResourceBundle();
                        final String name = l.getResourceBundleName();
                        if (b != null) {
                            if (!name.equals(b.getBaseBundleName())) {
                                throw new RuntimeException("Unexpected bundle name: " + b.getBaseBundleName());
                            }
                        }
                        Logger ll = Logger.getLogger(l.getName() + ".bie.bye");
                        ResourceBundle hrb;
                        String hrbName;
                        if (handler.getLevel() != Level.FINEST) {
                            throw new RuntimeException("Handler level is not finest: " + handler.getLevel());
                        }
                        final int countBefore = handler.count;
                        handler.reset();
                        ll.setLevel(Level.FINEST);
                        ll.addHandler(handler);
                        ll.log(Level.FINE, "dummy {0}", this);
                        ll.removeHandler(handler);
                        final int countAfter = handler.count;
                        if (countBefore == countAfter) {
                            throw new RuntimeException("Handler not called for " + ll.getName() + "(" + countAfter + ")");
                        }
                        hrb = handler.rb;
                        hrbName = handler.rbName;
                        if (name != null) {
                            if (!name.equals(hrbName)) {
                                throw new RuntimeException("Unexpected bundle name: " + hrbName);
                            }
                            if (!name.equals(hrb.getBaseBundleName())) {
                                throw new RuntimeException("Unexpected bundle name: " + hrb.getBaseBundleName());
                            }
                        }
                        if (!ll.getName().startsWith(l.getName())) {
                            throw new RuntimeException("Logger " + ll.getName() + "does not start with expected prefix " + l.getName());
                        }
                        getRBcount.incrementAndGet();
                        if (!goOn)
                            break;
                        Thread.sleep(1);
                    }
                }
            } catch (Exception x) {
                fail(x);
            }
        }
    }

    static final class SetRB extends Thread {

        final Class<? extends ResourceBundle> type;

        static final ExecutorService executor = Executors.newSingleThreadExecutor();

        static final class CheckRBTask implements Callable<Exception> {

            final Logger logger;

            volatile String rbName;

            volatile ResourceBundle rb;

            public CheckRBTask(Logger logger) {
                this.logger = logger;
            }

            @Override
            public Exception call() throws Exception {
                try {
                    final String name = logger.getResourceBundleName();
                    if (!Objects.equals(name, rbName)) {
                        throw new RuntimeException("Unexpected rbname for " + logger.getName() + ": " + name);
                    }
                    final ResourceBundle b = logger.getResourceBundle();
                    if (b != rb) {
                        throw new RuntimeException("Unexpected rb for " + logger.getName() + ": " + b);
                    }
                } catch (Exception x) {
                    return x;
                }
                return null;
            }

            public void check() throws Exception {
                final FutureTask<Exception> futureTask = new FutureTask<>(this);
                executor.submit(futureTask);
                Exception x = futureTask.get();
                if (x != null) {
                    throw new RuntimeException("Check failed: " + x, x);
                }
            }
        }

        SetRB(Class<? extends ResourceBundle> type) {
            super("SetRB[" + type.getSimpleName() + "]");
            this.type = type;
        }

        @Override
        public void run() {
            try {
                while (goOn) {
                    Logger l;
                    Logger foo = Logger.getLogger("foo");
                    Logger bar = Logger.getLogger("foo.bar");
                    l = Logger.getLogger("foo.bar.l" + nextLong.incrementAndGet());
                    final CheckRBTask checkTask = new CheckRBTask(l);
                    checkTask.check();
                    for (int i = 0; i < LCOUNT; i++) {
                        if (!goOn)
                            break;
                        ResourceBundle b = ResourceBundle.getBundle(type.getName());
                        try {
                            l.setResourceBundle(b);
                            checkTask.rb = b;
                            checkTask.rbName = type.getName();
                            checkTask.check();
                            if (!goOn)
                                break;
                            String name = l.getResourceBundleName();
                            ResourceBundle bb = l.getResourceBundle();
                            if (!type.getName().equals(name)) {
                                throw new RuntimeException(this.getName() + ": Unexpected name: " + name);
                            }
                            if (!b.getBaseBundleName().equals(name)) {
                                throw new RuntimeException(this.getName() + ": Unexpected base name: " + b.getBaseBundleName());
                            }
                            if (b != bb) {
                                throw new RuntimeException(this.getName() + ": Unexpected bundle: " + bb);
                            }
                            setRBcount.incrementAndGet();
                        } catch (IllegalArgumentException x) {
                            final String name = l.getResourceBundleName();
                            if (!name.startsWith(MyBundle.class.getName())) {
                                throw new RuntimeException(this.getName() + ": Unexpected name: " + name, x);
                            } else if (type.getName().equals(name)) {
                                throw new RuntimeException(this.getName() + ": Unexpected exception for " + name, x);
                            }
                            throw x;
                        }
                        l.fine("I'm fine");
                        if (!goOn)
                            break;
                        Thread.sleep(1);
                    }
                }
            } catch (Exception x) {
                fail(x);
            }
        }
    }

    static final class SetRBName extends Thread {

        int nexti = 0;

        final Class<? extends ResourceBundle> type;

        static final ExecutorService executor = Executors.newSingleThreadExecutor();

        static final class CheckRBNameTask implements Callable<Exception> {

            final Logger logger;

            volatile String rbName;

            public CheckRBNameTask(Logger logger) {
                this.logger = logger;
            }

            @Override
            public Exception call() throws Exception {
                try {
                    final String name = logger.getResourceBundleName();
                    if (!Objects.equals(name, rbName)) {
                        throw new RuntimeException("Unexpected rbname for " + logger.getName() + ": " + name);
                    }
                    final ResourceBundle b = logger.getResourceBundle();
                    if (!Objects.equals(b == null ? null : b.getBaseBundleName(), rbName)) {
                        throw new RuntimeException("Unexpected base name for " + logger.getName() + ": " + b.getBaseBundleName());
                    }
                } catch (Exception x) {
                    return x;
                }
                return null;
            }

            public void check() throws Exception {
                final FutureTask<Exception> futureTask = new FutureTask<>(this);
                executor.submit(futureTask);
                Exception x = futureTask.get();
                if (x != null) {
                    throw new RuntimeException("Check failed: " + x, x);
                }
            }
        }

        SetRBName(Class<? extends ResourceBundle> type) {
            super("SetRB[" + type.getSimpleName() + "]");
            this.type = type;
        }

        @Override
        public void run() {
            try {
                while (goOn) {
                    Logger foo = Logger.getLogger("foo");
                    Logger bar = Logger.getLogger("foo.bar");
                    Logger l = Logger.getLogger("foo.bar.l" + nextLong.incrementAndGet());
                    final CheckRBNameTask checkTask = new CheckRBNameTask(l);
                    checkTask.check();
                    for (int i = 0; i < LCOUNT; i++) {
                        if (!goOn)
                            break;
                        try {
                            Logger l2 = Logger.getLogger(l.getName(), type.getName());
                            if (l2 != l) {
                                System.err.println("**** ERROR WITH " + l.getName());
                                throw new RuntimeException("l2 != l [" + l2 + "(" + l2.getName() + ") != " + l + "(" + l.getName() + ")]");
                            }
                            checkTask.rbName = type.getName();
                            checkTask.check();
                            if (!goOn)
                                break;
                            String name = l.getResourceBundleName();
                            ResourceBundle bb = l.getResourceBundle();
                            if (!type.getName().equals(name)) {
                                throw new RuntimeException(this.getName() + ": Unexpected name: " + name);
                            }
                            if (!bb.getBaseBundleName().equals(name)) {
                                throw new RuntimeException(this.getName() + ": Unexpected base name: " + bb.getBaseBundleName());
                            }
                            setRBNameCount.incrementAndGet();
                        } catch (IllegalArgumentException x) {
                            final String name = l.getResourceBundleName();
                            if (!name.startsWith(MyBundle.class.getName())) {
                                throw new RuntimeException(this.getName() + ": Unexpected name: " + name, x);
                            } else if (type.getName().equals(name)) {
                                throw new RuntimeException(this.getName() + ": Unexpected exception for " + name, x);
                            }
                            throw x;
                        }
                        l.fine("I'm fine");
                        if (!goOn)
                            break;
                        Thread.sleep(1);
                    }
                }
            } catch (Exception x) {
                fail(x);
            }
        }
    }

    static final class DeadlockDetector extends Thread {

        @Override
        public void run() {
            while (goOn) {
                try {
                    long[] ids = ManagementFactory.getThreadMXBean().findDeadlockedThreads();
                    checkCount.incrementAndGet();
                    ids = ids == null ? new long[0] : ids;
                    if (ids.length == 1) {
                        throw new RuntimeException("Found 1 deadlocked thread: " + ids[0]);
                    } else if (ids.length > 0) {
                        ThreadInfo[] infos = ManagementFactory.getThreadMXBean().getThreadInfo(ids);
                        System.err.println("Found " + ids.length + " deadlocked threads: ");
                        for (ThreadInfo inf : infos) {
                            System.err.println(inf.toString());
                        }
                        throw new RuntimeException("Found " + ids.length + " deadlocked threads");
                    }
                    Thread.sleep(100);
                } catch (InterruptedException | RuntimeException x) {
                    fail(x);
                }
            }
        }
    }

    static final class Stopper extends Thread {

        long start;

        long time;

        Stopper(long time) {
            start = System.currentTimeMillis();
            this.time = time;
        }

        @Override
        public void run() {
            try {
                long rest, previous;
                previous = time;
                while (goOn && (rest = start - System.currentTimeMillis() + time) > 0) {
                    if (previous == time || previous - rest >= STEP) {
                        Logger.getLogger("remaining").info(String.valueOf(rest) + "ms remaining...");
                        previous = rest == time ? rest - 1 : rest;
                        System.gc();
                    }
                    if (goOn == false)
                        break;
                    Thread.sleep(Math.min(rest, 100));
                }
                System.out.println(System.currentTimeMillis() - start + " ms elapsed (" + time + " requested)");
                goOn = false;
            } catch (InterruptedException | RuntimeException x) {
                fail(x);
            }
        }
    }

    static void fail(Exception x) {
        x.printStackTrace();
        if (thrown == null) {
            thrown = x;
        }
        goOn = false;
    }
}
