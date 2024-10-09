import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class LastElement {

    void test(String[] args) throws Throwable {
        testQueue(new LinkedBlockingQueue<Integer>());
        testQueue(new LinkedBlockingDeque<Integer>());
        testQueue(new ArrayBlockingQueue<Integer>(10, true));
        testQueue(new ArrayBlockingQueue<Integer>(10, false));
        testQueue(new LinkedTransferQueue<Integer>());
        testQueue(new PriorityBlockingQueue<Integer>());
    }

    void testQueue(BlockingQueue<Integer> q) throws Throwable {
        Integer one = 1;
        Integer two = 2;
        Integer three = 3;
        q.put(one);
        q.put(two);
        check(!q.isEmpty() && q.size() == 2);
        check(q.remove(one));
        check(q.remove(two));
        check(q.isEmpty() && q.size() == 0);
        q.put(three);
        try {
            check(q.take() == three);
        } catch (Throwable t) {
            unexpected(t);
        }
        check(q.isEmpty() && q.size() == 0);
        check(noRetention(q));
        q.clear();
        q.put(one);
        check(q.offer(two));
        check(!q.isEmpty() && q.size() == 2);
        Iterator<Integer> i = q.iterator();
        check(i.next() == one);
        i.remove();
        check(i.next() == two);
        i.remove();
        check(q.isEmpty() && q.size() == 0);
        q.put(three);
        try {
            check(q.take() == three);
        } catch (Throwable t) {
            unexpected(t);
        }
        check(q.isEmpty() && q.size() == 0);
    }

    boolean noRetention(BlockingQueue<?> q) {
        if (q instanceof PriorityBlockingQueue) {
            PriorityBlockingQueue<?> pbq = (PriorityBlockingQueue) q;
            try {
                java.lang.reflect.Field queue = PriorityBlockingQueue.class.getDeclaredField("queue");
                queue.setAccessible(true);
                Object[] a = (Object[]) queue.get(pbq);
                return a[0] == null;
            } catch (NoSuchFieldException e) {
                unexpected(e);
            } catch (IllegalAccessException e) {
            }
        }
        return true;
    }

    volatile int passed = 0, failed = 0;

    void pass() {
        passed++;
    }

    void fail() {
        failed++;
        Thread.dumpStack();
    }

    void fail(String msg) {
        System.err.println(msg);
        fail();
    }

    void unexpected(Throwable t) {
        failed++;
        t.printStackTrace();
    }

    void check(boolean cond) {
        if (cond)
            pass();
        else
            fail();
    }

    void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y))
            pass();
        else
            fail(x + " not equal to " + y);
    }

    public static void main(String[] args) throws Throwable {
        new LastElement().instanceMain(args);
    }

    public void instanceMain(String[] args) throws Throwable {
        try {
            test(args);
        } catch (Throwable t) {
            unexpected(t);
        }
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0)
            throw new AssertionError("Some tests failed");
    }
}
