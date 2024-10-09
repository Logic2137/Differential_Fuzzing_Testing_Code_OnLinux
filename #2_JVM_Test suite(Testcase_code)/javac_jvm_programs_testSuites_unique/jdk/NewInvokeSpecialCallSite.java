

package java.lang.invoke;


public final class NewInvokeSpecialCallSite extends CallSite {

    private static MethodHandle mh;

    
    public static void setMH(MethodHandle newMH) {
        mh = newMH;
    }

    
    public NewInvokeSpecialCallSite(MethodHandles.Lookup lookup, String name, MethodType type) {
        super(mh);
    }

    
    public final void setTarget(MethodHandle newMH) {
        
    }

    
    public final MethodHandle getTarget() {
        return mh;
    }

    public final MethodHandle dynamicInvoker() {
        return makeDynamicInvoker();
    }
}
