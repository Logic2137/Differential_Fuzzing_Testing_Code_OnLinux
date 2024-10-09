
package nsk.share;

public class NativeUtils {
    static {
        System.loadLibrary("native_utils");
    }

    public static native long getCurrentPID();
}
