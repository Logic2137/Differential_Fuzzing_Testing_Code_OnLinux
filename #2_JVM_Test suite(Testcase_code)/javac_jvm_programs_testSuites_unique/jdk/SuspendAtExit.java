



import java.util.concurrent.CountDownLatch;

public class SuspendAtExit extends Thread {
    private final static String AGENT_LIB = "SuspendAtExit";
    private final static int DEF_TIME_MAX = 30;  
    private final static int JVMTI_ERROR_THREAD_NOT_ALIVE = 15;

    public CountDownLatch exitSyncObj = new CountDownLatch(1);
    public CountDownLatch startSyncObj = new CountDownLatch(1);

    private static void log(String msg) { System.out.println(msg); }

    native static int resumeThread(SuspendAtExit thr);
    native static int suspendThread(SuspendAtExit thr);

    @Override
    public void run() {
        
        startSyncObj.countDown();
        try {
            
            exitSyncObj.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected: " + e);
        }
    }

    public static void main(String[] args) {
        try {
            System.loadLibrary(AGENT_LIB);
            log("Loaded library: " + AGENT_LIB);
        } catch (UnsatisfiedLinkError ule) {
            log("Failed to load library: " + AGENT_LIB);
            log("java.library.path: " + System.getProperty("java.library.path"));
            throw ule;
        }

        int timeMax = 0;
        if (args.length == 0) {
            timeMax = DEF_TIME_MAX;
        } else {
            try {
                timeMax = Integer.parseUnsignedInt(args[0]);
            } catch (NumberFormatException nfe) {
                System.err.println("'" + args[0] + "': invalid timeMax value.");
                    usage();
            }
        }

        System.out.println("About to execute for " + timeMax + " seconds.");

        long count = 0;
        long start_time = System.currentTimeMillis();
        while (System.currentTimeMillis() < start_time + (timeMax * 1000)) {
            count++;

            int retCode;
            SuspendAtExit thread = new SuspendAtExit();
            thread.start();
            try {
                
                thread.startSyncObj.await();
                
                
                thread.exitSyncObj.countDown();
                while (true) {
                    retCode = suspendThread(thread);

                    if (retCode == JVMTI_ERROR_THREAD_NOT_ALIVE) {
                        
                        
                        break;
                    } else if (retCode != 0) {
                        throw new RuntimeException("thread " + thread.getName()
                                                   + ": suspendThread() " +
                                                   "retCode=" + retCode +
                                                   ": unexpected value.");
                    }

                    if (!thread.isAlive()) {
                        throw new RuntimeException("thread " + thread.getName()
                                                   + ": is not alive " +
                                                   "after successful " +
                                                   "suspendThread().");
                    }
                    retCode = resumeThread(thread);
                    if (retCode != 0) {
                        throw new RuntimeException("thread " + thread.getName()
                                                   + ": resumeThread() " +
                                                   "retCode=" + retCode +
                                                   ": unexpected value.");
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Unexpected: " + e);
            }

            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException("Unexpected: " + e);
            }
            retCode = suspendThread(thread);
            if (retCode != JVMTI_ERROR_THREAD_NOT_ALIVE) {
                throw new RuntimeException("thread " + thread.getName() +
                                           ": suspendThread() " +
                                           "retCode=" + retCode +
                                           ": unexpected value.");
            }
            retCode = resumeThread(thread);
            if (retCode != JVMTI_ERROR_THREAD_NOT_ALIVE) {
                throw new RuntimeException("thread " + thread.getName() +
                                           ": suspendThread() " +
                                           "retCode=" + retCode +
                                           ": unexpected value.");
            }
        }

        System.out.println("Executed " + count + " loops in " + timeMax +
                           " seconds.");

        String cmd = System.getProperty("sun.java.command");
        if (cmd != null && !cmd.startsWith("com.sun.javatest.regtest.agent.MainWrapper")) {
            
            System.exit(0);
        }
    }

    public static void usage() {
        System.err.println("Usage: " + AGENT_LIB + " [time_max]");
        System.err.println("where:");
        System.err.println("    time_max ::= max looping time in seconds");
        System.err.println("                 (default is " + DEF_TIME_MAX +
                           " seconds)");
        System.exit(1);
    }
}
