
package nsk.stress.stack;

import java.io.PrintStream;

public class stack009 {

    public static void main(String[] args) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
    }

    public static int run(String[] args, PrintStream out) {
        for (int depth = 100; ; depth += 100) try {
            recurse(depth);
        } catch (Error error1) {
            if (!(error1 instanceof StackOverflowError) && !(error1 instanceof OutOfMemoryError))
                throw error1;
            out.println("Max. depth: " + depth);
            try {
                recurse(10 * depth);
                out.println("?");
            } catch (Error error2) {
                if (!(error2 instanceof StackOverflowError) && !(error2 instanceof OutOfMemoryError))
                    throw error2;
            }
            break;
        }
        return 0;
    }

    static void recurse(int depth) {
        if (depth > 0)
            recurse(depth - 1);
    }
}
