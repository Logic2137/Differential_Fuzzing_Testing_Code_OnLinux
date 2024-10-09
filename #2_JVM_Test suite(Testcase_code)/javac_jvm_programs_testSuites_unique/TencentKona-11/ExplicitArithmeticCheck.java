



public class ExplicitArithmeticCheck {
    public static void main(String argv[]) throws Exception {
        for (int i = 0; i < 64; i++) {
          boolean result = false;
          int n;
          try {
              n = 0 / 0;
          } catch (ArithmeticException e) {
              result = true;
          }
          if (result == false) {
            throw new Error("Failed to throw correct exception!");
          }
          result = false;
          try {
              n = 0 % 0;
          } catch (ArithmeticException e) {
              result = true;
          }
          if (result == false) {
            throw new Error("Failed to throw correct exception!");
          }
          try {
              n = 0x80000000 / -1;
          } catch (Throwable t) {
            throw new Error("Failed to throw correct exception!");
          }
          if (n != 0x80000000) {
            throw new Error("Incorrect integer arithmetic ! ");
          }
          try {
              n = 0x80000000 % -1;
          } catch (Throwable t) {
            throw new Error("Failed to throw correct exception!");
          }
          if (n != 0) {
            throw new Error("Incorrect integer arithmetic!");
          }
        }
    }
}
