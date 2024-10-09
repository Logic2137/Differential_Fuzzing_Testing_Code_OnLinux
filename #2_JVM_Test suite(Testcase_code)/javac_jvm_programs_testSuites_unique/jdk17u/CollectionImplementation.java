import java.util.Collection;

public interface CollectionImplementation {

    public Class<?> klazz();

    public Collection emptyCollection();

    public Object makeElement(int i);

    public boolean isConcurrent();

    public boolean permitsNulls();
}
