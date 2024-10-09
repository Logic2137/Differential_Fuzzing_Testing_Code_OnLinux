



package compiler.tiered;

public class TieredModesTest {
    public static int sideEffect = 0;
    private static void test() {
      sideEffect++;
    }
    public static void main(String... args) {
      for (int i = 0; i < 100_000; i++) {
        test();
      }
    }
}
