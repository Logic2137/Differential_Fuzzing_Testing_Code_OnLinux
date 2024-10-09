



public class GVNTest {
  public static int result = 0;
  public static int value = 93;
  public static void main(String[] args) {
    for (int i = 0; i < 50000; ++i) {
      result = runTest(value + i);
      result = runTest(value + i);
      result = runTest(value + i);
      result = runTest(value + i);
      result = runTest(value + i);
    }
  }

  public static int runTest(int value) {
    int v = value + value;
    int sum = 0;
    if (v < 4032) {
      for (int i = 0; i < 1023; ++i) {
        sum += Math.addExact(value, value);
      }
    } else {
      for (int i = 0; i < 321; ++i) {
        sum += Math.addExact(value, value);
      }
    }
    return sum + v;
  }
}
