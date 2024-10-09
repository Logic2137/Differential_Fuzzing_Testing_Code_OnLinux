
import java.util.ArrayList;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;


public class ExtendsAbstractList<E> extends AbstractList<E> {

    protected final List<E> list;

    public ExtendsAbstractList() {
        this(ArrayList<E>::new);
    }

    protected ExtendsAbstractList(Supplier<List<E>> supplier) {
        this.list = supplier.get();
    }

    public ExtendsAbstractList(Collection<E> source) {
        this();
        addAll(source);
    }

    public boolean add(E element) {
        return list.add(element);
    }

    public E get(int index) {
        return list.get(index);
    }

    public boolean remove(Object element) {
        return list.remove(element);
    }

    public E set(int index, E element) {
        return list.set(index, element);
    }

    public void add(int index, E element) {
        list.add(index, element);
    }

    public E remove(int index) {
        return list.remove(index);
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Iterator<E> source = list.iterator();

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
        return list.size();
    }
}
