



package compiler.jsr292;


import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class TestArrayReturnType {

    static final MethodHandle mh;
    static int[] testArray = new int[1];
    static {
        try {
            mh = MethodHandles.lookup().findStatic(TestArrayReturnType.class, "testArrayReturnType", MethodType.methodType(int[].class));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    static int[] testArrayReturnType() {
        return testArray;
    }

    public static void test()  throws Throwable {
        int a[] = (int[])mh.invokeExact();
        for (int i=0; i<a.length; i++) {
            a[i] = 1;
        }
    }

    public static void main(String[] args) throws Throwable {
        for (int i=0; i<15000; i++) {
            test();
        }
        System.out.println("TEST PASSED");
    }
}
