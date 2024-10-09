



package nsk.stress.stack;


import java.io.PrintStream;

public class stack001 {
    public static void main(String[] args) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
    }

    public static int run(String args[], PrintStream out) {
        stack001 test = new stack001();
        test.recurse(0);
        out.println("Maximal depth: " + test.maxdepth);
        return 0;
    }

    private int maxdepth;

    private void recurse(int depth) {
        maxdepth = depth;
        try {
            recurse(depth + 1);
        } catch (Error error) {
            if (!(error instanceof StackOverflowError) &&
                    !(error instanceof OutOfMemoryError))
                throw error;

            if (maxdepth == depth)
                recurse(depth + 1);
        }
    }
}
