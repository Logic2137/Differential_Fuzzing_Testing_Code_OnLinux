
package nsk.stress.stack;

import java.io.PrintStream;

public class stack006 implements stack006i {

    public static void main(String[] args) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
    }

    public static int run(String[] args, PrintStream out) {
        stack006i test = new stack006();
        int depth;
        for (depth = 100; ; depth += 100) try {
            test.recurse(depth);
        } catch (StackOverflowError soe) {
            break;
        } catch (OutOfMemoryError oome) {
            break;
        }
        out.println("Max. depth: " + depth);
        for (int i = 0; i < 100; i++) try {
            test.recurse(2 * depth);
            out.println("?");
        } catch (StackOverflowError soe) {
        } catch (OutOfMemoryError oome) {
        }
        return 0;
    }

    public void recurse(int depth) {
        if (depth > 0)
            recurse(depth - 1);
    }
}

interface stack006i {

    void recurse(int depth);
}
