



import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.Semaphore;
import java.io.*;

public class HandshakeDirectTest  implements Runnable {
    static final int WORKING_THREADS = 32;
    static final int DIRECT_HANDSHAKES_MARK = 50000;
    static Thread[] workingThreads = new Thread[WORKING_THREADS];
    static Semaphore[] handshakeSem = new Semaphore[WORKING_THREADS];
    static Object[] locks = new Object[WORKING_THREADS];
    static boolean[] isBiased = new boolean[WORKING_THREADS];
    static AtomicInteger handshakeCount = new AtomicInteger(0);

    @Override
    public void run() {
        int me = Integer.parseInt(Thread.currentThread().getName());

        while (true) {
            try {
                if (!isBiased[me]) {
                    handshakeSem[me].acquire();
                    synchronized(locks[me]) {
                        isBiased[me] = true;
                    }
                    handshakeSem[me].release();
                }

                
                int handshakee = ThreadLocalRandom.current().nextInt(0, WORKING_THREADS - 1);
                if (handshakee == me) {
                    
                    handshakee = handshakee != 0 ? handshakee - 1 : handshakee + 1;
                }
                handshakeSem[handshakee].acquire();
                if (isBiased[handshakee]) {
                    
                    synchronized(locks[handshakee]) {
                        handshakeCount.incrementAndGet();
                    }
                    
                    locks[handshakee] = new Object();
                    isBiased[handshakee] = false;
                }
                handshakeSem[handshakee].release();
                if (handshakeCount.get() >= DIRECT_HANDSHAKES_MARK) {
                    break;
                }
            } catch(InterruptedException ie) {
                throw new Error("Unexpected interrupt");
            }
        }
    }

    public static void main(String... args) throws Exception {
        HandshakeDirectTest test = new HandshakeDirectTest();

        
        for (int i = 0; i < WORKING_THREADS; i++) {
            handshakeSem[i] = new Semaphore(1);
        }

        
        for (int i = 0; i < WORKING_THREADS; i++) {
            locks[i] = new Object();
        }

        
        for (int i = 0; i < WORKING_THREADS; i++) {
            workingThreads[i] = new Thread(test, Integer.toString(i));
            workingThreads[i].start();
        }

        
        Thread suspendResumeThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    int i = ThreadLocalRandom.current().nextInt(0, WORKING_THREADS - 1);
                    workingThreads[i].suspend();
                    try {
                        Thread.sleep(1); 
                    } catch(InterruptedException ie) {
                    }
                    workingThreads[i].resume();
                }
            }
        };
        suspendResumeThread.setDaemon(true);
        suspendResumeThread.start();

        
        
        for (int i = 0; i < WORKING_THREADS; i++) {
            workingThreads[i].join();
        }
    }
}
