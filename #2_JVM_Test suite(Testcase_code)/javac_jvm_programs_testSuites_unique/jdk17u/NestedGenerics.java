
package pkg;

import java.util.Map;

public class NestedGenerics {

    public static <A> void foo(Map<A, Map<A, A>> map) {
    }
}
