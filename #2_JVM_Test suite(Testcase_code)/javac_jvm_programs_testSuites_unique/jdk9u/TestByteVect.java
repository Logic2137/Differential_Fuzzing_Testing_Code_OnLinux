



package compiler.c2.cr7192963;

public class TestByteVect {
  private static final int ARRLEN = 997;
  private static final int ITERS  = 11000;
  public static void main(String args[]) {
    System.out.println("Testing Byte vectors");
    int errn = test();
    if (errn > 0) {
      System.err.println("FAILED: " + errn + " errors");
      System.exit(97);
    }
    System.out.println("PASSED");
  }

  static int test() {
    byte[] a0 = new byte[ARRLEN];
    byte[] a1 = new byte[ARRLEN];
    
    for (int i=0; i<ARRLEN; i++) {
      a1[i] = (byte)i;
    }
    System.out.println("Warmup");
    for (int i=0; i<ITERS; i++) {
      test_init(a0);
      test_addi(a0, a1);
      test_lsai(a0, a1);
      test_unrl_init(a0);
      test_unrl_addi(a0, a1);
      test_unrl_lsai(a0, a1);
    }
    
    System.out.println("Verification");
    int errn = 0;
    {
      test_init(a0);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_init: ", i, a0[i], (byte)(i&3));
      }
      test_addi(a0, a1);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_addi: ", i, a0[i], (byte)(i+(i&3)));
      }
      test_lsai(a0, a1);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_lsai: ", i, a0[i], (byte)(i<<(i&3)));
      }
      test_unrl_init(a0);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_unrl_init: ", i, a0[i], (byte)(i&3));
      }
      test_unrl_addi(a0, a1);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_unrl_addi: ", i, a0[i], (byte)(i+(i&3)));
      }
      test_unrl_lsai(a0, a1);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_unrl_lsai: ", i, a0[i], (byte)(i<<(i&3)));
      }
    }

    if (errn > 0)
      return errn;

    System.out.println("Time");
    long start, end;

    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_init(a0);
    }
    end = System.currentTimeMillis();
    System.out.println("test_init: " + (end - start));

    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_addi(a0, a1);
    }
    end = System.currentTimeMillis();
    System.out.println("test_addi: " + (end - start));

    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_lsai(a0, a1);
    }
    end = System.currentTimeMillis();
    System.out.println("test_lsai: " + (end - start));

    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_unrl_init(a0);
    }
    end = System.currentTimeMillis();
    System.out.println("test_unrl_init: " + (end - start));

    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_unrl_addi(a0, a1);
    }
    end = System.currentTimeMillis();
    System.out.println("test_unrl_addi: " + (end - start));

    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_unrl_lsai(a0, a1);
    }
    end = System.currentTimeMillis();
    System.out.println("test_unrl_lsai: " + (end - start));

    return errn;
  }

  static void test_init(byte[] a0) {
    for (int i = 0; i < a0.length; i+=1) {
      a0[i] = (byte)(i&3);
    }
  }
  static void test_addi(byte[] a0, byte[] a1) {
    for (int i = 0; i < a0.length; i+=1) {
      a0[i] = (byte)(a1[i]+(i&3));
    }
  }
  static void test_lsai(byte[] a0, byte[] a1) {
    for (int i = 0; i < a0.length; i+=1) {
      a0[i] = (byte)(a1[i]<<(i&3));
    }
  }
  static void test_unrl_init(byte[] a0) {
    int i = 0;
    for (; i < a0.length-4; i+=4) {
      a0[i+0] = 0;
      a0[i+1] = 1;
      a0[i+2] = 2;
      a0[i+3] = 3;
    }
    for (; i < a0.length; i++) {
      a0[i] = (byte)(i&3);
    }
  }
  static void test_unrl_addi(byte[] a0, byte[] a1) {
    int i = 0;
    for (; i < a0.length-4; i+=4) {
      a0[i+0] = (byte)(a1[i+0]+0);
      a0[i+1] = (byte)(a1[i+1]+1);
      a0[i+2] = (byte)(a1[i+2]+2);
      a0[i+3] = (byte)(a1[i+3]+3);
    }
    for (; i < a0.length; i++) {
      a0[i] = (byte)(a1[i]+(i&3));
    }
  }
  static void test_unrl_lsai(byte[] a0, byte[] a1) {
    int i = 0;
    for (; i < a0.length-4; i+=4) {
      a0[i+0] = (byte)(a1[i+0]<<0);
      a0[i+1] = (byte)(a1[i+1]<<1);
      a0[i+2] = (byte)(a1[i+2]<<2);
      a0[i+3] = (byte)(a1[i+3]<<3);
    }
    for (; i < a0.length; i++) {
      a0[i] = (byte)(a1[i]<<(i&3));
    }
  }

  static int verify(String text, int i, byte elem, byte val) {
    if (elem != val) {
      System.err.println(text + "[" + i + "] = " + elem + " != " + val);
      return 1;
    }
    return 0;
  }
}

