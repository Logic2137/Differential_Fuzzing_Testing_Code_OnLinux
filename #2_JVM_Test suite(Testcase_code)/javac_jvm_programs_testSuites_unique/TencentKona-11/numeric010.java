



package nsk.stress.numeric.numeric010;

import java.io.PrintStream;


public class numeric010 {
    
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
        numeric010.out = out;

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
                    "    java numeric010 [-verbose] [-performance] " +
                            "[-tolerance:percents] [-CPU:number] matrixSize [threads]");
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
        IntegerMatrix intA = new IntegerMatrix(size);
        IntegerMatrix intAA = new IntegerMatrix(size);
        LongMatrix longA = new LongMatrix(intA);
        LongMatrix longAA = new LongMatrix(intA);
        FloatMatrix floatA = new FloatMatrix(intA);
        FloatMatrix floatAA = new FloatMatrix(intA);
        DoubleMatrix doubleA = new DoubleMatrix(intA);
        DoubleMatrix doubleAA = new DoubleMatrix(intA);
        println(" done.");

        double elapsed[] = {0, 0};

        for (int i = 0; i < 2; i++) {
            double seconds =
                    elapsedTime((i == 0 ? 1 : threads),
                            intA, intAA,
                            longA, longAA,
                            floatA, floatAA,
                            doubleA, doubleAA);
            elapsed[i] = seconds;

            print("Checking accuracy:");
            for (int line = 0; line < size; line++)
                for (int column = 0; column < size; column++) {
                    if (intAA.value[line][column] != longAA.value[line][column]) {
                        println("");
                        complain("Test failed:");
                        complain("Integer and Long results differ at:");
                        complain("  line=" + line + ", column=" + column);
                        complain(" intAA.value[line][column]=" + intAA.value[line][column]);
                        complain("longAA.value[line][column]=" + longAA.value[line][column]);
                        return 2; 
                    }
                    if (intAA.value[line][column] != floatAA.value[line][column]) {
                        println("");
                        complain("Test failed:");
                        complain("Integer and Float results differ at:");
                        complain("  line=" + line + ", column=" + column);
                        complain("  intAA.value[line][column]=" + intAA.value[line][column]);
                        complain("floatAA.value[line][column]=" + floatAA.value[line][column]);
                        return 2; 
                    }
                    if (intAA.value[line][column] != doubleAA.value[line][column]) {
                        println("");
                        complain("Test failed:");
                        complain("Integer and Double results differ at:");
                        complain("  line=" + line + ", column=" + column);
                        complain("   intAA.value[line][column]=" + intAA.value[line][column]);
                        complain("doubleAA.value[line][column]=" + doubleAA.value[line][column]);
                        return 2; 
                    }
                }
            println(" done.");
        }

        double overallTime = elapsed[0] + elapsed[1];
        double averageTime = overallTime / 2;   
        double averagePerformance = 4 * size * size * (size + size) / averageTime / 1e6;
        println("");
        println("Overall elapsed time: " + overallTime + " seconds.");
        println("Average elapsed time: " + averageTime + " seconds.");
        println("Average performance: " + averagePerformance + " MOPS");

        if (testPerformance) {
            println("");
            print("Checking performance: ");
            double elapsed1 = elapsed[0];
            double elapsedM = elapsed[1] * numberOfCPU;
            if (elapsed1 > elapsedM * (1 + tolerance / 100)) {
                println("");
                complain("Test failed:");
                complain("Single-thread calculation is essentially slower:");
                complain("Calculation time elapsed (seconds):");
                complain("  single thread: " + elapsed[0]);
                complain("  multi-threads: " + elapsed[1]);
                complain("  number of CPU: " + numberOfCPU);
                complain("  tolerance: " + tolerance + "%");
                return 2; 
            }
            println("done.");
        }

        println("Test passed.");
        return 0; 
    }

    
    private static double elapsedTime(int threads,
                                      IntegerMatrix intA, IntegerMatrix intAA,
                                      LongMatrix longA, LongMatrix longAA,
                                      FloatMatrix floatA, FloatMatrix floatAA,
                                      DoubleMatrix doubleA, DoubleMatrix doubleAA) {

        println("");
        print("Computing A*A with " + threads + " thread(s):");
        long mark1 = System.currentTimeMillis();
        intAA.setSquareOf(intA, threads);
        longAA.setSquareOf(longA, threads);
        floatAA.setSquareOf(floatA, threads);
        doubleAA.setSquareOf(doubleA, threads);
        long mark2 = System.currentTimeMillis();
        println(" done.");

        int size = intA.size();
        double sec = (mark2 - mark1) / 1000.0;
        double perf = 4 * size * size * (size + size) / sec;
        println("Elapsed time: " + sec + " seconds");
        println("Performance: " + perf / 1e6 + " MOPS");

        return sec;
    }

    
    private static class IntegerMatrix {
        volatile int value[][];

        
        public int size() {
            return value.length;
        }

        
        public IntegerMatrix(int size) {
            value = new int[size][size];
            for (int line = 0; line < size; line++)
                for (int column = 0; column < size; column++)
                    value[line][column] =
                            Math.round((float) ((1 - 2 * Math.random()) * size));
        }

        
        public void setSquareOf(IntegerMatrix A, int threads) {
            if (this.size() != A.size())
                throw new IllegalArgumentException(
                        "this.size() != A.size()");

            if ((size() % threads) != 0)
                throw new IllegalArgumentException("size()%threads != 0");
            int bunch = size() / threads;

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
            private int result[][];
            private int source[][];
            private int line0;
            private int bunch;

            
            public MatrixComputer(
                    int result[][], int source[][], int line0, int bunch) {

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
                        int sum = 0;
                        for (int i = 0; i < size; i++)
                            sum += source[line][i] * source[i][column];
                        result[line][column] = sum;
                    }
            }

        }

    }

    
    private static class LongMatrix {
        volatile long value[][];

        
        public int size() {
            return value.length;
        }


        
        public LongMatrix(IntegerMatrix A) {
            int size = A.size();
            value = new long[size][size];
            for (int line = 0; line < size; line++)
                for (int column = 0; column < size; column++)
                    value[line][column] = A.value[line][column];
        }

        
        public void setSquareOf(LongMatrix A, int threads) {
            if (this.size() != A.size())
                throw new IllegalArgumentException(
                        "this.size() != A.size()");

            if ((size() % threads) != 0)
                throw new IllegalArgumentException("size()%threads != 0");
            int bunch = size() / threads;

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

    
    private static class FloatMatrix {
        volatile float value[][];

        
        public int size() {
            return value.length;
        }


        
        public FloatMatrix(IntegerMatrix A) {
            int size = A.size();
            value = new float[size][size];
            for (int line = 0; line < size; line++)
                for (int column = 0; column < size; column++)
                    value[line][column] = A.value[line][column];
        }

        
        public void setSquareOf(FloatMatrix A, int threads) {
            if (this.size() != A.size())
                throw new IllegalArgumentException(
                        "this.size() != A.size()");

            if ((size() % threads) != 0)
                throw new IllegalArgumentException("size()%threads != 0");
            int bunch = size() / threads;

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
            private float result[][];
            private float source[][];
            private int line0;
            private int bunch;

            
            public MatrixComputer(
                    float result[][], float source[][], int line0, int bunch) {

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
                        float sum = 0;
                        for (int i = 0; i < size; i++)
                            sum += source[line][i] * source[i][column];
                        result[line][column] = sum;
                    }
            }

        }

    }

    
    private static class DoubleMatrix {
        volatile double value[][];

        
        public int size() {
            return value.length;
        }


        
        public DoubleMatrix(IntegerMatrix A) {
            int size = A.size();
            value = new double[size][size];
            for (int line = 0; line < size; line++)
                for (int column = 0; column < size; column++)
                    value[line][column] = A.value[line][column];
        }

        
        public void setSquareOf(DoubleMatrix A, int threads) {
            if (this.size() != A.size())
                throw new IllegalArgumentException(
                        "this.size() != A.size()");

            if ((size() % threads) != 0)
                throw new IllegalArgumentException("size()%threads != 0");
            int bunch = size() / threads;

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
            private double result[][];
            private double source[][];
            private int line0;
            private int bunch;

            
            public MatrixComputer(
                    double result[][], double source[][], int line0, int bunch) {

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
                        double sum = 0;
                        for (int i = 0; i < size; i++)
                            sum += source[line][i] * source[i][column];
                        result[line][column] = sum;
                    }
            }

        }

    }

}
