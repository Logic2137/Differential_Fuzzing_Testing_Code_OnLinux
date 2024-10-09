
package pkg1;

import java.util.Collection;
import java.util.Iterator;

public abstract class OverrideOrdering<T> implements Collection<T>, Iterable<T> {

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}
