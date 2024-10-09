
import java.util.HashSet;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;


public class ExtendsAbstractSet<E> extends AbstractSet<E> {

    protected final Set<E> set;

    public ExtendsAbstractSet() {
        this(HashSet<E>::new);
    }

    public ExtendsAbstractSet(Collection<E> source) {
        this();
        addAll(source);
    }

    protected ExtendsAbstractSet(Supplier<Set<E>> backer) {
        this.set = backer.get();
    }

    public boolean add(E element) {
        return set.add(element);
    }

    public boolean remove(Object element) {
        return set.remove(element);
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Iterator<E> source = set.iterator();

            public boolean hasNext() {
                return source.hasNext();
            }

            public E next() {
                return source.next();
            }

            public void remove() {
                source.remove();
            }
        };
    }

    public int size() {
        return set.size();
    }
}
