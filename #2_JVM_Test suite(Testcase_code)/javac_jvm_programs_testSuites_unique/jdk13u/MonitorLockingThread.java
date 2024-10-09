
package nsk.share.locks;

public class MonitorLockingThread extends Thread {

    public static class LockFreeThread extends Thread {

        private Thread blockedThread;

        private MonitorLockingThread lockingThread;

        public LockFreeThread(Thread blockedThread, MonitorLockingThread lockingThread) {
            this.blockedThread = blockedThread;
            this.lockingThread = lockingThread;
        }

        public void run() {
            while (blockedThread.getState() != Thread.State.BLOCKED) yield();
            lockingThread.releaseLock();
        }
    }

    private volatile boolean isRunning = true;

    private volatile boolean holdsLock;

    private Object lockToHold;

    public MonitorLockingThread(Object lockToHold) {
        this.lockToHold = lockToHold;
    }

    public void run() {
        synchronized (lockToHold) {
            holdsLock = true;
            while (isRunning) yield();
        }
        holdsLock = false;
    }

    public void releaseLock() {
        isRunning = false;
        while (holdsLock) yield();
    }

    public void acquireLock() {
        start();
        while (!holdsLock) yield();
    }
}
