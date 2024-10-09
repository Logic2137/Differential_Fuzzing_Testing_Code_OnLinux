



public class T8191959 {
    static class A<E> { }

    <T> A<? super T> m(T t) {
        return null;
    }

    <U> A<? super A<? extends U>> m2(A<? super U> u) {
        return null;
    }

    void test() {
        var varValue = m2(m(10));
        A<? super A<? extends Integer>> expectedTypeValue = varValue;
    }
}
