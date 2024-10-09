





import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class ThrowingRunnable implements Runnable, UncaughtExceptionHandler {

    static final Phaser phaser = new Phaser(2);

    private static void realMain(String[] args) throws Throwable {
        ThrowingRunnable r = new ThrowingRunnable();
        ForkJoinPool.commonPool().execute(r);
        phaser.awaitAdvanceInterruptibly(phaser.arrive(), 10, TimeUnit.SECONDS);
        pass();
    }

    @Override
    public void run() {
        throw new RuntimeException("This is an exception.");
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        pass();
        phaser.arrive();
    }

    
    static volatile int passed = 0, failed = 0;
    static void pass() {passed++;}
    static void fail() {failed++; }
    static void fail(String msg) {System.out.println(msg); fail();}
    static void unexpected(Throwable t) {failed++; t.printStackTrace();}
    static void check(boolean cond, String msg) {if (cond) pass(); else fail(msg);}
    static void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y)) pass();
        else fail(x + " not equal to " + y);}
    public static void main(String[] args) throws Throwable {
        try {realMain(args);} catch (Throwable t) {unexpected(t);}
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0) throw new AssertionError("Some tests failed");}
}
