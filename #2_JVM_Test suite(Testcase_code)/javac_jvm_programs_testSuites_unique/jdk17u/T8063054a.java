class T8063054a {

    interface Consumer<T> {

        void accept(T arg);
    }

    interface Parent<P> {

        void foo();
    }

    interface Child extends Parent<String> {
    }

    static <T> void m(T arg, Consumer<T> f) {
    }

    public void test(Child c) {
        m(c, Parent::foo);
    }
}
