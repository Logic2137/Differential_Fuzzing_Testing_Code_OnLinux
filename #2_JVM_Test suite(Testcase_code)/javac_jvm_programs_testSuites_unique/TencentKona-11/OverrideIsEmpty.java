





import java.util.function.BiFunction;
import java.util.HashMap;

public class OverrideIsEmpty {
    public static class NotEmptyHashMap<K,V> extends HashMap<K,V> {
        private K alwaysExistingKey;
        private V alwaysExistingValue;

        @Override
        public V get(Object key) {
            if (key == alwaysExistingKey) {
                return alwaysExistingValue;
            }
            return super.get(key);
        }

        @Override
        public int size() {
            return super.size() + 1;
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }
    }

    public static void main(String[] args) {
        NotEmptyHashMap<Object, Object> map = new NotEmptyHashMap<>();
        Object key = new Object();
        Object value = new Object();
        map.get(key);
        map.remove(key);
        map.replace(key, value, null);
        map.replace(key, value);
        map.computeIfPresent(key, new BiFunction<Object, Object, Object>() {
            public Object apply(Object key, Object oldValue) {
                return oldValue;
            }
        });
    }
}

