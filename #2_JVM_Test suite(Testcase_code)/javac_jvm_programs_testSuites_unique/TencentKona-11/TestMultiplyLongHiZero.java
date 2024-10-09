



package compiler.c2;


public class TestMultiplyLongHiZero {

  private static void check(long leftFactor, long rightFactor, long optimizedProduct, long constantProduct) {
    long normalProduct = leftFactor * rightFactor; 
    if (optimizedProduct != constantProduct || normalProduct != constantProduct) {
      throw new RuntimeException("Not all three products are equal: " +
                                 Long.toHexString(normalProduct) + ", " +
                                 Long.toHexString(optimizedProduct) + ", " +
                                 Long.toHexString(constantProduct));
    }
  }

  private static int initInt(String[] args, int v) {
    if (args.length > 0) {
      try {
        return Integer.valueOf(args[0]);
      } catch (NumberFormatException e) { }
    }
    return v;
  }

  private static final long mask32 = 0x00000000FFFFFFFFL;

  private static void testNormal(int leftFactor, int rightFactor, long constantProduct) {
    check((long) leftFactor,
          (long) rightFactor,
          (long) leftFactor * (long) rightFactor, 
          constantProduct);
  }

  private static void testLeftOptimized(int leftFactor, int rightFactor, long constantProduct) {
    check((leftFactor & mask32),
          (long) rightFactor,
          (leftFactor & mask32) * (long) rightFactor, 
          constantProduct);
  }

  private static void testRightOptimized(int leftFactor, int rightFactor, long constantProduct) {
    check((long) leftFactor,
          (rightFactor & mask32),
          (long) leftFactor * (rightFactor & mask32), 
          constantProduct);
  }

  private static void testOptimized(int leftFactor, int rightFactor, long constantProduct) {
    check((leftFactor & mask32),
          (rightFactor & mask32),
          (leftFactor & mask32) * (rightFactor & mask32), 
          constantProduct);
  }

  private static void testLeftOptimized_LoadUI2L(int leftFactor, int rightFactor, long constantProduct, int[] factors) {
    check((leftFactor & mask32),
          (long) rightFactor,
          (factors[0] & mask32) * (long) rightFactor, 
          constantProduct);
  }

  private static void testRightOptimized_LoadUI2L(int leftFactor, int rightFactor, long constantProduct, int[] factors) {
    check((long) leftFactor,
          (rightFactor & mask32),
          (long) leftFactor * (factors[1] & mask32), 
          constantProduct);
  }

  private static void testOptimized_LoadUI2L(int leftFactor, int rightFactor, long constantProduct, int[] factors) {
    check((leftFactor & mask32),
          (rightFactor & mask32),
          (factors[0] & mask32) * (factors[1] & mask32), 
          constantProduct);
  }

  private static void test(int leftFactor, int rightFactor,
                           long normalConstantProduct,
                           long leftOptimizedConstantProduct,
                           long rightOptimizedConstantProduct,
                           long optimizedConstantProduct) {
    int[] factors = new int[2];
    factors[0] = leftFactor;
    factors[1] = rightFactor;
    testNormal(leftFactor, rightFactor, normalConstantProduct);
    testLeftOptimized(leftFactor, rightFactor, leftOptimizedConstantProduct);
    testRightOptimized(leftFactor, rightFactor, rightOptimizedConstantProduct);
    testOptimized(leftFactor, rightFactor, optimizedConstantProduct);
    testLeftOptimized_LoadUI2L(leftFactor, rightFactor, leftOptimizedConstantProduct, factors);
    testRightOptimized_LoadUI2L(leftFactor, rightFactor, rightOptimizedConstantProduct, factors);
    testOptimized_LoadUI2L(leftFactor, rightFactor, optimizedConstantProduct, factors);
  }

  public static void main(String[] args) {
    for (int i = 0; i < 100000; ++i) { 
      int i0 = initInt(args, 1);
      int i1 = initInt(args, 3);
      int i2 = initInt(args, -1);
      int i3 = initInt(args, 0x7FFFFFFF);
      test(i0, i1, 3L, 3L, 3L, 3L);
      test(i0, i2, -1L, -1L, 0xFFFFFFFFL, 0xFFFFFFFFL);
      test(i0, i3, 0x7FFFFFFFL, 0x7FFFFFFFL, 0x7FFFFFFFL, 0x7FFFFFFFL);
      test(i1, i2, -3L, -3L, 0x2FFFFFFFDL, 0x2FFFFFFFDL);
      test(i1, i3, 0x17FFFFFFDL, 0x17FFFFFFDL, 0x17FFFFFFDL, 0x17FFFFFFDL);
      test(i2, i3, 0xFFFFFFFF80000001L, 0x7FFFFFFE80000001L,
           0xFFFFFFFF80000001L, 0x7FFFFFFE80000001L);
    }
  }
}
