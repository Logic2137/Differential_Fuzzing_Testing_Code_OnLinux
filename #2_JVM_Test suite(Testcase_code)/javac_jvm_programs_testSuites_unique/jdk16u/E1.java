
package e1;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

public class E1 {
    public E1() { }

    public static Lookup lookup() {
        return MethodHandles.lookup();
    }
}
