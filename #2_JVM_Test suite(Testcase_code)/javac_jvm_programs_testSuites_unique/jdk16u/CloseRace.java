



import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class CloseRace {
    private static final String BIG_FILE = "bigfile";

    private static final int[] procFDs = new int[6];

    
    private static final int testDurationSeconds
        = Integer.getInteger("test.duration", 600);

    private static final CountDownLatch threadsStarted
        = new CountDownLatch(2);

    static boolean fdInUse(int i) {
        return new File("/proc/self/fd/" + i).exists();
    }

    static boolean[] procFDsInUse() {
        boolean[] inUse = new boolean[procFDs.length];
        for (int i = 0; i < procFDs.length; i++)
            inUse[i] = fdInUse(procFDs[i]);
        return inUse;
    }

    static int count(boolean[] bits) {
        int count = 0;
        for (int i = 0; i < bits.length; i++)
            count += bits[i] ? 1 : 0;
        return count;
    }

    static void dumpAllStacks() {
        System.err.println("Start of dump");
        final Map<Thread, StackTraceElement[]> allStackTraces
                = Thread.getAllStackTraces();
        for (Thread thread : allStackTraces.keySet()) {
            System.err.println("Thread " + thread.getName());
            for (StackTraceElement element : allStackTraces.get(thread))
                System.err.println("\t" + element);
        }
        System.err.println("End of dump");
    }

    public static void main(String args[]) throws Exception {
        if (!(new File("/proc/self/fd").isDirectory()))
            return;

        
        Thread.setDefaultUncaughtExceptionHandler
            ((t, e) -> { e.printStackTrace(); System.exit(1); });

        try (RandomAccessFile f = new RandomAccessFile(BIG_FILE, "rw")) {
            f.setLength(Runtime.getRuntime().maxMemory()); 
        }

        for (int i = 0, j = 0; j < procFDs.length; i++)
            if (!fdInUse(i))
                procFDs[j++] = i;

        Thread[] threads = {
            new Thread(new OpenLoop()),
            new Thread(new ExecLoop()),
        };
        for (Thread thread : threads)
            thread.start();

        threadsStarted.await();
        Thread.sleep(testDurationSeconds * 1000);

        for (Thread thread : threads)
            thread.interrupt();
        for (Thread thread : threads) {
            thread.join(10_000);
            if (thread.isAlive()) {
                dumpAllStacks();
                throw new Error("At least one child thread ("
                        + thread.getName()
                        + ") failed to finish gracefully");
            }
        }
    }

    static class OpenLoop implements Runnable {
        public void run() {
            threadsStarted.countDown();
            while (!Thread.interrupted()) {
                try {
                    
                    do {
                        if (Thread.interrupted())
                            return;
                    } while (count(procFDsInUse()) != 3);
                    List<InputStream> iss = new ArrayList<>(4);

                    
                    for (int i = 0; i < 3; i++)
                        iss.add(new FileInputStream(BIG_FILE));
                    do {
                        if (Thread.interrupted())
                            return;
                    } while (count(procFDsInUse()) == procFDs.length);
                    
                    iss.add(new FileInputStream(BIG_FILE));
                    Thread.sleep(1); 
                    for (InputStream is : iss)
                        is.close();
                } catch (InterruptedException e) {
                    break;
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        }
    }

    static class ExecLoop implements Runnable {
        public void run() {
            threadsStarted.countDown();
            ProcessBuilder builder = new ProcessBuilder("/bin/true");
            while (!Thread.interrupted()) {
                try {
                    
                    do {
                        if (Thread.interrupted())
                            return;
                    } while (count(procFDsInUse()) > 0);
                    Process process = builder.start();
                    InputStream is = process.getInputStream();
                    process.waitFor();
                    is.close();
                } catch (InterruptedException e) {
                    break;
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        }
    }
}
