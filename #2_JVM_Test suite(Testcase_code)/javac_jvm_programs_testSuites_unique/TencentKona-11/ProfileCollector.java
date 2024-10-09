

package nsk.share.jvmti;



public class ProfileCollector {

    static int callCount = 0;
    static int allocCount = 0;

    public static void reset() {
        callCount = 0;
        allocCount = 0;
    }

    public static void callTracker() {
        ++callCount;
    }

    public static void allocTracker() {
        ++allocCount;
    }
}
