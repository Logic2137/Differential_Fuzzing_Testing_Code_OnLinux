



public class CancellableThreadTest {
    public static final int THREADPAIRS = Integer.parseInt(System.getProperty("test.threadpairs", "128"));

    public static void main(String args[]) {
        Thread[] threads = new Thread[THREADPAIRS];
        Canceller[] cancellers = new Canceller[THREADPAIRS];

        System.out.println("Running with " + THREADPAIRS + " thread pairs");

        for (int i = 0; i < THREADPAIRS; i++) {
            cancellers[i] = new Canceller(i);
            threads[i] = new Thread(cancellers[i]);
            threads[i].start();
        }

        for (int i = 0; i < THREADPAIRS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
            }

            if (cancellers[i].failed) {
                throw new RuntimeException(" Test failed in " + i + " th pair. See error messages above.");
            }
        }
    }
}

class Canceller implements Runnable {

    private final CancellableTimer timer;

    public final String name;
    public volatile boolean failed = false;
    private volatile boolean hasBeenNotified = false;

    public Canceller(int index) {
        this.name = "Canceller #" + index;
        timer = new CancellableTimer(index, this);
    }

    public void setHasBeenNotified() {
        hasBeenNotified = true;
    }

    
    public void run() {
        Thread timerThread = new Thread(timer);

        try {
            synchronized(this) {
                timerThread.start();
                wait();
            }
        } catch (InterruptedException e) {
            System.err.println(name + " was interrupted during wait()");
            failed = true;
        }

        if (!hasBeenNotified) {
            System.err.println(name + ".hasBeenNotified is not true as expected");
            failed = true;
        }

        synchronized (timer) {
            timerThread.interrupt();
        }

        try {
            timerThread.join();
        } catch (InterruptedException ie) {
            System.err.println(name + " was interrupted while joining " +
                    timer.name);
            failed = true;
        }

        if (timerThread.isAlive()) {
            System.err.println(timer.name + " is still alive after " + name +
                    " attempted to join it.");
            failed = true;
        }
    }
}



class CancellableTimer implements Runnable {

    public final String name;
    private final Canceller myCanceller;

    public CancellableTimer(int index, Canceller aCanceller) {
        this.name = "CancellableTimer #" + index;
        this.myCanceller = aCanceller;
    }

    
    public void run() {
        try {
            synchronized (this) {
                synchronized (myCanceller) {
                    myCanceller.setHasBeenNotified();
                    myCanceller.notify();
                }
                wait();
            }
        } catch (InterruptedException first) {
            
            
            if (Thread.currentThread().isInterrupted()) {
                System.err.println(name + " should not register an interrupt here");
                myCanceller.failed = true;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.err.println(name + " was interrupted when sleeping");
                myCanceller.failed = true;
            }
        }
    }
}
