import java.lang.instrument.Instrumentation;

public class LockDuringDumpAgent implements Runnable {

    static boolean threadStarted = false;

    static Object lock = new Object();

    static String LITERAL = "@@LockDuringDump@@LITERAL";

    public static void premain(String agentArg, Instrumentation instrumentation) {
        System.out.println("inside LockDuringDumpAgent: " + LockDuringDumpAgent.class.getClassLoader());
        Thread t = new Thread(new LockDuringDumpAgent());
        t.setDaemon(true);
        t.start();
        waitForThreadStart();
    }

    static void waitForThreadStart() {
        try {
            synchronized (lock) {
                while (!threadStarted) {
                    lock.wait();
                }
                System.out.println("Thread has started");
            }
        } catch (Throwable t) {
            System.err.println("Unexpected: " + t);
            throw new RuntimeException(t);
        }
    }

    public void run() {
        try {
            synchronized (LITERAL) {
                System.out.println("Let's hold the lock on the literal string \"" + LITERAL + "\" +  forever .....");
                synchronized (lock) {
                    threadStarted = true;
                    lock.notifyAll();
                }
                while (true) {
                    Thread.sleep(1);
                }
            }
        } catch (Throwable t) {
            System.err.println("Unexpected: " + t);
            throw new RuntimeException(t);
        }
    }
}
