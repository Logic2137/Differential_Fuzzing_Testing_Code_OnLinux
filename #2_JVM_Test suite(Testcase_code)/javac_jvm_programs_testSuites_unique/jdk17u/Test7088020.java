
package compiler.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.WrongMethodTypeException;

public class Test7088020 {

    public static boolean test() {
        return false;
    }

    public static void main(String... args) throws Throwable {
        MethodHandle test = MethodHandles.lookup().findStatic(Test7088020.class, "test", MethodType.methodType(Boolean.TYPE));
        int thrown = 0;
        try {
            test.invokeExact(0);
        } catch (WrongMethodTypeException wmt) {
            thrown++;
            if (wmt.getStackTrace().length < 1)
                throw new InternalError("missing stack frames");
        }
        try {
            test.invokeExact(0, 1);
        } catch (WrongMethodTypeException wmt) {
            thrown++;
            if (wmt.getStackTrace().length < 1)
                throw new InternalError("missing stack frames");
        }
        try {
            test.invokeExact(0, 1, 2);
        } catch (WrongMethodTypeException wmt) {
            thrown++;
            if (wmt.getStackTrace().length < 1)
                throw new InternalError("missing stack frames");
        }
        try {
            test.invokeExact(0, 1, 2, 3);
        } catch (WrongMethodTypeException wmt) {
            thrown++;
            if (wmt.getStackTrace().length < 1)
                throw new InternalError("missing stack frames");
        }
        try {
            thrown++;
            test.invokeExact(0, 1, 2, 3, 4);
        } catch (WrongMethodTypeException wmt) {
            if (wmt.getStackTrace().length < 1)
                throw new InternalError("missing stack frames");
        }
        if (thrown != 5) {
            throw new InternalError("not enough throws");
        }
    }
}
