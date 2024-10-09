

package pkg;

import java.util.*;

public class MultiTypeParameters {

    public <T extends Number & Runnable> T foo(T t) {
        return null;
    }

}
