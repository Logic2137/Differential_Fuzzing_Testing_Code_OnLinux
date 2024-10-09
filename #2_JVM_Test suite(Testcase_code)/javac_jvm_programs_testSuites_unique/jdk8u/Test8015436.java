import java.lang.invoke.*;

interface InterfaceWithDefaultMethod {

    public void someMethod();

    default public void defaultMethod(String str) {
        System.out.println("defaultMethod() " + str);
    }
}

public class Test8015436 implements InterfaceWithDefaultMethod {

    @Override
    public void someMethod() {
        System.out.println("someMethod() invoked");
    }

    public static void main(String[] args) throws Throwable {
        Test8015436 testObj = new Test8015436();
        testObj.someMethod();
        testObj.defaultMethod("invoked directly");
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType mt = MethodType.methodType(void.class, String.class);
        MethodHandle mh = lookup.findVirtual(Test8015436.class, "defaultMethod", mt);
        mh.invokeExact(testObj, "invoked via a MethodHandle");
    }
}
