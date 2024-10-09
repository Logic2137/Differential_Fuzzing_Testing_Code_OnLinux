
package pkg2;

public interface Spliterator<T> {

    public interface OfDouble extends OfPrimitive<Double, Object, OfDouble> {
    }

    public static interface OfInt<Integer> extends Spliterator {
    }

    public interface OfPrimitive<T, T_CONS, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>> extends Spliterator<T> {
    }
}
