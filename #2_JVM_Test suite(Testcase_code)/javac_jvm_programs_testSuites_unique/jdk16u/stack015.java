



package nsk.stress.stack;


import java.io.PrintStream;

public class stack015 extends stack015i {
    final static int THREADS = 10;
    final static int CYCLES = 10;
    final static int STEP = 10;
    final static int RESERVE = 10;

    public static void main(String[] args) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
    }

    public static int run(String args[], PrintStream out) {
        
        
        
        
        stack015i test = new stack015();
        stack015i.test = test;

        
        
        
        int maxDepth = 0;
        for (int depth = 0; ; depth += STEP)
            try {
                test.recurse(depth);
                maxDepth = depth;
            } catch (StackOverflowError soe) {
                break;
            } catch (OutOfMemoryError oome) {
                break;
            }
        out.println("Max. depth: " + maxDepth);

        
        
        
        stack015i threads[] = new stack015i[THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new stack015();
            threads[i].depthToTry = RESERVE * maxDepth;
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++)
            if (threads[i].isAlive())
                try {
                    threads[i].join();
                } catch (InterruptedException exception) {
                    exception.printStackTrace(out);
                    return 2;
                }

        
        
        
        int exitCode = 0;
        for (int i = 0; i < threads.length; i++)
            if (threads[i].thrown != null) {
                threads[i].thrown.printStackTrace(out);
                exitCode = 2;
            }

        if (exitCode != 0)
            out.println("# TEST FAILED");
        return exitCode;
    }

    synchronized void syncRecurse(int depth) {
        if (depth > 0)
            syncRecurse(depth - 1);
    }
}

abstract class stack015i extends Thread {
    
    
    
    abstract void syncRecurse(int depth);

    void recurse(int depth) {
        
        
        
        syncRecurse(stack015.STEP);
        
        
        
        if (depth > 0)
            recurse(depth - 1);
    }

    Throwable thrown = null;
    int depthToTry;

    static stack015i test;

    public void run() {
        
        
        
        for (int i = 0; i < stack015.CYCLES; i++)
            try {
                
                
                
                test.recurse(depthToTry);

                throw new Exception(
                        "TEST_RFE: no stack overflow thrown" +
                                ", need to try deeper recursion?");

            } catch (StackOverflowError error) {
                
            } catch (OutOfMemoryError oome) {
                

            } catch (Throwable throwable) {
                if (throwable instanceof ThreadDeath)
                    throw (ThreadDeath) throwable;
                
                thrown = throwable;
                break;
            }
    }
}
