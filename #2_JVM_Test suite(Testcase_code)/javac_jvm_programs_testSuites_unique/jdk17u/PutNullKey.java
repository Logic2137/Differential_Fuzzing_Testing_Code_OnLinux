import java.util.*;
import java.util.stream.IntStream;

public class PutNullKey {

    static final int INITIAL_CAPACITY = 64;

    static final int SIZE = 256;

    static final float LOAD_FACTOR = 1.0f;

    public static class CollidingHash implements Comparable<CollidingHash> {

        private final int value;

        public CollidingHash(int value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (null == o) {
                return false;
            }
            if (o.getClass() != CollidingHash.class) {
                return false;
            }
            return value == ((CollidingHash) o).value;
        }

        @Override
        public int compareTo(CollidingHash o) {
            return value - o.value;
        }
    }

    public static void main(String[] args) throws Exception {
        Map<Object, Object> m = new HashMap<>(INITIAL_CAPACITY, LOAD_FACTOR);
        IntStream.range(0, SIZE).mapToObj(CollidingHash::new).forEach(e -> {
            m.put(e, e);
        });
        m.put(null, null);
    }
}
