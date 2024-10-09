



package nsk.stress.numeric.numeric009;

import java.io.PrintStream;


public class numeric009 {
    
    public static double tolerance = 100; 

    
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
        numeric009.out = out;

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

            if (argument.startsWith("-tolerance:")) {
                String percents =
                        argument.substring("-tolerance:".length(), argument.length());
                tolerance = Integer.parseInt(percents);
                if ((tolerance < 0) || (tolerance > 100)) {
                    complain("Tolerance should be 0 to 100%: " + argument);
                    return 2; 
                }
                continue;
            }

            complain("Cannot recognize argument: args[" + argsShift + "]: " + argument);
            return 2; 
        }

        if (args.length != argsShift + 2) {
            complain("Illegal arguments. Execute:");
            complain(
                    "    java numeric009 [-verbose] [-performance] " +
                            "[-tolerance:percents] matrixSize iterations");
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
        int[][] intA = newIntegerMatrix(size);
        int[][] intAA = new int[size][size];
        long[][] longA = newLongMatrix(intA);
        long[][] longAA = new long[size][size];
        double[][] doubleA = newDoubleMatrix(intA);
        double[][] doubleAA = new double[size][size];
        float[][] floatA = newFloatMatrix(intA);
        float[][] floatAA = new float[size][size];
        println(" done.");

        println("Should try " + iterations + " iteration(s):");
        println("==========================" +
                ((iterations > 99) ? "==" : (iterations > 9) ? "=" : ""));
        println("");

        double overallTime = 0;
        double firstTime = 0;
        double lastTime = 0;

        for (int i = 1; i <= iterations; i++) {
            double seconds = elapsedTime(i,
                    intA, intAA, longA, longAA, floatA, floatAA, doubleA, doubleAA);

            if (i == 1)
                firstTime = seconds;
            else
                lastTime = seconds;
            overallTime += seconds;

            print("Checking accuracy:");
            for (int line = 0; line < size; line++)
                for (int column = 0; column < size; column++) {
                    if (intAA[line][column] != longAA[line][column]) {
                        println("");
                        complain("Test failed:");
                        complain("Integer and Long results differ at:");
                        complain("  line=" + line + ", column=" + column);
                        return 2; 
                    }
                    if (intAA[line][column] != floatAA[line][column]) {
                        println("");
                        complain("Test failed:");
                        complain("Integer and Float results differ at:");
                        complain("  line=" + line + ", column=" + column);
                        return 2; 
                    }
                    if (intAA[line][column] != doubleAA[line][column]) {
                        println("");
                        complain("Test failed:");
                        complain("Integer and Double results differ at:");
                        complain("  line=" + line + ", column=" + column);
                        return 2; 
                    }
                }
            println(" done.");
        }

        double averageTime = overallTime / iterations / 4;
        double averagePerformance = size * size * (size + size) / averageTime / 1e6;

        println("");
        println("=======================" +
                ((iterations > 99) ? "==" : (iterations > 9) ? "=" : ""));
        println("Overall iteration(s): " + iterations);
        println("Overall elapsed time: " + overallTime + " seconds.");
        println("Average elapsed time: " + averageTime + " seconds.");
        println("Average performance: " + averagePerformance + " MFLOPS");

        if (testPerformance) {
            print("Checking performance: ");
            if (firstTime > lastTime * (1 + tolerance / 100)) {
                println("");
                complain("Test failed:");
                complain("1st iterartion is essentially slower:");
                complain("Calculation time elapsed (seconds):");
                complain("  1-st iteration: " + firstTime);
                complain("  last iteration: " + lastTime);
                complain("  tolerance: " + tolerance + "%");
                return 2; 
            }
            println("done.");
        }

        println("Test passed.");
        return 0; 
    }

    
    private static double elapsedTime(int i,
                                      int[][] intA, int[][] intAA,
                                      long[][] longA, long[][] longAA,
                                      float[][] floatA, float[][] floatAA,
                                      double[][] doubleA, double[][] doubleAA) {

        int size = intA.length;

        if (i > 1)
            println("");
        println("Iteration #" + i + ":");

        print("Computing int, long, float, and double A*A:");
        long mark1 = System.currentTimeMillis();
        setSquare(intA, intAA);
        setSquare(longA, longAA);
        setSquare(floatA, floatAA);
        setSquare(doubleA, doubleAA);
        long mark2 = System.currentTimeMillis();
        println(" done.");

        double sec = (mark2 - mark1) / 1000.0;
        double perf = size * size * (size + size) / (sec / 4);
        println("Elapsed time: " + sec + " seconds");
        println("Performance: " + perf / 1e6 + " MOPS");

        return sec;
    }

    
    private static void setSquare(int[][] A, int[][] AA) {
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
                int sum = 0;
                for (int k = 0; k < size; k++)
                    sum += A[line][k] * A[k][line];
                AA[line][column] = sum;
            }
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

    
    private static void setSquare(float[][] A, float[][] AA) {
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
                float sum = 0;
                for (int k = 0; k < size; k++)
                    sum += A[line][k] * A[k][line];
                AA[line][column] = sum;
            }
    }

    
    private static void setSquare(double[][] A, double[][] AA) {
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
                double sum = 0;
                for (int k = 0; k < size; k++)
                    sum += A[line][k] * A[k][line];
                AA[line][column] = sum;
            }
    }

    
    private static int[][] newIntegerMatrix(int size) {
        if ((size < 1) || (size > 1000))
            throw new IllegalArgumentException(
                    "matrix size should be 1 to 1000");

        int[][] A = new int[size][size];

        for (int line = 0; line < size; line++)
            for (int column = 0; column < size; column++)
                A[line][column] =
                        Math.round((float) ((1 - 2 * Math.random()) * size));

        return A;
    }

    
    private static long[][] newLongMatrix(int[][] intA) {
        if (intA.length != intA[0].length)
            throw new IllegalArgumentException(
                    "need square argument matrix");

        int size = intA.length;
        long[][] longA = new long[size][size];

        for (int line = 0; line < size; line++)
            for (int column = 0; column < size; column++)
                longA[line][column] = intA[line][column];

        return longA;
    }

    
    private static double[][] newDoubleMatrix(int[][] intA) {
        if (intA.length != intA[0].length)
            throw new IllegalArgumentException(
                    "need square argument matrix");

        int size = intA.length;
        double[][] doubleA = new double[size][size];

        for (int line = 0; line < size; line++)
            for (int column = 0; column < size; column++)
                doubleA[line][column] = intA[line][column];

        return doubleA;
    }

    
    private static float[][] newFloatMatrix(int[][] intA) {
        if (intA.length != intA[0].length)
            throw new IllegalArgumentException(
                    "need square argument matrix");

        int size = intA.length;
        float[][] floatA = new float[size][size];

        for (int line = 0; line < size; line++)
            for (int column = 0; column < size; column++)
                floatA[line][column] = intA[line][column];

        return floatA;
    }

}
