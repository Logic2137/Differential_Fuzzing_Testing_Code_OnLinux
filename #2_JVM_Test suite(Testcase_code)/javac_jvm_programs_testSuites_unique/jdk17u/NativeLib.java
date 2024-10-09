public class NativeLib {

    public static void main(String... args) throws Exception {
        System.loadLibrary("sanity_SimpleNativeLib2");
        int res = nativeFunc();
        if (res != 4712) {
            throw new Exception("Wrong value returned from native code: " + res);
        }
    }

    static native int nativeFunc();
}
