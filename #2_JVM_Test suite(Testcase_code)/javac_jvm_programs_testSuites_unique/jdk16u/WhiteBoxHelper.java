

package vm.mlvm.share;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;


public class WhiteBoxHelper {

    
    public static MethodHandle getMethod(String name, MethodType type)
            throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, InvocationTargetException {

        Class<?> wbClass = Class.forName("sun.hotspot.WhiteBox");
        MethodHandles.Lookup lu = MethodHandles.lookup();
        MethodHandle getWB = lu.findStatic(wbClass, "getWhiteBox", MethodType.methodType(wbClass));
        Object wb;
        try {
            wb = getWB.invoke();
        } catch (Throwable e) {
            throw new InvocationTargetException(e, "Can't obtain WhiteBox instance");
        }
        return lu.findVirtual(wbClass, name, type).bindTo(wb);
    }

}
