



package nsk.stress.numeric.numeric003;

import java.io.PrintStream;


public class numeric003 {
    
    public static final double TOLERANCE = 100; 

    
    private static boolean verbose = false;

    private static PrintStream out = null;

    
    private static void complain(Object x) {
        out.println("# " + x);
    }

    private static void print(Object x) {
        if (verbose)
            out.print(x);
    }

    private static void println(Object x) {
        print(x + "\n");
    }

    
    public static void main(String args[]) {
        int exitCode = run(args, System.out);
        System.exit(exitCode + 95);
        
    }

    
    public static int run(String args[], PrintStream out) {
        numeric003.out = out;

        boolean testPerformance = false;
        int numberOfCPU = 1;

        int argsShift = 0;
        for (; argsShift < args.length; argsShift++) {
            String argument = args[argsShift];

            if (!argument.startsWith("-"))
                break;

            if (argument.equals("-performance")) {
                testPerformance = true;
                continue;
            }

            if (argument.equals("-verbose")) {
                verbose = true;
                continue;
            }

            if (argument.startsWith("-CPU:")) {
                String value =
                        argument.substring("-CPU:".length(), argument.length());
                numberOfCPU = Integer.parseInt(value);

                if (numberOfCPU < 1) {
                    complain("Illegal number of CPU: " + argument);
                    return 2; 
                }
                continue;
            }

            complain("Cannot recognize argument: args[" + argsShift + "]: " + argument);
            return 2; 
        }

        if ((args.length < argsShift + 1) || (args.length > argsShift + 2)) {
            complain("Illegal argument(s). Execute:");
            complain(
                    "    java numeric003 [-verbose] [-performance] [-CPU:number] " +
                            "matrixSize [threads]");
            return 2; 
        }

        int size = Integer.parseInt(args[argsShift]);
        if ((size < 100) || (size > 10000)) {
            complain("Matrix size should be 100 to 1000 lines & columns.");
            return 2; 
        }

        int threads = size;
        if (args.length >= argsShift + 2)
            threads = Integer.parseInt(args[argsShift + 1]);
        if ((threads < 1) || (threads > size)) {
            complain("Threads number should be 1 to matrix size.");
            return 2; 
        }
        if ((size % threads) != 0) {
            complain("Threads number should evenly divide matrix size.");
            return 2; 
        }

        print("Preparing A[" + size + "," + size + "]:");
        SquareMatrix A = new SquareMatrix(size);
        SquareMatrix A1 = new SquareMatrix(size);
        SquareMatrix Am = new SquareMatrix(size);
        println(" done.");

        double singleThread = elapsedTime(out, A, A1, size, 1);
        double multiThreads = elapsedTime(out, A, Am, size, threads);

        print("Checking accuracy:");
        for (int line = 0; line < size; line++)
            for (int column = 0; column < size; column++)
                if (A1.value[line][column] != Am.value[line][column]) {
                    println("");
                    complain("Test failed:");
                    complain("Different results by single- and multi-threads:");
                    complain("  line=" + line + ", column=" + column);
                    complain("A1.value[line][column]=" + A1.value[line][column]);
                    complain("Am.value[line][column]=" + Am.value[line][column]);
                    return 2; 
                }
        println(" done.");

        if (testPerformance) {
            print("Checking performance: ");
            double elapsed1 = singleThread;
            double elapsedM = multiThreads * numberOfCPU;
            if (elapsed1 > elapsedM * (1 + TOLERANCE / 100)) {
                println("");
                complain("Test failed:");
                complain("Single-thread calculation is essentially slower:");
                complain("Calculation time elapsed (seconds):");
                complain("  single thread: " + singleThread);
                complain("  multi-threads: " + multiThreads);
                complain("  number of CPU: " + numberOfCPU);
                complain("  tolerance: " + TOLERANCE + "%");
                return 2; 
            }
            println("done.");
        }

        println("Test passed.");
        return 0; 
    }

    private static double elapsedTime(PrintStream out,
                                      SquareMatrix A, SquareMatrix AA, int size, int threads) {

        print("Computing A*A with " + threads + " thread(s):");
        long mark1 = System.currentTimeMillis();
        AA.setSquareOf(A, threads);
        long mark2 = System.currentTimeMillis();
        println(" done.");

        double sec = (mark2 - mark1) / 1000.0;
        double perf = size * size * (size + size) / sec;
        println("Elapsed time: " + sec + " seconds");
        println("Performance: " + perf / 1e6 + " MFLOPS");

        return sec;
    }

    
    private static class SquareMatrix {
        volatile long value[][];

        
        public SquareMatrix(int size) {
            value = new long[size][size];
            for (int line = 0; line < size; line++)
                for (int column = 0; column < size; column++)
                    value[line][column] = Math.round(Math.random() * size);
        }

        
        public void setSquareOf(SquareMatrix A, int threads) {
            if (this.value.length != A.value.length)
                throw new IllegalArgumentException(
                        "this.value.length != A.value.length");

            int size = value.length;
            if ((size % threads) != 0)
                throw new IllegalArgumentException("size%threads != 0");
            int bunch = size / threads;

            Thread task[] = new Thread[threads];
            for (int t = 0; t < threads; t++) {
                int line0 = bunch * t;
                MatrixComputer computer =
                        new MatrixComputer(value, A.value, line0, bunch);
                task[t] = new Thread(computer);
            }

            for (int t = 0; t < threads; t++)
                task[t].start();

            for (int t = 0; t < threads; t++)
                if (task[t].isAlive())
                    try {
                        task[t].join();
                    } catch (InterruptedException exception) {
                        throw new RuntimeException(exception.toString());
                    }
        }

        
        private static class MatrixComputer implements Runnable {
            private long result[][];
            private long source[][];
            private int line0;
            private int bunch;

            
            public MatrixComputer(
                    long result[][], long source[][], int line0, int bunch) {

                this.result = result;   
                this.source = source;   
                this.line0 = line0;     
                this.bunch = bunch;     
            }

            
            public void run() {
                int line1 = line0 + bunch;
                int size = result.length;
                for (int line = line0; line < line1; line++)
                    for (int column = 0; column < size; column++) {
                        long sum = 0;
                        for (int i = 0; i < size; i++)
                            sum += source[line][i] * source[i][column];
                        result[line][column] = sum;
                    }
            }

        }

    }

}
