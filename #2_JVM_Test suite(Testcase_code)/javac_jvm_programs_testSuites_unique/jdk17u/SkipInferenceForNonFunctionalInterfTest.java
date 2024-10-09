class SkipInferenceForNonFunctionalInterfTest {

    class U1 {
    }

    class U2 {
    }

    class U3 {
    }

    interface SAM<P1 extends U1, P2 extends U2, P3 extends U3> {

        P3 m(P1 p1, P2 p2);
    }

    interface I<T> {
    }

    class Tester {

        Object method(SAM<U1, U2, U3> sam) {
            return null;
        }

        Object run() {
            return method((SAM<U1, U2, U3> & I<?>) (U1 u1, U2 u2) -> {
                return new U3();
            });
        }
    }
}
