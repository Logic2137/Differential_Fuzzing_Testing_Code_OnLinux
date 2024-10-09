
public class T8167000c<X extends Throwable> {
    interface A {
        Integer m() throws Throwable;
    }

    interface B<X extends Throwable> {
        Object m() throws X;
    }

    interface E<T extends Throwable> extends A, B<T> { }

    void test() {
        E<X> ex = () -> { throw new Throwable(); };
    }
}
