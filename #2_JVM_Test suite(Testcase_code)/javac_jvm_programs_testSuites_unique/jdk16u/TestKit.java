

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;




public final class TestKit {

    private TestKit() { }

    public static void assertNotThrows(ThrowingProcedure code) {
        requireNonNull(code, "code");
        assertNotThrows(() -> {
            code.run();
            return null;
        });
    }

    public static <V> V assertNotThrows(ThrowingFunction<V> code) {
        requireNonNull(code, "code");
        try {
            return code.run();
        } catch (Throwable t) {
            throw new RuntimeException("Expected to run normally, but threw "
                    + t.getClass().getCanonicalName(), t);
        }
    }

    public static <T extends Throwable> T assertThrows(Class<T> clazz,
                                                       ThrowingProcedure code) {
        requireNonNull(clazz, "clazz");
        requireNonNull(code, "code");
        try {
            code.run();
        } catch (Throwable t) {
            if (clazz.isInstance(t)) {
                return clazz.cast(t);
            }
            throw new RuntimeException("Expected to catch an exception of type "
                    + clazz.getCanonicalName() + ", but caught "
                    + t.getClass().getCanonicalName(), t);

        }
        throw new RuntimeException("Expected to catch an exception of type "
                + clazz.getCanonicalName() + ", but caught nothing");
    }

    public interface ThrowingProcedure {
        void run() throws Throwable;
    }

    public interface ThrowingFunction<V> {
        V run() throws Throwable;
    }

    
    
    
    public static <T extends Throwable> T assertThrows(Class<T> clazz,
                                                       String messageRegex,
                                                       ThrowingProcedure code) {
        requireNonNull(messageRegex, "messagePattern");
        T t = assertThrows(clazz, code);
        String m = t.getMessage();
        if (m == null) {
            throw new RuntimeException(String.format(
                    "Expected exception message to match the regex '%s', " +
                            "but the message was null", messageRegex), t);
        }
        if (!Pattern.matches(messageRegex, m)) {
            throw new RuntimeException(String.format(
                    "Expected exception message to match the regex '%s', " +
                            "actual message: %s", messageRegex, m), t);
        }
        return t;
    }

    
    public static void assertUnmodifiableCollection(Collection<?> collection) {
        assertUnmodifiableCollection(collection, () -> null);
    }

    public static <E> void assertUnmodifiableCollection(Collection<E> collection,
                                                        Supplier<? extends E> elementsFactory) {
        requireNonNull(collection, "collection");
        requireNonNull(elementsFactory, "elementsFactory");

        E e = elementsFactory.get();

        assertUOE(() -> collection.add(e));
        assertUOE(() -> collection.addAll(singleton(e)));
        Iterator<?> i = collection.iterator();
        if (i.hasNext()) {
            i.next();
            assertUOE(i::remove);
        }
        assertUOE(collection::clear);
        assertUOE(() -> collection.remove(e));
        assertUOE(() -> collection.removeAll(singleton(e)));
        assertUOE(() -> collection.removeIf(x -> true));
        assertUOE(() -> collection.retainAll(emptyList()));
        
        
    }

    public static void assertUnmodifiableSet(Set<?> set) {
        assertUnmodifiableCollection(set, () -> null);
    }

    public static <E> void assertUnmodifiableSet(Set<E> set,
                                                 Supplier<? extends E> elementsFactory) {
        assertUnmodifiableCollection(set, elementsFactory);
    }

    public static void assertUnmodifiableList(List<?> list) {
        assertUnmodifiableList(list, () -> null);
    }

    public static <E> void assertUnmodifiableList(List<E> list,
                                                  Supplier<? extends E> elementsFactory) {
        assertUnmodifiableList(list, elementsFactory, 3); 
    }

    private static <E> void assertUnmodifiableList(List<E> list,
                                                   Supplier<? extends E> elementsFactory,
                                                   int depth) {
        requireNonNull(list, "list");
        requireNonNull(elementsFactory, "elementsFactory");
        if (depth < 0) {
            throw new IllegalArgumentException("depth: " + depth);
        }
        if (depth == 0) {
            return;
        }

        E e = elementsFactory.get();

        assertUnmodifiableCollection(list, elementsFactory);
        assertUOE(() -> list.add(0, e));
        assertUOE(() -> list.addAll(0, singleton(e)));

        ListIterator<E> i = list.listIterator();
        if (i.hasNext()) {
            i.next();
            assertUOE(i::remove);
            assertUOE(() -> i.set(e));
            assertUOE(() -> i.add(e));
        }
        assertUOE(() -> list.remove((int) 0));
        assertUOE(() -> list.replaceAll(x -> e));
        assertUOE(() -> list.set(0, e));

        
        Comparator<Object> comparator = (a, b) -> Objects.hash(a, b);
        assertUOE(() -> list.sort(comparator));

        assertUnmodifiableList(list.subList(0, list.size()), elementsFactory, depth - 1);
    }

    public static void assertUnmodifiableMap(Map<?, ?> map) {
        assertUnmodifiableMap(map, () -> new AbstractMap.SimpleImmutableEntry<>(null, null));
    }

    public static <K, V> void assertUnmodifiableMap(Map<K, V> map,
                                                    Supplier<? extends Map.Entry<? extends K, ? extends V>> entriesFactory) {
        requireNonNull(map, "map");
        requireNonNull(entriesFactory, "entriesFactory");
        assertUOE(map::clear);

        Map.Entry<? extends K, ? extends V> e1 = entriesFactory.get();
        K k = e1.getKey();
        V v = e1.getValue();

        assertUOE(() -> map.compute(k, (k1, v1) -> v));
        assertUOE(() -> map.computeIfAbsent(k, (k1) -> v));
        assertUOE(() -> map.computeIfPresent(k, (k1, v1) -> v));

        Set<Map.Entry<K, V>> entrySet = map.entrySet();
        assertUnmodifiableSet(entrySet);
        for (Map.Entry<K, V> e : entrySet) {
            assertUOE(() -> e.setValue(null));
        }

        assertUnmodifiableSet(map.keySet());
        assertUOE(() -> map.merge(k, v, (k1, v1) -> v));
        assertUOE(() -> map.put(k, v));
        
        Map<K, V> m = new HashMap<>();
        m.put(k, v);
        assertUOE(() -> map.putAll(m));
        assertUOE(() -> map.putIfAbsent(k, v));
        assertUOE(() -> map.remove(k));
        assertUOE(() -> map.remove(k, v));
        assertUOE(() -> map.replace(k, v));
        assertUOE(() -> map.replace(k, v, v));
        assertUOE(() -> map.replaceAll((k1, v1) -> v));
        assertUnmodifiableCollection(map.values());
    }

    public static void assertUOE(ThrowingProcedure code) {
        assertThrows(UnsupportedOperationException.class, code);
    }
}
