

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;



public class RacingThreadsTest {
    
    public final String TEST_NAME;
    
    public final int N_LOOPS;
    
    public final int N_SECS;
    
    public final int N_THREADS;

    
    RacingThreadsTest(String name, int n_threads, int n_loops, int n_secs) {
        TEST_NAME = name;
        N_THREADS = n_threads;
        N_LOOPS = n_loops;
        N_SECS = n_secs;

        finishBarrier = new CyclicBarrier(N_THREADS + 1);
        resetBarrier = new CyclicBarrier(N_THREADS + 1);
        startBarrier = new CyclicBarrier(N_THREADS + 1);
    }


    
    public static void main(String[] args) {
        
        
        
        
        
        
        RacingThreadsTest test = new RacingThreadsTest("dummy", 2, 3, 2);
        DriverThread driver = new DriverThread(test);
        WorkerThread[] workers = new WorkerThread[2];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new WorkerThread(i, test);
        }
        test.runTest(driver, workers);
    }

    private static volatile boolean done = false;  

    
    private static final AtomicInteger failCnt = new AtomicInteger();
    
    
    private static final AtomicInteger loopCnt = new AtomicInteger();
    private static boolean verbose
        = Boolean.getBoolean("RacingThreadsTest.verbose");

    
    private final CyclicBarrier finishBarrier;
    private final CyclicBarrier resetBarrier;
    private final CyclicBarrier startBarrier;


    
    public boolean getDone() {
        return done;
    }

    
    public void setDone(boolean v) {
        done = v;
    }

    
    public int getFailCnt() {
        return failCnt.get();
    }

    
    public int incAndGetFailCnt() {
        return failCnt.incrementAndGet();
    }

    
    public int getLoopCnt() {
        return loopCnt.get();
    }

    
    public int incAndGetLoopCnt() {
        return loopCnt.incrementAndGet();
    }

    
    public boolean getVerbose() {
        return verbose;
    }

    
    public void setVerbose(boolean v) {
        verbose = v;
    }

    
    public void runTest(DriverThread driver, WorkerThread[] workers) {
        driver.run(workers);

        try {
            driver.join();
        } catch (InterruptedException ie) {
            unexpectedException(Thread.currentThread(), ie);
            
        }

        if (failCnt.get() == 0) {
            System.out.println(TEST_NAME + ": Test PASSed.");
        } else {
            System.out.println(TEST_NAME + ": failCnt=" + failCnt.get());
            System.out.println(TEST_NAME + ": Test FAILed.");
            throw new RuntimeException("Test Failed");
        }
    }

    
    public void unexpectedException(Thread t, Exception e) {
        System.err.println(t.getName() + ": ERROR: unexpected exception: " + e);
        incAndGetFailCnt();  
    }


    
    
    

    
    public void oneTimeDriverInit(DriverThread dt) {
        if (verbose)
            System.out.println(dt.getName() + ": oneTimeDriverInit() called");
    }

    
    public void oneTimeWorkerInit(WorkerThread wt) {
        if (verbose)
            System.out.println(wt.getName() + ": oneTimeWorkerInit() called");
    }

    
    public void perRaceDriverInit(DriverThread dt) {
        if (verbose)
            System.out.println(dt.getName() + ": perRaceDriverInit() called");
    }

    
    public void perRaceWorkerInit(WorkerThread wt) {
        if (verbose)
            System.out.println(wt.getName() + ": perRaceWorkerInit() called");
    }

    
    public void executeRace(WorkerThread wt) {
        if (verbose)
            System.out.println(wt.getName() + ": executeRace() called");
    }

    
    public void checkRaceResults(DriverThread dt) {
        if (verbose)
            System.out.println(dt.getName() + ": checkRaceResults() called");
    }

    
    public void perRaceDriverEpilog(DriverThread dt) {
        if (verbose)
            System.out.println(dt.getName() + ": perRaceDriverEpilog() called");
    }

    
    public void perRaceWorkerEpilog(WorkerThread wt) {
        if (verbose)
            System.out.println(wt.getName() + ": perRaceWorkerEpilog() called");
    }

    
    public void oneTimeWorkerEpilog(WorkerThread wt) {
        if (verbose)
            System.out.println(wt.getName() + ": oneTimeWorkerEpilog() called");
    }

    
    public void oneTimeDriverEpilog(DriverThread dt) {
        if (verbose)
            System.out.println(dt.getName() + ": oneTimeDriverEpilog() called");
    }


    
    public static class DriverThread extends Thread {
        private final RacingThreadsTest test;

        
        DriverThread(RacingThreadsTest test) {
            super("DriverThread");
            this.test = test;
        }

        private void run(WorkerThread[] workers) {
            System.out.println(getName() + ": is starting.");
            System.out.println(getName() + ": # WorkerThreads: " + test.N_THREADS);
            System.out.println(getName() + ": max # loops: " + test.N_LOOPS);
            System.out.println(getName() + ": max # secs: " + test.N_SECS);

            
            test.oneTimeDriverInit(this);

            
            for (int i = 0; i < workers.length; i++) {
                workers[i].start();
            }

            
            

            long endTime = System.currentTimeMillis() + test.N_SECS * 1000;

            for (; !test.getDone() && test.getLoopCnt() < test.N_LOOPS;
                test.incAndGetLoopCnt()) {

                if (test.getVerbose() && (test.N_LOOPS < 10 ||
                    (test.getLoopCnt() % (test.N_LOOPS / 10)) == 0)) {
                    System.out.println(getName() + ": race loop #"
                        + test.getLoopCnt());
                }

                
                test.perRaceDriverInit(this);

                try {
                    
                    
                    test.startBarrier.await();
                } catch (BrokenBarrierException bbe) {
                    test.unexpectedException(this, bbe);
                    return;
                } catch (InterruptedException ie) {
                    test.unexpectedException(this, ie);
                    return;
                }

                
                

                
                try {
                    test.finishBarrier.await();
                } catch (BrokenBarrierException bbe) {
                    test.unexpectedException(this, bbe);
                    return;
                } catch (InterruptedException ie) {
                    test.unexpectedException(this, ie);
                    return;
                }
                
                
                

                test.checkRaceResults(this);

                if (test.getLoopCnt() + 1 >= test.N_LOOPS ||
                    System.currentTimeMillis() >= endTime) {
                    
                    
                    
                    test.setDone(true);
                }

                
                try {
                    test.resetBarrier.await();
                } catch (BrokenBarrierException bbe) {
                    test.unexpectedException(this, bbe);
                    return;
                } catch (InterruptedException ie) {
                    test.unexpectedException(this, ie);
                    return;
                }

                
                
                
                
                
                

                
                test.perRaceDriverEpilog(this);
            }

            System.out.println(getName() + ": completed " + test.getLoopCnt()
                + " race loops.");
            if (test.getLoopCnt() < test.N_LOOPS) {
                System.out.println(getName() + ": race stopped @ " + test.N_SECS
                    + " seconds.");
            }

            for (int i = 0; i < workers.length; i++) {
                try {
                    workers[i].join();
                } catch (InterruptedException ie) {
                    test.unexpectedException(this, ie);
                    return;
                }
            }

            
            test.oneTimeDriverEpilog(this);

            System.out.println(getName() + ": is done.");
        }
    }


    
    public static class WorkerThread extends Thread {
        private final RacingThreadsTest test;
        private final int workerNum;

        
        WorkerThread(int workerNum, RacingThreadsTest test) {
            super("WorkerThread-" + workerNum);
            this.test = test;
            this.workerNum = workerNum;
        }

        
        public int getWorkerNum() {
            return workerNum;
        }

        
        public void run() {
            System.out.println(getName() + ": is running.");

            
            test.oneTimeWorkerInit(this);

            while (!test.getDone()) {
                
                test.perRaceWorkerInit(this);

                try {
                    test.startBarrier.await();  
                } catch (BrokenBarrierException bbe) {
                    test.unexpectedException(this, bbe);
                    return;
                } catch (InterruptedException ie) {
                    test.unexpectedException(this, ie);
                    return;
                }

                
                test.executeRace(this);

                try {
                    test.finishBarrier.await();  
                } catch (BrokenBarrierException bbe) {
                    test.unexpectedException(this, bbe);
                    return;
                } catch (InterruptedException ie) {
                    test.unexpectedException(this, ie);
                    return;
                }

                try {
                    test.resetBarrier.await();  
                } catch (BrokenBarrierException bbe) {
                    test.unexpectedException(this, bbe);
                    return;
                } catch (InterruptedException ie) {
                    test.unexpectedException(this, ie);
                    return;
                }

               
                test.perRaceWorkerEpilog(this);
            }

            
            test.oneTimeWorkerEpilog(this);

            System.out.println(getName() + ": is ending.");
        }
    }
}
