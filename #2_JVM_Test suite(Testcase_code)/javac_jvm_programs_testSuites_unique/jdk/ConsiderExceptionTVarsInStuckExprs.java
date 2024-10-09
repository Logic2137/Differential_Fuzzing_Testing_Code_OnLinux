



class ConsiderExceptionTVarsInStuckExprs {

    public static void test() {
        outer(nested(x -> mightThrow()));
        outer(nested(ConsiderExceptionTVarsInStuckExprs::mightThrow2));
    }

    static <A> void outer(Object o) {}

    static <B, C, E extends Throwable> B nested(Fun<C,E> fun) {
        return null;
    }

    interface Fun<X, Y extends Throwable> {
        void m(X t) throws Y;
    }

    static void mightThrow() throws Exception {}
    static <C> void mightThrow2(C c) throws Exception {}
}
