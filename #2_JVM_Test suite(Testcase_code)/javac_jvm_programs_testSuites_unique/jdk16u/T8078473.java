


class T8078473<P, Q> {

    static class C<T, U> {
        C(T8078473<?, ?> p) {}
    }

    {
        new C<Q, Q>(this) {};
        new C<Q, Q>(this);
    }
}
