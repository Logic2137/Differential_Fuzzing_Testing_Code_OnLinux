



package nsk.stress.numeric.numeric007;

import java.io.PrintStream;


public class numeric007 {
    
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
        numeric007.out = out;

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

            complain("Cannot recognize argument: args[" + argsShift + "]: " + argument);
            return 2; 
        }

        if (args.length != argsShift + 2) {
            complain("Illegal arguments. Execute:");
            complain(
                    "    java numeric007 [-verbose] [-performance] [-CPU:number] " +
                            "matrixSize iterations");
            return 2; 
        }

        int size = Integer.parseInt(args[argsShift]);
        if ((size < 100) || (size > 10000)) {
            complain("Matrix size should be 100 to 1000 lines & columns.");
            return 2; 
        }

        int iterations = Integer.parseInt(args[argsShift + 1]);
        if ((iterations < 1) || (iterations > 100)) {
            complain("Iterations number should be 1 to 100.");
            return 2; 
        }

        print("Preparing A[" + size + "," + size + "]:");
        long[][] A = newMatrix(size);
        long[][] A1 = new long[size][size];
        long[][] Ai = new long[size][size];
        println(" done.");

        println("Should try " + iterations + " iteration(s):");
        println("==========================" +
                ((iterations > 99) ? "==" : (iterations > 9) ? "=" : ""));
        println("");

        double overallTime = 0;
        double firstTime = 0;
        double lastTime = 0;

        for (int i = 1; i <= iterations; i++) {
            double seconds;

            if (i == 1) {
                seconds = elapsedTime(i, A, A1);
                firstTime = seconds;
            } else {
                seconds = elapsedTime(i, A, Ai);
                lastTime = seconds;
            }

            overallTime += seconds;
        }

        double averageTime = overallTime / iterations;
        double averagePerformance = size * size * (size + size) / averageTime / 1e6;

        println("");
        println("=======================" +
                ((iterations > 99) ? "==" : (iterations > 9) ? "=" : ""));
        println("Overall iteration(s): " + iterations);
        println("Overall elapsed time: " + overallTime + " seconds.");
        println("Average elapsed time: " + averageTime + " seconds.");
        println("Average performance: " + averagePerformance + " MFLOPS");

        println("========================");
        print("Checking accuracy:");
        for (int line = 0; line < size; line++)
            for (int column = 0; column < size; column++)
                if (A1[line][column] != Ai[line][column]) {
                    println("");
                    complain("Test failed:");
                    complain("Different results in 1st and last iterations:");
                    complain("  line=" + line + ", column=" + column);
                    return 2; 
                }
        println(" done.");

        if (testPerformance) {
            print("Checking performance: ");
            if (firstTime > lastTime * (1 + TOLERANCE / 100)) {
                println("");
                complain("Test failed:");
                complain("1st iterartion is essentially slower:");
                complain("Calculation time elapsed (seconds):");
                complain("  1-st iteration: " + firstTime);
                complain("  last iteration: " + lastTime);
                complain("  tolerance: " + TOLERANCE + "%");
                return 2; 
            }
            println("done.");
        }

        println("Test passed.");
        return 0; 
    }

    private static double elapsedTime(int i, long[][] A, long[][] AA) {
        int size = A.length;

        if (i > 1)
            println("");
        println("Iteration #" + i + ":");

        print("Computing A*A:");
        long mark1 = System.currentTimeMillis();
        setSquare(A, AA);
        long mark2 = System.currentTimeMillis();
        println(" done.");

        double sec = (mark2 - mark1) / 1000.0;
        double perf = size * size * (size + size) / sec;
        println("Elapsed time: " + sec + " seconds");
        println("Performance: " + perf / 1e6 + " MFLOPS");

        return sec;
    }

    
    private static void setSquare(long[][] A, long[][] AA) {
        if (A.length != A[0].length)
            throw new IllegalArgumentException(
                    "the argument matrix A should be square matrix");
        if (AA.length != AA[0].length)
            throw new IllegalArgumentException(
                    "the resulting matrix AA should be square matrix");
        if (A.length != AA.length)
            throw new IllegalArgumentException(
                    "the matrices A and AA should have equal size");

        int size = A.length;

        for (int line = 0; line < size; line++)
            for (int column = 0; column < size; column++) {
                long sum = 0;
                for (int k = 0; k < size; k++)
                    sum += A[line][k] * A[k][line];
                AA[line][column] = sum;
            }
    }

    
    private static long[][] newMatrix(int size) {
        if ((size < 1) || (size > 1000))
            throw new IllegalArgumentException(
                    "matrix size should be 1 to 1000");

        long[][] A = new long[size][size];

        for (int line = 0; line < size; line++)
            for (int column = 0; column < size; column++)
                A[line][column] = Math.round((1 - 2 * Math.random()) * size);

        return A;
    }

}
