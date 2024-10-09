



import java.util.concurrent.CountDownLatch;

public class IsInterruptedAtExit extends Thread {
    private final static int DEF_TIME_MAX = 30;  
    private final static String PROG_NAME = "IsInterruptedAtExit";

    public CountDownLatch exitSyncObj = new CountDownLatch(1);
    public CountDownLatch startSyncObj = new CountDownLatch(1);

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

            IsInterruptedAtExit thread = new IsInterruptedAtExit();
            thread.start();
            try {
                
                thread.startSyncObj.await();
                
                
                
                thread.exitSyncObj.countDown();
                while (true) {
                    thread.isInterrupted();

                    if (!thread.isAlive()) {
                        
                        
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new Error("Unexpected: " + e);
            }

            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new Error("Unexpected: " + e);
            }
            thread.isInterrupted();
        }

        System.out.println("Executed " + count + " loops in " + timeMax +
                           " seconds.");

        String cmd = System.getProperty("sun.java.command");
        if (cmd != null && !cmd.startsWith("com.sun.javatest.regtest.agent.MainWrapper")) {
            
            System.exit(0);
        }
    }

    public static void usage() {
        System.err.println("Usage: " + PROG_NAME + " [time_max]");
        System.err.println("where:");
        System.err.println("    time_max  max looping time in seconds");
        System.err.println("              (default is " + DEF_TIME_MAX +
                           " seconds)");
        System.exit(1);
    }
}
