



import java.io.PrintStream;

public class SuspendWithCurrentThread {
    private static final String AGENT_LIB = "SuspendWithCurrentThread";
    private static final String SUSPENDER_OPT = "SuspenderIndex=";
    private static final int THREADS_COUNT = 10;

    private static void log(String msg) { System.out.println(msg); }

    private static native void    registerTestedThreads(Thread[] threads);
    private static native boolean checkTestedThreadsSuspended();
    private static native void    resumeTestedThreads();
    private static native void    releaseTestedThreadsInfo();

    
    
    private static int suspenderIndex;

    public static void main(String args[]) throws Exception {
        try {
            System.loadLibrary(AGENT_LIB);
            log("Loaded library: " + AGENT_LIB);
        } catch (UnsatisfiedLinkError ule) {
            log("Failed to load library: " + AGENT_LIB);
            log("java.library.path: " + System.getProperty("java.library.path"));
            throw ule;
        }
        if (args.length != 1) {
            throw new RuntimeException("Main: wrong arguments count: " + args.length + ", expected: 1");
        }
        String arg = args[0];
        if (arg.equals(SUSPENDER_OPT + "first")) {
            suspenderIndex = 0;
        } else if (arg.equals(SUSPENDER_OPT + "last")) {
            suspenderIndex = THREADS_COUNT - 1;
        } else {
            throw new RuntimeException("Main: wrong argument: " + arg + ", expected: SuspenderIndex={first|last}");
        }
        log("Main: suspenderIndex: " + suspenderIndex);

        SuspendWithCurrentThread test = new SuspendWithCurrentThread();
        test.run();
    }

    private ThreadToSuspend[] startTestedThreads(int threadsCount) throws RuntimeException  {
        ThreadToSuspend[]threads = new ThreadToSuspend[threadsCount];

        
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ThreadToSuspend("ThreadToSuspend#" + i,
                                             i == suspenderIndex 
                                            );
        }
        log("Main: starting tested threads");
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
            if (!threads[i].checkReady()) {
                throw new RuntimeException("Main: unable to prepare tested thread: " + threads[i]);
            }
        }
        log("Main: tested threads started");

        registerTestedThreads(threads);
        return threads;
    }

    private boolean checkSuspendedStatus() throws RuntimeException  {
        log("Main: checking all tested threads have been suspended");
        return checkTestedThreadsSuspended();
    }

    
    private void run() throws Exception {
        ThreadToSuspend[] threads = null; 

        log("Main: started");
        try {
            threads = startTestedThreads(THREADS_COUNT);

            log("Main: trigger " + threads[suspenderIndex].getName() +
                " to suspend all tested threads including itself");
            ThreadToSuspend.setAllThreadsReady();

            if (!checkSuspendedStatus()) {
                throw new RuntimeException("Main: FAILED status returned from checkTestedThreadsSuspended");
            }

            log("Main: resuming all tested threads");
            resumeTestedThreads();
        } finally {
            
            for (int i = 0; i < threads.length; i++) {
                threads[i].letFinish();
            }
            log("Main: tested threads finished");
        }

        
        log("Main: joining tested threads");
        try {
            for (int i = 0; i < threads.length; i++) {
                threads[i].join();
            }
            log("Main: tested thread joined");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log("Main: releasing tested threads native info");
        releaseTestedThreadsInfo();

        log("Main: finished");
    }
}




class ThreadToSuspend extends Thread {
    private static void log(String msg) { System.out.println(msg); }

    private static native void init();
    private static native void suspendTestedThreads();
    private static volatile boolean allThreadsReady = false;

    public static void setAllThreadsReady() {
        allThreadsReady = true;
    }

    private volatile boolean threadReady = false;
    private volatile boolean shouldFinish = false;
    private boolean isSuspender = false;

    
    public ThreadToSuspend(String name, boolean isSuspender) {
        super(name);
        this.isSuspender = isSuspender;
    }

    
    public void run() {
        boolean needSuspend = true;

        if (isSuspender) {
            init();
        }
        threadReady = true;

        
        while (!shouldFinish) {
            if (isSuspender && needSuspend && allThreadsReady) {
                log(getName() + ": before suspending all tested threads including myself");
                needSuspend = false;
                suspendTestedThreads();
                log(getName() + ": after suspending all tested threads including myself");
            }
        }
    }

    
    public boolean checkReady() {
        try {
            while (!threadReady) {
                sleep(1);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("checkReady: sleep was interrupted\n\t" + e);
        }
        return threadReady;
    }

    
    public void letFinish() {
        shouldFinish = true;
    }
}
