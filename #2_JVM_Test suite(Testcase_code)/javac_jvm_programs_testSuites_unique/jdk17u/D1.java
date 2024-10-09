
package d1;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

public class D1 {

    public D1() {
    }

    public static Lookup lookup() {
        return MethodHandles.lookup();
    }
}
