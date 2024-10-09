



public class FloatingPointFoldingTest {
    
    public static final long MINUS_INF_LONGBITS = 0xfff0000000000000L;
    public static final double DOUBLE_MINUS_INF = Double.longBitsToDouble(MINUS_INF_LONGBITS);

    public static final long PLUS_INF_LONGBITS = 0x7ff0000000000000L;
    public static final double DOUBLE_PLUS_INF = Double.longBitsToDouble(PLUS_INF_LONGBITS);

    public static final long MINUS_ZERO_LONGBITS = 0x8000000000000000L;
    public static final double DOUBLE_MINUS_ZERO = Double.longBitsToDouble(MINUS_ZERO_LONGBITS);

    
    
    
    
    
    
    public static final long NAN_LONGBITS = 0x7ffc000000000000L;
    public static final double DOUBLE_NAN = Double.longBitsToDouble(NAN_LONGBITS);

    
    public static final int MINUS_INF_INTBITS = 0xff800000;
    public static final float FLOAT_MINUS_INF = Float.intBitsToFloat(MINUS_INF_INTBITS);

    public static final int PLUS_INF_INTBITS = 0x7f800000;
    public static final float FLOAT_PLUS_INF = Float.intBitsToFloat(PLUS_INF_INTBITS);

    public static final int MINUS_ZERO_INTBITS = 0x80000000;
    public static final float FLOAT_MINUS_ZERO = Float.intBitsToFloat(MINUS_ZERO_INTBITS);

    
    
    public static final int NAN_INTBITS = 0x7fe00000;
    public static final float FLOAT_NAN = Float.intBitsToFloat(NAN_INTBITS);


    
    static void test_double_inf(long[] result) {
        double d1 = DOUBLE_MINUS_INF;
        double d2 = DOUBLE_PLUS_INF;
        result[0] = Double.doubleToRawLongBits(d1);
        result[1] = Double.doubleToRawLongBits(d2);
    }

    static void test_double_zero(long[] result) {
        double d1 = DOUBLE_MINUS_ZERO;
        double d2 = 0;
        result[0] = Double.doubleToRawLongBits(d1);
        result[1] = Double.doubleToRawLongBits(d2);
    }

    static void test_double_nan(long[] result) {
        double d1 = DOUBLE_NAN;
        double d2 = Double.NaN;
        result[0] = Double.doubleToRawLongBits(d1);
        result[1] = Double.doubleToRawLongBits(d2);
    }

    
    static void test_float_inf(int[] result) {
        float f1 = FLOAT_MINUS_INF;
        float f2 = FLOAT_PLUS_INF;
        result[0] = Float.floatToRawIntBits(f1);
        result[1] = Float.floatToRawIntBits(f2);
    }

    static void test_float_zero(int[] result) {
        float f1 = FLOAT_MINUS_ZERO;
        float f2 = 0;
        result[0] = Float.floatToRawIntBits(f1);
        result[1] = Float.floatToRawIntBits(f2);
    }

    static void test_float_nan(int[] result) {
        float f1 = FLOAT_NAN;
        float f2 = Float.NaN;
        result[0] = Float.floatToRawIntBits(f1);
        result[1] = Float.floatToRawIntBits(f2);
    }

    
    static void check_double(long[] result, double d1, double d2) {
        if (result[0] == result[1]) {
            throw new RuntimeException("ERROR: Two different double values are considered equal. \n"
                                       + String.format("\toriginal values: 0x%x 0x%x\n", Double.doubleToRawLongBits(d1), Double.doubleToRawLongBits(d2))
                                       + String.format("\tvalues after execution of method test(): 0x%x 0x%x", result[0], result[1]));
        }
    }

    
    static void check_float(int[] result, float f1, float f2) {
        if (result[0] == result[1]) {
            throw new RuntimeException("ERROR: Two different float values are considered equal. \n"
                                       + String.format("\toriginal values: 0x%x 0x%x\n", Float.floatToRawIntBits(f1), Float.floatToRawIntBits(f2))
                                       + String.format("\tvalues after execution of method test(): 0x%x 0x%x", result[0], result[1]));
        }
    }

    public static void main(String[] args) {
        

        int[] iresult = new int[2];

        
        test_float_inf(iresult);
        check_float(iresult, FLOAT_MINUS_INF, FLOAT_PLUS_INF);

        
        test_float_zero(iresult);
        check_float(iresult, FLOAT_MINUS_ZERO, 0);

        
        test_float_nan(iresult);
        check_float(iresult, FLOAT_NAN, Float.NaN);

        

        long[] lresult = new long[2];

        
        test_double_inf(lresult);
        check_double(lresult, DOUBLE_MINUS_INF, DOUBLE_PLUS_INF);

        
        test_double_zero(lresult);
        check_double(lresult, DOUBLE_MINUS_ZERO, 0);

        
        test_double_nan(lresult);
        check_double(lresult, DOUBLE_NAN, Double.NaN);
    }
}
