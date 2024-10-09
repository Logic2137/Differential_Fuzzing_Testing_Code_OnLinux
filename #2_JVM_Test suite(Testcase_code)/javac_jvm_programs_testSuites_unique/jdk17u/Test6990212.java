
package compiler.jsr292.cr6990212;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

interface intf {

    public Object target();
}

public class Test6990212 implements intf {

    public Object target() {
        return null;
    }

    public static void main(String[] args) throws Throwable {
        MethodHandle target = MethodHandles.lookup().findVirtual(intf.class, "target", MethodType.methodType(Object.class));
        try {
            target.invoke(new Object());
        } catch (ClassCastException cce) {
            System.out.println("got expected ClassCastException");
        }
    }
}
