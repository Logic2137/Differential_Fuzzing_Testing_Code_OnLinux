



import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.io.IOException;
import java.io.ObjectInputStream;


public class Mutex implements Lock, java.io.Serializable {
    private static class Sync extends AbstractQueuedSynchronizer {
        public boolean isHeldExclusively() { return getState() == 1; }

        public boolean tryAcquire(int acquires) {
            assert acquires == 1; 
            return compareAndSetState(0, 1);
        }

        public boolean tryRelease(int releases) {
            setState(0);
            return true;
        }

        Condition newCondition() { return new ConditionObject(); }

        private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); 
        }
    }

    private final Sync sync = new Sync();
    public void lock() {
        sync.acquire(1);
    }
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }
    public void unlock() { sync.release(1); }
    public Condition newCondition() { return sync.newCondition(); }
    public boolean isLocked() { return sync.isHeldExclusively(); }
    public boolean hasQueuedThreads() { return sync.hasQueuedThreads(); }
}
