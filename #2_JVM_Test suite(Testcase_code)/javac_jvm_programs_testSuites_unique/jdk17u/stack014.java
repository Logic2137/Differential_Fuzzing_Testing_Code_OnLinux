
package nsk.stress.stack;

import java.io.PrintStream;

public class stack014 extends stack014i {

    final static int THREADS = 10;

    final static int CYCLES = 10;

    public static void main(String[] args) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
    }

    public static int run(String[] args, PrintStream out) {
        stack014i test = new stack014();
        int maxDepth = 0;
        for (int depth = 10; ; depth += 10) try {
            test.recurse(depth);
            maxDepth = depth;
        } catch (StackOverflowError soe) {
            break;
        } catch (OutOfMemoryError oome) {
            break;
        }
        out.println("Max. depth: " + maxDepth);
        stack014i[] threads = new stack014i[THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new stack014();
            threads[i].depthToTry = 10 * maxDepth;
            threads[i].cycles = CYCLES;
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) if (threads[i].isAlive())
            try {
                threads[i].join();
            } catch (InterruptedException exception) {
                exception.printStackTrace(out);
                return 2;
            }
        int exitCode = 0;
        for (int i = 0; i < threads.length; i++) if (threads[i].thrown != null) {
            threads[i].thrown.printStackTrace(out);
            exitCode = 2;
        }
        if (exitCode != 0)
            out.println("# TEST FAILED");
        return exitCode;
    }

    synchronized void recurse(int depth) {
        if (depth > 0)
            recurse(depth - 1);
    }
}

abstract class stack014i extends Thread {

    abstract void recurse(int depth);

    Throwable thrown = null;

    int depthToTry;

    int cycles;

    public void run() {
        for (int i = 0; i < cycles; i++) try {
            recurse(depthToTry);
            throw new Exception("TEST_RFE: no stack overflow thrown" + ", need to try deeper recursion?");
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
