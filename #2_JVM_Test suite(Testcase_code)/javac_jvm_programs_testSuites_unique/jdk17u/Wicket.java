
package nsk.share;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Wicket {

    private int count;

    private int waiters = 0;

    private PrintStream debugOutput = null;

    private String name = "";

    private final Lock lock = new ReentrantLock();

    private final Condition condition = lock.newCondition();

    public Wicket() {
        this(1);
    }

    public Wicket(String _name, int _count, PrintStream _debugOutput) {
        this(_count);
        name = _name;
        debugOutput = _debugOutput;
    }

    public Wicket(int count) {
        if (count < 1)
            throw new IllegalArgumentException("count is less than one: " + count);
        this.count = count;
    }

    public void waitFor() {
        long id = System.currentTimeMillis();
        lock.lock();
        try {
            ++waiters;
            if (debugOutput != null) {
                debugOutput.printf("Wicket %d %s: waitFor(). There are %d waiters totally now.\n", id, name, waiters);
            }
            while (count > 0) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                }
            }
            --waiters;
        } finally {
            lock.unlock();
        }
    }

    public int waitFor(long timeout) {
        if (timeout < 0)
            throw new IllegalArgumentException("timeout value is negative: " + timeout);
        long id = System.currentTimeMillis();
        lock.lock();
        try {
            ++waiters;
            if (debugOutput != null) {
                debugOutput.printf("Wicket %d %s: waitFor(). There are %d waiters totally now.\n", id, name, waiters);
            }
            long waitTime = timeout;
            long startTime = System.currentTimeMillis();
            while (count > 0 && waitTime > 0) {
                try {
                    condition.await(waitTime, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                }
                waitTime = timeout - (System.currentTimeMillis() - startTime);
            }
            --waiters;
            return count;
        } finally {
            lock.unlock();
        }
    }

    public void unlock() {
        lock.lock();
        try {
            if (count == 0)
                throw new IllegalStateException("locks are already open");
            --count;
            if (debugOutput != null) {
                debugOutput.printf("Wicket %s: unlock() the count is now %d\n", name, count);
            }
            if (count == 0) {
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public void unlockAll() {
        if (debugOutput != null) {
            debugOutput.printf("Wicket %s: unlockAll()\n", name);
        }
        lock.lock();
        try {
            count = 0;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public int getWaiters() {
        lock.lock();
        try {
            if (debugOutput != null) {
                debugOutput.printf("Wicket %s: getWaiters()\n", name);
            }
            return waiters;
        } finally {
            lock.unlock();
        }
    }
}
