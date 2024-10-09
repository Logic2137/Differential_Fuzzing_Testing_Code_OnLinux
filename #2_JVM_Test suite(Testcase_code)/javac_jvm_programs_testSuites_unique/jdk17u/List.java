
package listpkg;

public interface List<E> {

    List add(E e);

    void remove(int i);

    int size();

    static <E> List<E> of() {
        return null;
    }

    static <E> List<E> of(E e1) {
        return null;
    }

    static <E> List<E> of(E e1, E e2) {
        return null;
    }

    static <E> List<E> of(E e1, E e2, E e3) {
        return null;
    }

    static <E> List<E> of(E e1, E e2, E e3, E e4) {
        return null;
    }

    static <E> List<E> of(E e1, E e2, E e3, E e4, E e5) {
        return null;
    }

    static <E> List<E> of(E... elements) {
        return null;
    }
}
