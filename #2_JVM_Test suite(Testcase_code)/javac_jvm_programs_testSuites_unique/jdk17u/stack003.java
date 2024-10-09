
package nsk.stress.stack;

import java.io.PrintStream;

public class stack003 {

    final static int ITERATIONS = 100;

    final static int INCREMENT = 100;

    public static void main(String[] args) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
    }

    public static int run(String[] args, PrintStream out) {
        int depth;
        for (depth = 1; ; depth += INCREMENT) try {
            recurse(depth);
        } catch (StackOverflowError soe) {
            break;
        } catch (OutOfMemoryError oome) {
            break;
        }
        out.println("Max. depth: " + depth);
        for (int i = 0; i < ITERATIONS; i++) try {
            recurse(2 * depth);
            out.println("?");
        } catch (StackOverflowError soe) {
        } catch (OutOfMemoryError oome) {
        }
        return 0;
    }

    static void recurse(int depth) {
        if (depth > 0)
            recurse(depth - 1);
    }
}
