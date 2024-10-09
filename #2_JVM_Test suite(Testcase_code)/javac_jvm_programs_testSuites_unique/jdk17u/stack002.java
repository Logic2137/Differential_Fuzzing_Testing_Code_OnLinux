
package nsk.stress.stack;

import java.io.PrintStream;

public class stack002 {

    static final long timeout = 10000;

    public static void main(String[] args) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
    }

    public static int run(String[] args, PrintStream out) {
        Tester tester = new Tester(out);
        Timer timer = new Timer(tester);
        timer.start();
        tester.start();
        while (timer.isAlive()) try {
            timer.join();
        } catch (InterruptedException e) {
            e.printStackTrace(out);
            return 2;
        }
        out.println("Maximal depth: " + tester.maxdepth);
        return 0;
    }

    private static class Tester extends Thread {

        int maxdepth;

        PrintStream out;

        public Tester(PrintStream out) {
            this.out = out;
            maxdepth = 0;
        }

        public void run() {
            recurse(0);
        }

        void recurse(int depth) {
            maxdepth = depth;
            try {
                recurse(depth + 1);
            } catch (Error error) {
                if (!(error instanceof StackOverflowError) && !(error instanceof OutOfMemoryError))
                    throw error;
                recurse(depth + 1);
            }
        }
    }

    private static class Timer extends Thread {

        private Tester tester;

        public Timer(Tester tester) {
            this.tester = tester;
        }

        public void run() {
            long started;
            started = System.currentTimeMillis();
            while (System.currentTimeMillis() - started < timeout) ;
            tester.stop();
        }
    }
}
