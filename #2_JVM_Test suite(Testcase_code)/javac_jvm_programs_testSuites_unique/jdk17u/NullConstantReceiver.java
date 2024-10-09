
package compiler.jsr292;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class NullConstantReceiver {

    static final MethodHandle target;

    static {
        try {
            target = MethodHandles.lookup().findVirtual(NullConstantReceiver.class, "test", MethodType.methodType(void.class));
        } catch (ReflectiveOperationException e) {
            throw new Error(e);
        }
    }

    public void test() {
    }

    static void run() throws Throwable {
        target.invokeExact((NullConstantReceiver) null);
    }

    public static void main(String[] args) throws Throwable {
        for (int i = 0; i < 15000; i++) {
            try {
                run();
            } catch (NullPointerException e) {
                continue;
            }
            throw new AssertionError("NPE wasn't thrown");
        }
        System.out.println("TEST PASSED");
    }
}
