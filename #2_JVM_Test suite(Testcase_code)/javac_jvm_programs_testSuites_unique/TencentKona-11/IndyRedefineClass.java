

package vm.mlvm.indy.func.jvmti.share;

public class IndyRedefineClass {

    public static native void setRedefineTriggerMethodName(String name);
    public static native void setRedefinedClassFileName(String name);
    public static native void setPopFrameDepthAfterRedefine(int depth);

    public static native boolean checkStatus();

    static {
        System.loadLibrary(IndyRedefineClass.class.getSimpleName());
    }
}
