

package MyPackage;



public class HeapMonitorNoCapabilityTest {
  private native static int allSamplingMethodsFail();

  public static void main(String[] args) {
    int result = allSamplingMethodsFail();

    if (result == 0) {
      throw new RuntimeException("Some methods could be called without a capability.");
    }
  }
}
