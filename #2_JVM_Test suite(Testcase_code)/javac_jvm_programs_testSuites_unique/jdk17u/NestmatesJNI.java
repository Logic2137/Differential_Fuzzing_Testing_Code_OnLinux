public class NestmatesJNI {

    static {
        System.loadLibrary("NestmatesJNI");
    }

    public static native void callVoidVoid(Object target, String definingClassName, String methodName, boolean virtual);

    public static native String callStringVoid(Object target, String definingClassName, String methodName, boolean virtual);

    public static native void callStaticVoidVoid(String definingClassName, String methodName);

    public static Object newInstance(String definingClassName, String sig, Object outerThis) {
        return newInstance0(definingClassName, "<init>", sig, outerThis);
    }

    private static native Object newInstance0(String definingClassName, String method_name, String sig, Object outerThis);

    public static native int getIntField(Object target, String definingClassName, String fieldName);

    public static native void setIntField(Object target, String definingClassName, String fieldName, int newVal);

    public static native int getStaticIntField(String definingClassName, String fieldName);

    public static native void setStaticIntField(String definingClassName, String fieldName, int newVal);
}
