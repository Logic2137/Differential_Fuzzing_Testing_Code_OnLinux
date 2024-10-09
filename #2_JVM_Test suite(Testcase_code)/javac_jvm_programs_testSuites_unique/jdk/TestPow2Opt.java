



public class TestPow2Opt {

  static void test(double a) {
    double r1 = a * a;
    double r2 = Math.pow(a, 2.0);
    if (r1 != r2) {
      throw new RuntimeException("pow(" + a + ", 2.0), expected: " + r1 + ", actual: " + r2);
    }
  }

  public static void main(String[] args) throws Exception {
    for (int i = 0; i < 10; i++) {
      for (int j = 1; j < 100000; j++) {
        test(j * 1.0);
        test(1.0 / j);
      }
    }
  }

}
