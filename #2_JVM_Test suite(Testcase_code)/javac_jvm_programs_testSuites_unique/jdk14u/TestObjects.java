

package gc.epsilon;



import java.util.Random;

public class TestObjects {

  static long SEED = Long.getLong("seed", System.nanoTime());
  static int COUNT = Integer.getInteger("count", 1_000_000); 

  static MyObject[] arr;

  public static void main(String[] args) throws Exception {
    Random r = new Random(SEED);

    arr = new MyObject[COUNT];
    for (int c = 0; c < COUNT; c++) {
      arr[c] = new MyObject(r.nextInt());
    }

    r = new Random(SEED);
    for (int c = 0; c < COUNT; c++) {
      int expected = r.nextInt();
      int actual = arr[c].id();
      if (expected != actual) {
        throw new IllegalStateException("Failure: expected = " + expected + ", actual = " + actual);
      }
    }
  }

  public static class MyObject {
    int id;
    public MyObject(int id) {
      this.id = id;
    }
    public int id() {
      return id;
    }
  }
}
