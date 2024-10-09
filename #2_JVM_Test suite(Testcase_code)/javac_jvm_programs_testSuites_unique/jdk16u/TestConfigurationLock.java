
import java.io.File;
import java.io.IOException;
import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.security.Permission;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;












public class TestConfigurationLock {

    static volatile Exception thrown = null;
    static volatile boolean goOn = true;
    static volatile boolean deadlock = false;

    static final double CONFSYNCTHRESHOLD = 0.3;
    static final double LOGSYNCTHRESHOLD = 0.3;
    static final int RESETERS = 0;
    static final int READERS = 3;
    static final int LOGGERS = 4;
    static final long TIME = 8 * 1000; 
    static final long STEP = 1 * 1000;  
    static final int  LCOUNT = 50; 
    static final AtomicLong nextLogger = new AtomicLong(0);
    static final AtomicLong resetCount = new AtomicLong(0);
    static final AtomicLong readCount = new AtomicLong(0);
    static final AtomicLong checkCount = new AtomicLong(0);

    static final String BLAH = "blah";

    static Object fakeConfExternalLock() {
        return LogManager.getLogManager();
    }

    static Object fakeLogExternalLock() {
        return LogManager.getLogManager();
    }


     
    public static void main(String[] args) throws Exception {

        File conf = new File(System.getProperty("test.src", "./src"),
                TestConfigurationLock.class.getSimpleName() + ".properties");
        if (!conf.canRead()) {
            throw new IOException("Can't read config file: " + conf.getAbsolutePath());
        }
        System.setProperty("java.util.logging.config.file", conf.getAbsolutePath());
        
        System.out.println("No security");
        test();

        
        System.out.println("\nWith security");
        Policy.setPolicy(new Policy() {
            @Override
            public boolean implies(ProtectionDomain domain, Permission permission) {
                if (super.implies(domain, permission)) return true;
                
                return true; 
            }
        });
        System.setSecurityManager(new SecurityManager());
        test();
    }


    
    public static void test() throws Exception {
          goOn = true;
          thrown = null;
          long sNextLogger = nextLogger.get();
          long sUpdateCount  = resetCount.get();
          long sReadCount  = readCount.get();
          long sCheckCount = checkCount.get();
          List<Thread> threads = new ArrayList<>();
          for (int i = 0; i<RESETERS; i++) {
              threads.add(new ResetConf());
          }
          for (int i = 0; i<READERS; i++) {
              threads.add(new ReadConf());
          }
          for (int i = 0; i<LOGGERS; i++) {
              threads.add(new AddLogger());
          }
          threads.add(0, new Stopper(TIME));
          threads.stream().forEach(Thread::start);

          Thread deadLockDetector = new DeadlockDetector();
          deadLockDetector.start();
          deadLockDetector.join();

          if (!deadlock) {
              threads.stream().forEach(TestConfigurationLock::join);
          } else {
              System.err.println("Deadlock found: exiting forcibly.");
              Runtime.getRuntime().halt(-1);
          }

          if (thrown != null) {
              throw thrown;
          }
          System.out.println("Passed: " + (nextLogger.get() - sNextLogger)
                  + " loggers created by " + LOGGERS + " Thread(s),");
          System.out.println("\t LogManager.reset() called "
                  + (resetCount.get() - sUpdateCount) + " times by " + RESETERS
                  + " Thread(s).");
          System.out.println("\t LogManager.readConfiguration() called "
                  + (readCount.get() - sReadCount) + " times by " + READERS
                  + " Thread(s).");
          System.out.println("\t ThreadMXBean.findDeadlockedThreads called "
                  + (checkCount.get() -sCheckCount) + " times by 1 Thread.");

    }

    static void join(Thread t) {
        try {
            t.join();
        } catch (Exception x) {
            fail(x);
        }
    }

    static final class ResetConf extends Thread {

        public ResetConf() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while (goOn) {
                try {
                    if (Math.random() > CONFSYNCTHRESHOLD) {
                        
                        
                        synchronized(fakeConfExternalLock()) {
                            LogManager.getLogManager().reset();
                        }
                    } else {
                        LogManager.getLogManager().reset();
                    }
                    Logger blah = Logger.getLogger(BLAH);
                    blah.setLevel(Level.FINEST);
                    blah.fine(BLAH);
                    resetCount.incrementAndGet();
                    pause(1);
                } catch (Exception x) {
                    fail(x);
                }
            }
        }
    }

    static final class ReadConf extends Thread {

        public ReadConf() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while (goOn) {
                try {
                    if (Math.random() > CONFSYNCTHRESHOLD) {
                        
                        
                        synchronized(fakeConfExternalLock()) {
                            LogManager.getLogManager().readConfiguration();
                        }
                    } else {
                        LogManager.getLogManager().readConfiguration();
                    }
                    Logger blah = Logger.getLogger(BLAH);
                    blah.setLevel(Level.FINEST);
                    blah.fine(BLAH);
                    readCount.incrementAndGet();
                    pause(1);
                } catch (Exception x) {
                    fail(x);
                }
            }
        }
    }

    static final class AddLogger extends Thread {

        public AddLogger() {
            setDaemon(true);
        }

        @Override
        public void run() {
            try {
                while (goOn) {
                    Logger l;
                    Logger foo = Logger.getLogger("foo");
                    Logger bar = Logger.getLogger("foo.bar");
                    for (int i=0; i < LCOUNT ; i++) {
                        LogManager manager = LogManager.getLogManager();
                        if (Math.random() > LOGSYNCTHRESHOLD) {
                            synchronized(fakeLogExternalLock()) {
                                l = Logger.getLogger("foo.bar.l"+nextLogger.incrementAndGet());
                            }
                        } else {
                            l = Logger.getLogger("foo.bar.l"+nextLogger.incrementAndGet());
                        }
                        l.setLevel(Level.FINEST);
                        l.fine("I'm fine");
                        if (!goOn) break;
                        pause(1);
                    }
                }
            } catch (InterruptedException | RuntimeException x ) {
                fail(x);
            }
        }
    }

    static final class DeadlockDetector extends Thread {

        @Override
        public void run() {
            boolean deadlock = false;
            while(goOn) {
                try {
                    long[] ids = ManagementFactory.getThreadMXBean().findDeadlockedThreads();
                    checkCount.incrementAndGet();
                    ids = ids == null ? new long[0] : ids;
                    if (ids.length == 1) {
                        throw new RuntimeException("Found 1 deadlocked thread: "+ids[0]);
                    } else if (ids.length > 0) {
                        deadlock = true;
                        ThreadInfo[] infos = ManagementFactory.getThreadMXBean()
                            .getThreadInfo(ids, true, true);
                        System.err.println("Found "+ids.length+" deadlocked threads: ");
                        for (ThreadInfo inf : infos) {
                            System.err.println(asString(inf));
                        }
                        throw new RuntimeException("Found "+ids.length+" deadlocked threads");
                    }
                    pause(100);
                } catch(InterruptedException | RuntimeException x) {
                    if (deadlock) deadlock(x);
                    else fail(x);
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
            setDaemon(true);
        }

        @Override
        public void run() {
            try {
                long rest, previous;
                int msgCount = 0;
                previous = time;
                Logger logger =  Logger.getLogger("remaining");
                while (goOn && (rest = start - System.currentTimeMillis() + time) > 0) {
                    if (previous == time || previous - rest >= STEP) {
                        logger.log(Level.INFO, "{0}ms remaining...", String.valueOf(rest));
                        msgCount++;
                        previous = rest == time ? rest -1 : rest;
                        System.gc();
                    }
                    if (goOn == false) break;
                    pause(Math.min(rest, 100));
                }
                System.err.println(this + ": " + msgCount + " messages.");
                System.err.flush();
                System.out.println(System.currentTimeMillis() - start
                        + " ms elapsed ("+time+ " requested)");
                goOn = false;
            } catch(InterruptedException | RuntimeException x) {
                fail(x);
            }
        }

    }

    
    static String asString(ThreadInfo inf) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(inf.getThreadName()).append("\"")
                .append(inf.isDaemon() ? " daemon" : "")
                .append(" prio=").append(inf.getPriority())
                .append(" Id=").append(inf.getThreadId())
                .append(" ").append(inf.getThreadState());
        if (inf.getLockName() != null) {
            sb.append(" on ").append(inf.getLockName());
        }
        if (inf.getLockOwnerName() != null) {
            sb.append(" owned by \"").append(inf.getLockOwnerName())
                    .append("\" Id=").append(inf.getLockOwnerId());
        }
        if (inf.isSuspended()) {
            sb.append(" (suspended)");
        }
        if (inf.isInNative()) {
            sb.append(" (in native)");
        }
        sb.append('\n');
        int i = 0;
        StackTraceElement[] stackTrace = inf.getStackTrace();
        for (; i < stackTrace.length; i++) {
            StackTraceElement ste = stackTrace[i];
            sb.append("\tat ").append(ste.toString());
            sb.append('\n');
            if (i == 0 && inf.getLockInfo() != null) {
                Thread.State ts = inf.getThreadState();
                switch (ts) {
                    case BLOCKED:
                        sb.append("\t-  blocked on ").append(inf.getLockInfo());
                        sb.append('\n');
                        break;
                    case WAITING:
                        sb.append("\t-  waiting on ").append(inf.getLockInfo());
                        sb.append('\n');
                        break;
                    case TIMED_WAITING:
                        sb.append("\t-  waiting on ").append(inf.getLockInfo());
                        sb.append('\n');
                        break;
                    default:
                }
            }

            for (MonitorInfo mi : inf.getLockedMonitors()) {
                if (mi.getLockedStackDepth() == i) {
                    sb.append("\t-  locked ").append(mi);
                    sb.append('\n');
                }
            }
        }
        if (i < stackTrace.length) {
           sb.append("\t...");
           sb.append('\n');
        }

        LockInfo[] locks = inf.getLockedSynchronizers();
        if (locks.length > 0) {
           sb.append("\n\tNumber of locked synchronizers = ").append(locks.length);
           sb.append('\n');
           for (LockInfo li : locks) {
               sb.append("\t- ").append(li);
               sb.append('\n');
           }
        }
        sb.append('\n');
        return sb.toString();
    }

    static void pause(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    static void fail(Exception x) {
        x.printStackTrace(System.err);
        if (thrown == null) {
            thrown = x;
        }
        goOn = false;
    }

    static void deadlock(Exception x) {
        deadlock = true;
        System.out.flush();
        fail(x);
        System.err.flush();
    }
}
