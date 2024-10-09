
package compiler.runtime;

public class Test6863420 {

    static final int INTERNAL_TIMEOUT = 240;

    static class TOT extends Thread {

        public void run() {
            try {
                Thread.sleep(INTERNAL_TIMEOUT * 1000);
            } catch (InterruptedException ex) {
            }
            done = true;
        }
    }

    static long value = 0;

    static boolean got_backward_time = false;

    static volatile boolean done = false;

    public static void main(String[] args) {
        final int count = 100000;
        TOT tot = new TOT();
        tot.setDaemon(true);
        tot.start();
        for (int numThreads = 1; !done && numThreads <= 32; numThreads++) {
            final int numRuns = 1;
            for (int t = 1; t <= numRuns; t++) {
                final int curRun = t;
                System.out.println("Spawning " + numThreads + " threads");
                final Thread[] threads = new Thread[numThreads];
                for (int i = 0; i < threads.length; i++) {
                    Runnable thread = new Runnable() {

                        public void run() {
                            for (long l = 0; !done && l < 100000; l++) {
                                final long start = System.nanoTime();
                                if (value == 12345678) {
                                    System.out.println("Wow!");
                                }
                                final long end = System.nanoTime();
                                final long time = end - start;
                                value += time;
                                if (time < 0) {
                                    System.out.println("Backwards: " + "start=" + start + " " + "end=" + end + " " + "time= " + time);
                                    got_backward_time = true;
                                }
                            }
                        }
                    };
                    threads[i] = new Thread(thread, "Thread" + i);
                }
                for (int i = 0; i < threads.length; i++) {
                    threads[i].start();
                }
                for (int i = 0; i < threads.length; i++) {
                    try {
                        threads[i].join();
                    } catch (InterruptedException e) {
                        continue;
                    }
                }
            }
        }
        if (got_backward_time) {
            System.exit(97);
        }
    }
}
