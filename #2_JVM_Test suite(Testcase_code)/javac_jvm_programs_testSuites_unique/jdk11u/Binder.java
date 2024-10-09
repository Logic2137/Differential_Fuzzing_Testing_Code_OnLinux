
package nsk.jvmti.SetNativeMethodPrefix;

public class Binder {
    static { System.loadLibrary("SetNativeMethodPrefix001"); }

    
    public final static int FUNCTION_FOO = 0;
    public final static int FUNCTION_WRAPPED_FOO = 1;

    
    static public final int FOO_RETURN = 1;
    static public final int WRAPPED_FOO_RETURN = 2;

    native static public boolean setMethodPrefix (String prefix);
    native static public boolean setMultiplePrefixes (String prefix);
    native static public boolean registerMethod (Class klass, String methodName, String methodSig, int i);
}
