

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;



public class TestPrivateMember {
    public static void main(String... args) throws Throwable {
        System.setSecurityManager(new SecurityManager());
        TestPrivateMember t = new TestPrivateMember();
        t.test();
    }

    public TestPrivateMember() {
    }

    public void test() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType mt = MethodType.methodType(void.class);
        try {
            Class<?> checkInittedHolder = TestPrivateMemberPackageSibling.class;
            
            
            MethodHandle mh = lookup.findStatic(checkInittedHolder, "checkInitted", mt);
            throw new RuntimeException("IllegalAccessException not thrown");
        } catch (IllegalAccessException e) {
            
            System.out.println("Expected exception: " + e.getMessage());
        }
    }
}

class TestPrivateMemberPackageSibling {
    private static void checkInitted() { }
}
