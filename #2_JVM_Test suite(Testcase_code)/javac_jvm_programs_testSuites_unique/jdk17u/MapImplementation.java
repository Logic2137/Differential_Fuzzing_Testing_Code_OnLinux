import java.util.Map;

public interface MapImplementation {

    public Class<?> klazz();

    public Map emptyMap();

    default Object makeKey(int i) {
        return i;
    }

    default Object makeValue(int i) {
        return i;
    }

    default int keyToInt(Object key) {
        return (Integer) key;
    }

    default int valueToInt(Object value) {
        return (Integer) value;
    }

    public boolean isConcurrent();

    default boolean remappingFunctionCalledAtMostOnce() {
        return true;
    }

    public boolean permitsNullKeys();

    public boolean permitsNullValues();

    public boolean supportsSetValue();
}
