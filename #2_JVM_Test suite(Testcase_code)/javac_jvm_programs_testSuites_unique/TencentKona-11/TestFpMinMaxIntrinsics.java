



package compiler.intrinsics.math;

import java.util.Arrays;
import java.util.Random;
import java.lang.reflect.Method;

public class TestFpMinMaxIntrinsics {

    private static final float fPos     =  15280.0f;
    private static final float fNeg     = -55555.5f;
    private static final float fPosZero =      0.0f;
    private static final float fNegZero =     -0.0f;
    private static final float fPosInf  = Float.POSITIVE_INFINITY;
    private static final float fNegInf  = Float.NEGATIVE_INFINITY;
    private static final float fNaN     = Float.NaN;

    private static final double dPos     =  482390926662501720.0;
    private static final double dNeg     = -333333333333333333.3;
    private static final double dPosZero =                   0.0;
    private static final double dNegZero =                  -0.0;
    private static final double dPosInf  = Double.POSITIVE_INFINITY;
    private static final double dNegInf  = Double.NEGATIVE_INFINITY;
    private static final double dNaN     = Double.NaN;

    private static final float[][] f_cases = {
        
        {     fPos,     fPos,     fPos,     fPos },
        {     fNeg,     fNeg,     fNeg,     fNeg },
        {     fPos,     fNeg,     fNeg,     fPos },
        {     fNeg,     fPos,     fNeg,     fPos },

        { fPosZero, fNegZero, fNegZero, fPosZero },
        { fNegZero, fPosZero, fNegZero, fPosZero },
        { fNegZero, fNegZero, fNegZero, fNegZero },

        {     fPos,  fPosInf,     fPos,  fPosInf },
        {     fNeg,  fNegInf,  fNegInf,     fNeg },

        {     fPos,     fNaN,     fNaN,     fNaN },
        {     fNaN,     fPos,     fNaN,     fNaN },
        {     fNeg,     fNaN,     fNaN,     fNaN },
        {     fNaN,     fNeg,     fNaN,     fNaN },

        {  fPosInf,     fNaN,     fNaN,     fNaN },
        {     fNaN,  fPosInf,     fNaN,     fNaN },
        {  fNegInf,     fNaN,     fNaN,     fNaN },
        {     fNaN,  fNegInf,     fNaN,     fNaN }
    };

    private static final double[][] d_cases = {
        
        {     dPos,     dPos,     dPos,     dPos },
        {     dNeg,     dNeg,     dNeg,     dNeg },
        {     dPos,     dNeg,     dNeg,     dPos },
        {     dNeg,     dPos,     dNeg,     dPos },

        { dPosZero, dNegZero, dNegZero, dPosZero },
        { dNegZero, dPosZero, dNegZero, dPosZero },
        { dNegZero, dNegZero, dNegZero, dNegZero },

        {     dPos,  dPosInf,     dPos,  dPosInf },
        {     dNeg,  dNegInf,  dNegInf,     dNeg },

        {     dPos,     dNaN,     dNaN,     dNaN },
        {     dNaN,     dPos,     dNaN,     dNaN },
        {     dNeg,     dNaN,     dNaN,     dNaN },
        {     dNaN,     dNeg,     dNaN,     dNaN },

        {  dPosInf,     dNaN,     dNaN,     dNaN },
        {     dNaN,  dPosInf,     dNaN,     dNaN },
        {  dNegInf,     dNaN,     dNaN,     dNaN },
        {     dNaN,  dNegInf,     dNaN,     dNaN }
    };

    private static void fTest(float[] row) {
        fCheck(row[0], row[1], Math.min(row[0], row[1]), Math.max(row[0], row[1]), row[2], row[3]);
    }

    private static void fReductionTest(float[] row) {
        float fmin = row[0], fmax = row[0];

        for (int i=0; i<100; i++) {
            fmin = Math.min(fmin, row[1]);
            fmax = Math.max(fmax, row[1]);
        }

        fCheck(row[0], row[1], fmin, fmax, row[2], row[3]);
    }

    private static void fCheck(float a, float b, float fmin, float fmax, float efmin, float efmax) {
        int min = Float.floatToRawIntBits(fmin);
        int max = Float.floatToRawIntBits(fmax);
        int emin = Float.floatToRawIntBits(efmin);
        int emax = Float.floatToRawIntBits(efmax);

        if (min != emin || max != emax) {
            throw new AssertionError("Unexpected result of float min/max: " +
                    "a = " + a + ", b = " + b + ", " +
                    "result = (" + fmin + ", " + fmax + "), " +
                    "expected = (" + efmin + ", " + efmax + ")");
        }
    }

    private static void dTest(double[] row) {
        dCheck(row[0], row[1], Math.min(row[0], row[1]), Math.max(row[0], row[1]), row[2], row[3]);
    }

    private static void dReductionTest(double[] row) {
        double dmin = row[0], dmax = row[0];

        for (int i=0; i<100; i++) {
            dmin = Math.min(dmin, row[1]);
            dmax = Math.max(dmax, row[1]);
        }

        dCheck(row[0], row[1], dmin, dmax, row[2], row[3]);
    }

    private static void dCheck(double a, double b, double dmin, double dmax, double edmin, double edmax) {
        double min = Double.doubleToRawLongBits(dmin);
        double max = Double.doubleToRawLongBits(dmax);
        double emin = Double.doubleToRawLongBits(edmin);
        double emax = Double.doubleToRawLongBits(edmax);

        if (min != emin || max != emax) {
            throw new AssertionError("Unexpected result of double min/max: " +
                    "a = " + a + ", b = " + b + ", " +
                    "result = (" + dmin + ", " + dmax + "), " +
                    "expected = (" + edmin + ", " + edmax + ")");
        }
    }

    public static void sanityTests() {
        Arrays.stream(f_cases).forEach(TestFpMinMaxIntrinsics::fTest);
        Arrays.stream(d_cases).forEach(TestFpMinMaxIntrinsics::dTest);
    }

    public static void reductionTests() {
        Arrays.stream(f_cases).forEach(TestFpMinMaxIntrinsics::fReductionTest);
        Arrays.stream(d_cases).forEach(TestFpMinMaxIntrinsics::dReductionTest);
    }

    public static void main(String[] args) throws Exception {
        Method m = TestFpMinMaxIntrinsics.class.getDeclaredMethod(args[0]);
        for (int i = 0 ; i < Integer.parseInt(args[1]) ; i++)
            m.invoke(null);
    }

    private static final int COUNT = 1000;
    private static final int LOOPS = 100;

    private static Random r = new Random();

    private static Node[] pool = new Node[COUNT];

    private static long time = 0;
    private static long times = 0;

    public static void init() {
        for (int i=0; i<COUNT; i++)
            pool[i] = new Node(Double.NaN);
    }

    public static void finish() {
        
        
        System.out.println("Average time: " + (time/times) + " ns");
    }

    public static void randomSearchTree() {
        init();
        for (int l=0; l < LOOPS; l++) {
            Node root = pool[0].reset(r.nextDouble());

            for (int i=1; i<COUNT; i++)
                insert(root, pool[i].reset(r.nextDouble()));
        }
        finish();
    }

    public static void sortedSearchTree() {
        init();
        for (int l=0; l < LOOPS; l++) {
            Node root = pool[0].reset(-0.0);

            for (int i=1; i<COUNT; i++)
                insert(root, pool[i].reset(i-1));
        }
        finish();
    }

    private static class Node {
        private double value;
        private Node min;
        private Node max;

        public Node(double d) { value = d; }

        public Node reset(double d) { value = d; min = max = null; return this; }

        @Override
        public String toString() {
            return  (min != null ? min + ", " : "") +
                    value +
                    (max != null ? ", " + max : "");
        }
    }

    private static Node insert(Node root, Node d) {
        for ( ; ; ) {
            long rootBits = Double.doubleToRawLongBits(root.value);
            long dBits = Double.doubleToRawLongBits(d.value);

            
            if (rootBits == dBits)
                return root;

            long delta = System.nanoTime();

            double dmin = min(root.value, d.value);

            time += System.nanoTime() - delta;
            times++;

            long minBits = Double.doubleToRawLongBits(dmin);

            if (minBits == dBits)
                if (root.min != null)
                    root = root.min;
                else
                    return root.min = d;
            else
                if (root.max != null)
                    root = root.max;
                else
                    return root.max = d;
        }
    }

    
    private static double min(double a, double b) {
        return Math.min(a, b);
    }
}
