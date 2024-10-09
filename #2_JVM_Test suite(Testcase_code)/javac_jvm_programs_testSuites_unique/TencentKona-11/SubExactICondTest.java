



package compiler.intrinsics.mathexact;

public class SubExactICondTest {
  public static int result = 0;

  public static void main(String[] args) {
    for (int i = 0; i < 50000; ++i) {
      runTest();
    }
  }

  public static void runTest() {
    int i = 7;
    while (java.lang.Math.subtractExact(i, result) > -31361) {
        if ((java.lang.Math.subtractExact(i, i) & 1) == 1) {
            i -= 3;
        } else if ((i & 5) == 4) {
            i -= 7;
        } else if ((i & 0xf) == 6) {
            i -= 2;
        } else {
            i -= 1;
        }
        result += 2;
    }
  }
}
