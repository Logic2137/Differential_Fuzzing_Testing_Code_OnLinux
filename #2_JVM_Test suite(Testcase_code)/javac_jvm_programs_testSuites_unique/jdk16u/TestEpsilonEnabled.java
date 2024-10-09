

package gc.epsilon;



import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

public class TestEpsilonEnabled {
  public static void main(String[] args) throws Exception {
    if (!isEpsilonEnabled()) {
      throw new IllegalStateException("Debug builds should have Epsilon enabled");
    }
  }

  public static boolean isEpsilonEnabled() {
    for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
      if (bean.getName().contains("Epsilon")) {
        return true;
      }
    }
    return false;
  }
}
