
package nsk.stress.thread;

import java.io.PrintStream;

public class thread005 extends Thread {

    private static boolean DEBUG_MODE = false;

    private static int THREADS_EXPECTED = 1000;

    private static long TIMEOUT = 300000;

    private static long YIELD_TIME = 5000;

    private static long parseTime(String arg) {
        for (int i = arg.lastIndexOf("ms"); i > -1; ) return Long.parseLong(arg.substring(0, i));
        for (int i = arg.lastIndexOf("s"); i > -1; ) return Long.parseLong(arg.substring(0, i)) * 1000;
        for (int i = arg.lastIndexOf("m"); i > -1; ) return Long.parseLong(arg.substring(0, i)) * 60000;
        throw new IllegalArgumentException("cannot recognize time scale: " + arg);
    }

    public static void main(String[] args) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
    }

    public static int run(String[] args, PrintStream out) {
        if (args.length > 0)
            THREADS_EXPECTED = Integer.parseInt(args[0]);
        if (args.length > 1)
            TIMEOUT = parseTime(args[1]);
        if (args.length > 2)
            YIELD_TIME = parseTime(args[2]);
        if (args.length > 3)
            DEBUG_MODE = args[3].toLowerCase().startsWith("-v");
        if (args.length > 4) {
            out.println("#");
            out.println("# Too namy command-line arguments!");
            out.println("#");
            return 2;
        }
        if (DEBUG_MODE) {
            out.println("Start " + THREADS_EXPECTED + " threads, " + "halt after " + TIMEOUT + " milliseconds:");
        }
        Thread[] thread = new Thread[THREADS_EXPECTED];
        for (int i = 0; i < THREADS_EXPECTED; i++) try {
            thread[i] = new thread005();
            thread[i].start();
            if (DEBUG_MODE)
                out.println("Threads started: " + (i + 1));
        } catch (OutOfMemoryError oome) {
            oome.printStackTrace(out);
            out.println("#");
            out.println("# The test have FAILED:");
            out.println("# Only " + i + " threads could start,");
            out.println("# while at least " + THREADS_EXPECTED + " were expected.");
            out.println("#");
            return 2;
        }
        GO = true;
        try {
            doSleep(YIELD_TIME);
        } catch (InterruptedException ie) {
            ie.printStackTrace(out);
            out.println("#");
            out.println("# OOPS! Could not let threads actually start!");
            out.println("#");
            return 2;
        }
        STOP = true;
        if (DEBUG_MODE)
            out.println("The test have PASSED.");
        return 0;
    }

    private static boolean GO = false;

    private static boolean STOP = false;

    public void run() {
        while (!GO && !timeout()) Thread.yield();
        while (!STOP && !timeout()) ;
    }

    private static long startTime = System.currentTimeMillis();

    private boolean timeout() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        return elapsedTime > TIMEOUT;
    }

    private static void doSleep(long time) throws InterruptedException {
        Object lock = new Object();
        synchronized (lock) {
            lock.wait(time);
        }
    }
}
