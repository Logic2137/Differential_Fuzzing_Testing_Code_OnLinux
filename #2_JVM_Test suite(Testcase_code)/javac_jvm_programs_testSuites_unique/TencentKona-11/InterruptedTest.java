



package compiler.c2;

public class InterruptedTest {

    public static void main(String[] args) throws Exception {
        
        int threshold = 100;

        if (args.length != 1) {
            System.out.println("Incorrect number of arguments");
            System.exit(1);
        }

        try {
            threshold = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid argument format");
            System.exit(1);
        }

        if (threshold < 1) {
            System.out.println("Threshold must be at least 1");
            System.exit(1);
        }

        Thread workerThread = new Thread("worker") {
            public void run() {
                System.out.println("Worker thread: running...");
                while (!Thread.currentThread().isInterrupted()) {
                }
                System.out.println("Worker thread: bye");
            }
        };
        System.out.println("Main thread: starts a worker thread...");
        workerThread.start();
        System.out.println("Main thread: waits 5 seconds after starting the worker thread");
        workerThread.join(5000); 

        int ntries = 0;
        while (workerThread.isAlive() && ntries < threshold) {
            System.out.println("Main thread: interrupts the worker thread...");
            workerThread.interrupt();
            if (workerThread.isInterrupted()) {
                System.out.println("Main thread: worker thread is interrupted");
            }
            ntries++;
            System.out.println("Main thread: waits 1 second for the worker thread to die...");
            workerThread.join(1000); 
        }

        if (ntries == threshold) {
          System.out.println("Main thread: the worker thread did not die after " +
                             ntries + " seconds have elapsed");
          System.exit(97);
        }

        System.out.println("Main thread: bye");
    }
}
