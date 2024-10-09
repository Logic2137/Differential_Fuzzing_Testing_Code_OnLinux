





package compiler.runtime.cr8015436;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class Test8015436 implements InterfaceWithDefaultMethod {
    public static final String SOME_MTD_INVOKED = "someMethod() invoked";
    public static final String DEFAULT_MTD_INVOKED_DIRECTLY = "defaultMethod() invoked directly";
    public static final String DEFAULT_MTD_INVOKED_MH = "defaultMethod() invoked via a MethodHandle";

    @Override
    public void someMethod() {
        System.out.println(SOME_MTD_INVOKED);
    }

    public static void main(String[] args) throws Throwable {
        Test8015436 testObj = new Test8015436();
        testObj.someMethod();
        testObj.defaultMethod(DEFAULT_MTD_INVOKED_DIRECTLY);

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType   mt = MethodType.methodType(void.class, String.class);
        MethodHandle mh = lookup.findVirtual(Test8015436.class, "defaultMethod", mt);
        mh.invokeExact(testObj, DEFAULT_MTD_INVOKED_MH);
    }
}

interface InterfaceWithDefaultMethod {
    public void someMethod();

    default public void defaultMethod(String str){
        System.out.println(str);
    }
}

