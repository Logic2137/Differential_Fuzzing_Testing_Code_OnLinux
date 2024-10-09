
package test.java.lang.invoke.t8150782;

import static java.lang.invoke.MethodHandles.*;

public class TestCls {

    public static final Lookup LOOKUP = lookup();

    private static class PrivateSIC {
    }

    public static Class getPrivateSIC() {
        return PrivateSIC.class;
    }

    public static Lookup getLookupForPrivateSIC() {
        return lookup();
    }
}
