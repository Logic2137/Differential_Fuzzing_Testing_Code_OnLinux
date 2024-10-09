
package p.q.r;

class T8078473_2<P, Q> {

    static class C<T, U> {

        C(T8078473_2<?, ?> p) {
        }
    }

    {
        new C<Q, Q>(this) {
        };
        new C<Q, Q>(this);
    }
}
