



package nsk.stress.stack;


import java.io.PrintStream;

public class stack012 extends Thread {
    final static int THREADS = 10;
    final static int CYCLES = 10;

    public static void main(String[] args) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
    }

    public static int run(String args[], PrintStream out) {
        stack012 test = new stack012();
        
        
        
        int maxDepth = 0;
        for (int depth = 10; ; depth += 10)
            try {
                test.recurse(depth);
                maxDepth = depth;
            } catch (StackOverflowError soe) {
                break;
            } catch (OutOfMemoryError oome) {
                break;
            }
        out.println("Max. depth: " + maxDepth);

        
        
        
        stack012 threads[] = new stack012[THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new stack012();
            threads[i].depthToTry = 10 * maxDepth;
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

    int depthToTry = 0;
    Throwable thrown = null;

    public void run() {
        for (int i = 0; i < CYCLES; i++)
            try {
                this.recurse(depthToTry);
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

    final void recurse(int depth) {
        if (depth > 0)
            recurse(depth - 1);
    }
}
