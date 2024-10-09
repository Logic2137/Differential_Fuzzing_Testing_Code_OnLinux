

package gc;

public class CriticalNative {
    static {
        System.loadLibrary("CriticalNative");
    }

    public static native boolean isNull(int[] a);
    public static native long sum1(long[] a);
    
    public static native long sum2(long a1, int[] a2, int[] a3, long[] a4, int[] a5);
}
