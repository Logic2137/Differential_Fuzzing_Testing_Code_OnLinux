import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class PollUnexpired {

    private static class Godot implements Delayed {

        public long getDelay(TimeUnit unit) {
            return Long.MAX_VALUE;
        }

        public int compareTo(Delayed other) {
            return 0;
        }
    }

    private static void realMain(String[] args) throws Throwable {
        DelayQueue<Godot> q = new DelayQueue<>();
        for (int i = 0; i < 3; i++) {
            equal(q.size(), i);
            equal(q.poll(), null);
            equal(q.size(), i);
            equal(q.poll(100, TimeUnit.MILLISECONDS), null);
            equal(q.size(), i);
            q.add(new Godot());
        }
    }

    static volatile int passed = 0, failed = 0;

    static void pass() {
        passed++;
    }

    static void fail() {
        failed++;
        Thread.dumpStack();
    }

    static void fail(String msg) {
        System.out.println(msg);
        fail();
    }

    static void unexpected(Throwable t) {
        failed++;
        t.printStackTrace();
    }

    static void check(boolean cond) {
        if (cond)
            pass();
        else
            fail();
    }

    static void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y))
            pass();
        else
            fail(x + " not equal to " + y);
    }

    public static void main(String[] args) throws Throwable {
        try {
            realMain(args);
        } catch (Throwable t) {
            unexpected(t);
        }
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0)
            throw new AssertionError("Some tests failed");
    }
}
