import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

public class ExtendsAbstractCollection<E> extends AbstractCollection<E> {

    protected final Collection<E> coll;

    public ExtendsAbstractCollection() {
        this(ArrayList<E>::new);
    }

    public ExtendsAbstractCollection(Collection<E> source) {
        this();
        coll.addAll(source);
    }

    protected ExtendsAbstractCollection(Supplier<Collection<E>> backer) {
        this.coll = backer.get();
    }

    public boolean add(E element) {
        return coll.add(element);
    }

    public boolean remove(Object element) {
        return coll.remove(element);
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {

            Iterator<E> source = coll.iterator();

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
        return coll.size();
    }
}
