
package nsk.share.test;

import java.util.Arrays;

public class LazyObjectArrayToString {

    private Object[] array;

    public LazyObjectArrayToString(Object[] array) {
        this.array = array;
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }
}
