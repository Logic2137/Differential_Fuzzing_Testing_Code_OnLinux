

package java.lang.invoke;

import java.lang.invoke.MethodHandles.Lookup;



public class MethodHandleHelper {

    private MethodHandleHelper() { }

    public static final Lookup IMPL_LOOKUP = Lookup.IMPL_LOOKUP;

    public static void customize(MethodHandle mh) {
        mh.customize();
    }

    public static MethodHandle varargsArray(int nargs) {
        return MethodHandleImpl.varargsArray(nargs);
    }

    public static MethodHandle varargsArray(Class<?> arrayType, int nargs) {
        return MethodHandleImpl.varargsArray(arrayType, nargs);
    }

}

