



import java.io.PrintStream;

























public class SuspendWithObjectMonitorWait {
    private static final String AGENT_LIB = "SuspendWithObjectMonitorWait";
    private static final int exit_delta   = 95;

    private static final int DEF_TIME_MAX = 60;    
    private static final int JOIN_MAX     = 30;    

    public static final int TS_INIT            = 1;  
    public static final int TS_WAITER_RUNNING  = 2;  
    public static final int TS_RESUMER_RUNNING = 3;  
    public static final int TS_READY_TO_NOTIFY = 4;  
    public static final int TS_CALL_SUSPEND    = 5;  
    public static final int TS_READY_TO_RESUME = 6;  
    public static final int TS_CALL_RESUME     = 7;  
    public static final int TS_WAITER_DONE     = 8;  

    public static Object barrierLaunch = new Object();   
    public static Object barrierResumer = new Object();  
    public static Object threadLock = new Object();      

    public static long count = 0;
    public static boolean printDebug = false;
    public volatile static int testState;

    private static void log(String msg) { System.out.println(msg); }

    native static int suspendThread(SuspendWithObjectMonitorWaitWorker thr);
    native static int wait4ContendedEnter(SuspendWithObjectMonitorWaitWorker thr);

    public static void main(String[] args) throws Exception {
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
            int argIndex = 0;
            int argsLeft = args.length;
            if (args[0].equals("-p")) {
                printDebug = true;
                argIndex = 1;
                argsLeft--;
            }
            if (argsLeft == 0) {
                timeMax = DEF_TIME_MAX;
            } else if (argsLeft == 1) {
                try {
                    timeMax = Integer.parseUnsignedInt(args[argIndex]);
                } catch (NumberFormatException nfe) {
                    System.err.println("'" + args[argIndex] +
                                       "': invalid timeMax value.");
                    usage();
                }
            } else {
                usage();
            }
        }

        System.exit(run(timeMax, System.out) + exit_delta);
    }

    public static void logDebug(String mesg) {
        if (printDebug) {
            System.err.println(Thread.currentThread().getName() + ": " + mesg);
        }
    }

    public static void usage() {
        System.err.println("Usage: " + AGENT_LIB + " [-p][time_max]");
        System.err.println("where:");
        System.err.println("    -p       ::= print debug info");
        System.err.println("    time_max ::= max looping time in seconds");
        System.err.println("                 (default is " + DEF_TIME_MAX +
                           " seconds)");
        System.exit(1);
    }

    public static int run(int timeMax, PrintStream out) {
        return (new SuspendWithObjectMonitorWait()).doWork(timeMax, out);
    }

    public static void checkTestState(int exp) {
        if (testState != exp) {
            System.err.println("Failure at " + count + " loops.");
            throw new InternalError("Unexpected test state value: "
                + "expected=" + exp + " actual=" + testState);
        }
    }

    public int doWork(int timeMax, PrintStream out) {
        SuspendWithObjectMonitorWaitWorker waiter;    
        SuspendWithObjectMonitorWaitWorker resumer;    

        System.out.println("About to execute for " + timeMax + " seconds.");

        long start_time = System.currentTimeMillis();
        while (System.currentTimeMillis() < start_time + (timeMax * 1000)) {
            count++;
            testState = TS_INIT;  

            
            synchronized (barrierLaunch) {
                waiter = new SuspendWithObjectMonitorWaitWorker("waiter");
                waiter.start();

                while (testState != TS_WAITER_RUNNING) {
                    try {
                        barrierLaunch.wait(0);  
                    } catch (InterruptedException ex) {
                    }
                }
            }

            
            synchronized (barrierLaunch) {
                resumer = new SuspendWithObjectMonitorWaitWorker("resumer", waiter);
                resumer.start();

                while (testState != TS_RESUMER_RUNNING) {
                    try {
                        barrierLaunch.wait(0);  
                    } catch (InterruptedException ex) {
                    }
                }
            }

            checkTestState(TS_RESUMER_RUNNING);

            
            
            
            
            synchronized (threadLock) {
                
                testState = TS_READY_TO_NOTIFY;
                threadLock.notify();

                
                logDebug("before contended enter wait");
                int retCode = wait4ContendedEnter(waiter);
                if (retCode != 0) {
                    throw new RuntimeException("error in JVMTI GetThreadState: "
                                               + "retCode=" + retCode);
                }
                logDebug("done contended enter wait");

                checkTestState(TS_READY_TO_NOTIFY);
                testState = TS_CALL_SUSPEND;
                logDebug("before suspend thread");
                retCode = suspendThread(waiter);
                if (retCode != 0) {
                    throw new RuntimeException("error in JVMTI SuspendThread: "
                                               + "retCode=" + retCode);
                }
                logDebug("suspended thread");
            }

            
            
            
            
            
            
            
            
            

            synchronized (barrierResumer) {
                checkTestState(TS_CALL_SUSPEND);

                
                testState = TS_READY_TO_RESUME;
                barrierResumer.notify();

                
                
                
            }

            try {
                resumer.join(JOIN_MAX * 1000);
                if (resumer.isAlive()) {
                    System.err.println("Failure at " + count + " loops.");
                    throw new InternalError("resumer thread is stuck");
                }
                waiter.join(JOIN_MAX * 1000);
                if (waiter.isAlive()) {
                    System.err.println("Failure at " + count + " loops.");
                    throw new InternalError("waiter thread is stuck");
                }
            } catch (InterruptedException ex) {
            }

            checkTestState(TS_WAITER_DONE);
        }

        System.out.println("Executed " + count + " loops in " + timeMax +
                           " seconds.");

        return 0;
    }
}

class SuspendWithObjectMonitorWaitWorker extends Thread {
    private SuspendWithObjectMonitorWaitWorker target;  

    public SuspendWithObjectMonitorWaitWorker(String name) {
        super(name);
    }

    public SuspendWithObjectMonitorWaitWorker(String name, SuspendWithObjectMonitorWaitWorker target) {
        super(name);
        this.target = target;
    }

    native static int resumeThread(SuspendWithObjectMonitorWaitWorker thr);

    public void run() {
        SuspendWithObjectMonitorWait.logDebug("thread running");

        
        
        
        
        
        
        if (getName().equals("waiter")) {
            
            SuspendWithObjectMonitorWait.logDebug("before enter threadLock");
            synchronized(SuspendWithObjectMonitorWait.threadLock) {
                SuspendWithObjectMonitorWait.logDebug("enter threadLock");

                SuspendWithObjectMonitorWait.checkTestState(SuspendWithObjectMonitorWait.TS_INIT);

                synchronized(SuspendWithObjectMonitorWait.barrierLaunch) {
                    
                    SuspendWithObjectMonitorWait.testState = SuspendWithObjectMonitorWait.TS_WAITER_RUNNING;
                    SuspendWithObjectMonitorWait.barrierLaunch.notify();
                }

                SuspendWithObjectMonitorWait.logDebug("before wait");

                
                
                
                while (SuspendWithObjectMonitorWait.testState <= SuspendWithObjectMonitorWait.TS_READY_TO_NOTIFY) {
                    try {
                        SuspendWithObjectMonitorWait.threadLock.wait(0);
                    } catch (InterruptedException ex) {
                    }
                }

                SuspendWithObjectMonitorWait.logDebug("after wait");

                SuspendWithObjectMonitorWait.checkTestState(SuspendWithObjectMonitorWait.TS_CALL_RESUME);
                SuspendWithObjectMonitorWait.testState = SuspendWithObjectMonitorWait.TS_WAITER_DONE;

                SuspendWithObjectMonitorWait.logDebug("exit threadLock");
            }
        }
        
        
        
        
        
        
        
        else if (getName().equals("resumer")) {
            synchronized(SuspendWithObjectMonitorWait.barrierResumer) {
                synchronized(SuspendWithObjectMonitorWait.barrierLaunch) {
                    
                    SuspendWithObjectMonitorWait.testState = SuspendWithObjectMonitorWait.TS_RESUMER_RUNNING;
                    SuspendWithObjectMonitorWait.barrierLaunch.notify();
                }
                SuspendWithObjectMonitorWait.logDebug("thread waiting");
                while (SuspendWithObjectMonitorWait.testState != SuspendWithObjectMonitorWait.TS_READY_TO_RESUME) {
                    try {
                        
                        SuspendWithObjectMonitorWait.barrierResumer.wait(0);
                    } catch (InterruptedException ex) {
                    }
                }
            }

            SuspendWithObjectMonitorWait.logDebug("before enter threadLock");
            synchronized(SuspendWithObjectMonitorWait.threadLock) {
                SuspendWithObjectMonitorWait.logDebug("enter threadLock");

                SuspendWithObjectMonitorWait.checkTestState(SuspendWithObjectMonitorWait.TS_READY_TO_RESUME);
                SuspendWithObjectMonitorWait.testState = SuspendWithObjectMonitorWait.TS_CALL_RESUME;

                
                SuspendWithObjectMonitorWait.logDebug("before resume thread");
                int retCode = resumeThread(target);
                if (retCode != 0) {
                    throw new RuntimeException("error in JVMTI ResumeThread: " +
                                               "retCode=" + retCode);
                }
                SuspendWithObjectMonitorWait.logDebug("resumed thread");

                SuspendWithObjectMonitorWait.logDebug("exit threadLock");
            }
        }
    }
}
