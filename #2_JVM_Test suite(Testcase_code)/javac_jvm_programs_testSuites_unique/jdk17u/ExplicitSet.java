import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ExplicitSet {

    static void realMain(String[] args) throws Throwable {
        for (int i = 1; i <= 10000; i++) {
            test();
        }
    }

    static void test() throws Throwable {
        final SettableTask task = new SettableTask();
        Thread thread = new Thread() {

            public void run() {
                try {
                    check(task.get() != null);
                } catch (Exception e) {
                    unexpected(e);
                }
            }
        };
        thread.start();
        task.set(Boolean.TRUE);
        thread.join(5000);
    }

    static class SettableTask extends FutureTask<Boolean> {

        SettableTask() {
            super(new Callable<Boolean>() {

                public Boolean call() {
                    fail("The task should never be run!");
                    return null;
                }
            });
        }

        @Override
        public void set(Boolean b) {
            super.set(b);
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
