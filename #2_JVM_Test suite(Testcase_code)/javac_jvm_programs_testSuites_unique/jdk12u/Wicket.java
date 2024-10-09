

package nsk.share;

import java.io.PrintStream;



public class Wicket {

    
    private int count;

    
    private int waiters = 0;

    
    private PrintStream debugOutput = null;

    
    private String name = "";

    
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
            throw new IllegalArgumentException(
                "count is less than one: " + count);
        this.count = count;
    }

    
    public synchronized void waitFor() {
        ++waiters;
        if (debugOutput != null) {
            debugOutput.printf("Wicket %s: waitFor()\n", name);
        }

        while (count > 0) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        --waiters;
    }

    
    public synchronized int waitFor(long timeout) {
        if (debugOutput != null) {
            debugOutput.printf("Wicket %s: waitFor(%d)\n", name, timeout);
        }

        if (timeout < 0)
            throw new IllegalArgumentException(
                "timeout value is negative: " + timeout);
        ++waiters;
        long waitTime = timeout;
        long startTime = System.currentTimeMillis();
        while (count > 0 && waitTime > 0) {
            try {
                wait(waitTime);
            } catch (InterruptedException e) {}
            waitTime = timeout - (System.currentTimeMillis() - startTime);
        }
        --waiters;
        return (count);
    }

    
    public synchronized void unlock() {
        if (debugOutput != null) {
            debugOutput.printf("Wicket %s: unlock()\n", name);
        }

        if (count == 0)
            throw new IllegalStateException("locks are already open");

        --count;
        if (count == 0) {
            notifyAll();
        }
    }

    
    public synchronized void unlockAll() {
        if (debugOutput != null) {
            debugOutput.printf("Wicket %s: unlockAll()\n", name);
        }

        count = 0;
        notifyAll();
    }

    
    public synchronized int getWaiters() {
        if (debugOutput != null) {
            debugOutput.printf("Wicket %s: getWaiters()\n", name);
        }
        return waiters;
    }
}
