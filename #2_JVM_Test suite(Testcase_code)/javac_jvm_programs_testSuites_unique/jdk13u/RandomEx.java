
package vm.share;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class RandomEx extends Random {

    private final Map<Class<?>, Supplier<?>> map = new HashMap<>();

    {
        map.put(Boolean.class, this::nextBoolean);
        map.put(boolean.class, this::nextBoolean);
        map.put(Byte.class, this::nextByte);
        map.put(byte.class, this::nextByte);
        map.put(Short.class, this::nextShort);
        map.put(short.class, this::nextShort);
        map.put(Character.class, this::nextChar);
        map.put(char.class, this::nextChar);
        map.put(Integer.class, this::nextInt);
        map.put(int.class, this::nextInt);
        map.put(Long.class, this::nextLong);
        map.put(long.class, this::nextLong);
        map.put(Float.class, this::nextFloat);
        map.put(float.class, this::nextFloat);
        map.put(Double.class, this::nextDouble);
        map.put(double.class, this::nextDouble);
    }

    public RandomEx() {
    }

    public RandomEx(long seed) {
        super(seed);
    }

    public byte nextByte() {
        return (byte) next(Byte.SIZE);
    }

    public short nextShort() {
        return (short) next(Short.SIZE);
    }

    public char nextChar() {
        return (char) next(Character.SIZE);
    }

    public <T> T next(Predicate<T> p, T dummy) {
        T result;
        do {
            result = next(dummy);
        } while (!p.test(result));
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T next(T dummy) {
        Supplier<?> supplier = map.get(dummy.getClass());
        if (supplier == null) {
            throw new IllegalArgumentException("supplier for <" + dummy.getClass() + ">is not found");
        }
        return (T) supplier.get();
    }
}
