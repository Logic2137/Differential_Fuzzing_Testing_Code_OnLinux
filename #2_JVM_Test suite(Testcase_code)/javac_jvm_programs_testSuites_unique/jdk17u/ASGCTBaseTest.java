
package MyPackage;

public class ASGCTBaseTest {

    static {
        try {
            System.loadLibrary("AsyncGetCallTraceTest");
        } catch (UnsatisfiedLinkError ule) {
            System.err.println("Could not load AsyncGetCallTrace library");
            System.err.println("java.library.path: " + System.getProperty("java.library.path"));
            throw ule;
        }
    }

    private static native boolean checkAsyncGetCallTraceCall();

    public static void main(String[] args) {
        if (!checkAsyncGetCallTraceCall()) {
            throw new RuntimeException("AsyncGetCallTrace call failed.");
        }
    }
}
