import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import static java.lang.invoke.MethodType.*;

public class ReflectiveLookupTest {

    public static void main(String... args) throws Throwable {
        Lookup lookup1 = MethodHandles.lookup();
        MethodHandle mh1 = lookup1.findStatic(lookup1.lookupClass(), "foo", methodType(String.class));
        assertEquals((String) mh1.invokeExact(), foo());
        Method lookupMethod = MethodHandles.class.getMethod("lookup");
        System.out.println("reflection method: " + lookupMethod);
        if (!lookupMethod.getName().equals("lookup")) {
            throw new RuntimeException("Unexpected name: " + lookupMethod.getName());
        }
        Lookup lookup2 = (Lookup) lookupMethod.invoke(null);
        assertEquals(lookup1.lookupClass(), lookup2.lookupClass());
        assertEquals(lookup1.lookupModes(), lookup2.lookupModes());
        MethodHandle mh2 = lookup2.findStatic(lookup2.lookupClass(), "foo", methodType(String.class));
        assertEquals((String) mh2.invokeExact(), foo());
    }

    static String foo() {
        return "foo!";
    }

    static void assertEquals(Object o1, Object o2) {
        if (!o1.equals(o2)) {
            throw new RuntimeException(o1 + " != " + o2);
        }
    }
}
