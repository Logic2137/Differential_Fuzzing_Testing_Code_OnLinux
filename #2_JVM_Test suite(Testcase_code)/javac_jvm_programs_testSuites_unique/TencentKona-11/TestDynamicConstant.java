



import java.lang.invoke.*;

public class TestDynamicConstant {
    static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    static final MethodType TEST_MT = MethodType.methodType(void.class);
    static final Class<?> TEST_CLASS;

    static {
       try {
          TEST_CLASS = Class.forName("DynamicConstantHelper");
       } catch (ClassNotFoundException e) {
           throw new Error(e);
       }
    }

    static void test(String testName, Class<? extends Throwable> expectedError) {
        try {
            LOOKUP.findStatic(TEST_CLASS, testName, TEST_MT).invokeExact();
        } catch (Throwable e) {
            if (expectedError.isInstance(e)) {
                
            } else {
                e.printStackTrace();
                String msg = String.format("%s: wrong exception: %s, but %s expected",
                                           testName, e.getClass().getName(), expectedError.getName());
                throw new AssertionError(msg);
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        test("testDoubleConstantDynamic", NoSuchMethodError.class);
        test("testIntConstantDynamic",  NoSuchMethodError.class);
    }
}
