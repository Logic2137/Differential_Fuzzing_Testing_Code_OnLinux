
package MyPackage;

public class HeapMonitorIllegalArgumentTest {

    private native static int testIllegalArgument();

    public static void main(String[] args) {
        int result = testIllegalArgument();
        if (result == 0) {
            throw new RuntimeException("Test illegal argument failed.");
        }
    }
}
