
package compiler.codecache;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class TestStressCodeBuffers {

    static MethodHandle permh;

    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test() throws Exception {
        MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        MethodHandle mh = lookup.findStatic(TestStressCodeBuffers.class, "bar", MethodType.methodType(void.class, int.class, long.class));
        permh = MethodHandles.permuteArguments(mh, mh.type(), 0, 1);
    }

    public static void bar(int x, long y) {
    }
}
