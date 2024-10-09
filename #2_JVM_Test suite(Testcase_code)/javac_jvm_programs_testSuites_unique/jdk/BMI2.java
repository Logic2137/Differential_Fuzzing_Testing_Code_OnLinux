



package compiler.codegen;

public class BMI2 {
    private final static int ITERATIONS = 30000;

    
    public static void testBzhiI2L(int ix) {
        long[] goldv = new long[16];
        for (int i = 0; i <= 15; i++) {
              goldv[i] = BMITests.bzhiI2L(ix, i);
        }
        for (int i2 = 0; i2 < ITERATIONS; i2++) {
             for (int i = 0; i <= 15; i++) {
                  long v = BMITests.bzhiI2L(ix, i);
                  if (v != goldv[i]) {
                          throw new Error(returnBzhiI2LErrMessage (goldv[i], v));
                  }
             }
       }
    }

    private static String returnBzhiI2LErrMessage (long value, long value2) {
        return "bzhi I2L with register failed, uncompiled result: " + value + " does not match compiled result: " + value2;
    }

    static class BMITests {

        static long bzhiI2L(int src1, int src2) {

            switch(src2) {
                case 0:
                    return (long)(src1 & 0x1);
                case 1:
                    return (long)(src1 & 0x3);
                case 2:
                    return (long)(src1 & 0x7);
                case 3:
                    return (long)(src1 & 0xF);
                case 4:
                    return (long)(src1 & 0x1F);
                case 5:
                    return (long)(src1 & 0x3F);
                case 6:
                    return (long)(src1 & 0x7F);
                case 7:
                    return (long)(src1 & 0xFF);
                case 8:
                    return (long)(src1 & 0x1FF);
                case 9:
                    return (long)(src1 & 0x3FF);
                case 10:
                    return (long)(src1 & 0x7FF);
                case 11:
                    return (long)(src1 & 0xFFF);
                case 12:
                    return (long)(src1 & 0x1FFF);
                case 13:
                    return (long)(src1 & 0x3FFF);
                case 14:
                    return (long)(src1 & 0x7FFF);
                case 15:
                    return (long)(src1 & 0xFFFF);
                default:
                    return (long)(src1 & 0xFFFF);
            }
        }
    }

    public static void main(String[] args) {
        testBzhiI2L(0);
        testBzhiI2L(1);
    }
}
