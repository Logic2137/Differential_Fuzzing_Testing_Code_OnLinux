



import java.util.concurrent.atomic.*;

public class TestManyThreads {

  static int COUNT = Integer.getInteger("count", 128);  

  static volatile Object sink;
  static volatile Throwable failed;
  static final AtomicInteger allocated = new AtomicInteger();

  public static void workload() {
    try {
      sink = new Object();
      allocated.incrementAndGet();
      Thread.sleep(3600 * 1000);
    } catch (Throwable e) {
      failed = e;
    }
  }

  public static void main(String[] args) throws Throwable {
    for (int c = 0; c < COUNT; c++) {
      Thread t = new Thread(TestManyThreads::workload);
      t.setDaemon(true);
      t.start();
    }

    while ((failed == null) && (allocated.get() != COUNT)) {
        Thread.sleep(100);
    }

    if (failed != null) {
      throw failed;
    }
  }

}
