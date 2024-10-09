



import java.util.concurrent.CountDownLatch;

public class SetNameAtExit extends Thread {
    final static int N_THREADS = 32;
    final static int N_LATE_CALLS = 1000;

    public CountDownLatch exitSyncObj = new CountDownLatch(1);
    public CountDownLatch startSyncObj = new CountDownLatch(1);

    @Override
    public void run() {
        
        startSyncObj.countDown();
        try {
            
            
            exitSyncObj.await();
        } catch (InterruptedException e) {
            
        }
    }

    public static void main(String[] args) {
        SetNameAtExit threads[] = new SetNameAtExit[N_THREADS];

        for (int i = 0; i < N_THREADS; i++ ) {
            threads[i] = new SetNameAtExit();
            int late_count = 1;
            threads[i].start();
            try {
                
                threads[i].startSyncObj.await();

                
                
                
                threads[i].interrupt();
                for (; late_count <= N_LATE_CALLS; late_count++) {
                    threads[i].setName("T" + i + "-" + late_count);

                    if (!threads[i].isAlive()) {
                        
                        
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new Error("Unexpected: " + e);
            }

            System.out.println("INFO: thread #" + i + ": made " + late_count +
                               " late calls to java.lang.Thread.setName()");
            System.out.println("INFO: thread #" + i + ": N_LATE_CALLS==" +
                               N_LATE_CALLS + " value is " +
                               ((late_count >= N_LATE_CALLS) ? "NOT " : "") +
                               "large enough to cause a Thread.setName() " +
                               "call after thread exit.");

            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new Error("Unexpected: " + e);
            }
            threads[i].setName("T" + i + "-done");
            if (threads[i].isAlive()) {
                throw new Error("Expected !Thread.isAlive() after thread #" +
                                i + " has been join()'ed");
            }
        }

        String cmd = System.getProperty("sun.java.command");
        if (cmd != null && !cmd.startsWith("com.sun.javatest.regtest.agent.MainWrapper")) {
            
            System.exit(0);
        }
    }
}
