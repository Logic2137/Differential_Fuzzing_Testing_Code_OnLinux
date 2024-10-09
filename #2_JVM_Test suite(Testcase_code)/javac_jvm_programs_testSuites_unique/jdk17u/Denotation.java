
package nsk.share;

import java.util.*;

abstract public class Denotation {

    abstract public int[] indexFor(String name);

    abstract public String nameFor(int[] index);

    public String nameFor(int i) {
        return nameFor(new int[] { i });
    }

    public String nameFor(int i0, int i1) {
        return nameFor(new int[] { i0, i1 });
    }

    public String nameFor(int i0, int i1, int i2) {
        return nameFor(new int[] { i0, i1, i2 });
    }

    public boolean equality(int[] index1, int[] index2) {
        if (index1 == null || index2 == null)
            return false;
        return Arrays.equals(index1, index2);
    }

    public boolean equivalence(String name1, String name2) {
        if (name1 == null || name2 == null)
            return false;
        return equality(indexFor(name1), indexFor(name2));
    }

    protected Denotation() {
    }
}
