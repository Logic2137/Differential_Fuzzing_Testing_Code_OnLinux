


public class StackMapFrameTest {

  public static void foo() {
    Object o = new Object();
  }

  public static void main(String args[]) {
    for (int i = 0; i < 25000; i++) {
      foo();
    }
  }
}
