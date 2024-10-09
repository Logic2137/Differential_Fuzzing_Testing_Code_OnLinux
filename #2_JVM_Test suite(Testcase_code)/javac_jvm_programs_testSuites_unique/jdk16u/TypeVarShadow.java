



public class TypeVarShadow {
    class T<E> {}

    abstract class One<E> {
        abstract E foo();
    }

    abstract class Two<T> extends One<T> {
        abstract T foo();
    }
}
