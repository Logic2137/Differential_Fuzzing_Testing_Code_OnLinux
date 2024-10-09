

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.lang.StackWalker.StackFrame;

public class LocalLongHelper {
    static StackWalker sw;
    static Method longValue;
    static Method getLocals;
    static Class<?> primitiveValueClass;
    static Method primitiveSize;
    static Method getMethodType;
    static Field memberName;
    static Field offset;

    public static void main(String[] args) throws Throwable {
        setupReflectionStatics();
        new LocalLongHelper().longArg(0xC0FFEE, 0x1234567890ABCDEFL);
    }

    
    public long longArg(int i, long l) throws Throwable {
        List<StackFrame> frames = sw.walk(s -> s.collect(Collectors.toList()));
        Object[] locals = (Object[]) getLocals.invoke(frames.get(0));

        if (8 == (int) primitiveSize.invoke(locals[2])) { 
            long locals_2 = (long) longValue.invoke(locals[2]);
            if (locals_2 != 0){
                throw new RuntimeException("Expected locals_2 == 0");
            }
        }
        return l; 
    }

    private static void setupReflectionStatics() throws Throwable {
        Class<?> liveStackFrameClass = Class.forName("java.lang.LiveStackFrame");
        primitiveValueClass = Class.forName("java.lang.LiveStackFrame$PrimitiveSlot");

        getLocals = liveStackFrameClass.getDeclaredMethod("getLocals");
        getLocals.setAccessible(true);

        longValue = primitiveValueClass.getDeclaredMethod("longValue");
        longValue.setAccessible(true);

        Class<?> stackFrameInfoClass = Class.forName("java.lang.StackFrameInfo");
        memberName = stackFrameInfoClass.getDeclaredField("memberName");
        memberName.setAccessible(true);
        offset = stackFrameInfoClass.getDeclaredField("bci");
        offset.setAccessible(true);
        getMethodType = Class.forName("java.lang.invoke.MemberName").getDeclaredMethod("getMethodType");
        getMethodType.setAccessible(true);

        Class<?> extendedOptionClass = Class.forName("java.lang.StackWalker$ExtendedOption");
        Method ewsNI = StackWalker.class.getDeclaredMethod("newInstance", Set.class, extendedOptionClass);
        ewsNI.setAccessible(true);
        Field f = extendedOptionClass.getDeclaredField("LOCALS_AND_OPERANDS");
        f.setAccessible(true);
        Object localsAndOperandsOption = f.get(null);

        primitiveSize = primitiveValueClass.getDeclaredMethod("size");
        primitiveSize.setAccessible(true);
        sw = (StackWalker) ewsNI.invoke(null, java.util.Collections.emptySet(), localsAndOperandsOption);
    }
}
