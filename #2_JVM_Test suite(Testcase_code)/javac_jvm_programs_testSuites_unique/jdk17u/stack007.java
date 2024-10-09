
package nsk.stress.stack;

import java.io.PrintStream;

public class stack007 implements stack007i {

    final static int ITERATIONS = 1000;

    final static int INCREMENT = 100;

    public static void main(String[] args) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
    }

    public static int run(String[] args, PrintStream out) {
        stack007i test = new stack007();
        int depth;
        for (depth = 100; ; depth += INCREMENT) try {
            test.recurse(depth);
        } catch (StackOverflowError soe) {
            break;
        } catch (OutOfMemoryError oome) {
            break;
        }
        out.println("Max. depth: " + depth);
        for (int i = 0; i < ITERATIONS; i++) try {
            test.recurse(10 * depth);
            out.println("?");
        } catch (StackOverflowError soe) {
        } catch (OutOfMemoryError oome) {
        }
        return 0;
    }

    public synchronized void recurse(int depth) {
        if (depth > 0)
            recurse(depth - 1);
    }
}

interface stack007i {

    void recurse(int depth);
}
