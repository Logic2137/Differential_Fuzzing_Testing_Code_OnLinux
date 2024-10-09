
package test.java.lang.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class ObjectMethodInInterfaceTest {

    public static void main(String[] args) throws Throwable {
        MethodHandle mh = MethodHandles.lookup().findVirtual(CharSequence.class, "toString", MethodType.methodType(String.class));
        MethodType mt = MethodType.methodType(Object.class, CharSequence.class);
        mh = mh.asType(mt);
        Object res = mh.invokeExact((CharSequence) "123");
        System.out.println("TEST PASSED");
    }
}
