
package pkg2;

abstract class UndocumentedGenericParent<T, E extends Throwable, F extends Throwable> {

    public T parentField;

    protected abstract T parentMethod(T t) throws F, E, IllegalStateException;
}
