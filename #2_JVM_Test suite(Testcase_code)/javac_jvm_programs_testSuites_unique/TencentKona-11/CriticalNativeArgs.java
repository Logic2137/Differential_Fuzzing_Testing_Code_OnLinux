public class CriticalNativeArgs {

    static {
        System.loadLibrary("CriticalNative");
    }

    static native boolean isNull(int[] a);

    public static void main(String[] args) {
        int[] arr = new int[2];
        if (isNull(arr)) {
            throw new RuntimeException("Should not be null");
        }
        if (!isNull(null)) {
            throw new RuntimeException("Should be null");
        }
    }
}
