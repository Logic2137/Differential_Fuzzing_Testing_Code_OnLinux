

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

public class LookupHelper {
    private Lookup lookup() {
        return MethodHandles.lookup();
    }
    public static Lookup getLookup() {
        return (new LookupHelper()).lookup();
    }
}
