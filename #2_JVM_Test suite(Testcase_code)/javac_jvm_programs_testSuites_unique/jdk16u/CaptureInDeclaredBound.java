



public class CaptureInDeclaredBound {

    class C<T> {}

    interface I<X extends Comparable<X>> {
        C<X> get();
    }

    <Z extends Comparable<? super Z>> void m(C<Z> arg) {}

    void test(I<?> arg) {
        m(arg.get());
    }

}
