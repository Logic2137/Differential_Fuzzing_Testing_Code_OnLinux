



package nsk.stress.stack;


import java.io.PrintStream;

public class stack004 {
    public static void main(String[] args) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
    }

    public static int run(String args[], PrintStream out) {
        stack004 test = new stack004();
        int exitCode = test.doRun(args, out);
        return exitCode;
    }

    public int doRun(String args[], PrintStream out) {
        int depth;
        for (depth = 100; ; depth += 100)
            try {
                recurse(depth);
            } catch (StackOverflowError soe) {
                break;
            } catch (OutOfMemoryError oome) {
                break;
            }
        out.println("Max. depth: " + depth);
        for (int i = 0; i < 100; i++)
            try {
                recurse(2 * depth);
                out.println("?");
            } catch (StackOverflowError soe) {
                
            } catch (OutOfMemoryError oome) {
                
            }
        return 0;
    }

    final static void recurse(int depth) {
        if (depth > 0)
            recurse(depth - 1);
    }
}
