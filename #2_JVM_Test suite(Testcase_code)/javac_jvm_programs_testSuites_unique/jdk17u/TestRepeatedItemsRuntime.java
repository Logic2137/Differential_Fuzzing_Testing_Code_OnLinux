import java.lang.annotation.*;
import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

@Quux
public class TestRepeatedItemsRuntime extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedOptions() {
        IdentityHashMap<String, Integer> temp = new IdentityHashMap<>();
        temp.put(new String("foo"), 1);
        temp.put(new String("foo"), 2);
        var returnValue = temp.keySet();
        assert returnValue.size() == 2;
        return returnValue;
    }

    private static class ArrayBackedSet implements Set<String> {

        private static String[] data = { "Quux", "Quux", "&&&/foo.Bar", "foo.Bar", "foo.Bar", "quux/Quux", "*" };

        public ArrayBackedSet() {
        }

        @Override
        public Iterator<String> iterator() {
            return Arrays.asList(data).iterator();
        }

        @Override
        public boolean add(String e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends String> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean contains(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            return o == this;
        }

        @Override
        public int hashCode() {
            int hash = 0;
            for (String s : data) {
                hash += s.hashCode();
            }
            return hash;
        }

        @Override
        public boolean isEmpty() {
            return data.length > 0;
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return data.length;
        }

        @Override
        public Object[] toArray() {
            return data.clone();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new ArrayBackedSet();
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        return true;
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface Quux {
}
