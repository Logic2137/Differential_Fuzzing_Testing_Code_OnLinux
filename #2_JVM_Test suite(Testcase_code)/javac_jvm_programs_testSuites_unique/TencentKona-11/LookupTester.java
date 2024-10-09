

package java.util.concurrent;

import java.lang.invoke.MethodHandles;

public class LookupTester {
    public static MethodHandles.Lookup getLookup() {
        return MethodHandles.lookup();
    }


    public static MethodHandles.Lookup getLookupIn() {
        return MethodHandles.lookup().in(ConcurrentHashMap.class);
    }
}
