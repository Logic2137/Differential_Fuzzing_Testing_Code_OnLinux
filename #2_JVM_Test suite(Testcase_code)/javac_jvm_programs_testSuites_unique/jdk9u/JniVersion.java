

 
public class JniVersion {

    public static final int JNI_VERSION_9 = 0x00090000;

    public static void main(String... args) throws Exception {
        System.loadLibrary("JniVersion");
        int res = getJniVersion();
        if (res != JNI_VERSION_9) {
            throw new Exception("Unexpected value returned from getJniVersion(): 0x" + Integer.toHexString(res));
        }
    }

    static native int getJniVersion();
}
