



class T8055984 {
    static class C<U> {
        U fu;

        C() { }

        C(C<U> other) {
            this.fu = other.fu;
        }

        C(U fu) {
            this.fu = fu;
        }
    }

    static <U> C<U> m(C<U> src) { return new C<U>(src); }

    static void test() {
        C<String> c2 = m(new C<>(m(new C<>() )) );
        C<String> c3 = m(new C<>(m(new C<>(m(new C<>() )) )) );
        C<String> c4 = m(new C<>(m(new C<>(m(new C<>(m(new C<>() )) )) )) );
        C<String> c5 = m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>() )) )) )) )) );
        C<String> c6 = m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>() )) )) )) )) )) );
        C<String> c7 = m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>() )) )) )) )) )) )) );
        C<String> c8 = m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>() )) )) )) )) )) )) )) );
        C<String> c9 = m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>() )) )) )) )) )) )) )) )) );
        C<String> c10 = m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>(m(new C<>())))))))))))))))))));
    }
}

