import java.lang.invoke.*;

public class LongReferenceCastingTest {

    static final String MY_STRING = "myString";

    static final MethodHandle MH;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodType mt = MethodType.methodType(String.class, long.class, Object.class, String.class);
            MH = lookup.findVirtual(LongReferenceCastingTest.class, "myMethod", mt);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public String myMethod(long l, Object o, String s) {
        return o.toString();
    }

    public String toString() {
        return MY_STRING;
    }

    public static void main(String[] args) throws Exception {
        LongReferenceCastingTest test = new LongReferenceCastingTest();
        try {
            for (int i = 0; i < 20_000; ++i) {
                if (!test.invoke().equals(MY_STRING)) {
                    throw new RuntimeException("Invalid string");
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Test failed", t);
        }
    }

    public String invoke() throws Throwable {
        return (String) MH.invokeExact(this, 0L, (Object) this, MY_STRING);
    }
}
