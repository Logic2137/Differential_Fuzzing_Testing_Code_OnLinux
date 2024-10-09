

package pkg3;

public class PrivateGenericParent {

    private static class PrivateParent<T> {
        public T method(T t) {
            return t;
        }
    }

    public class PublicChild extends PrivateParent<String> {}
}
