

class T8141343 {
    interface Foo<X> { }

    static class A extends Exception implements Foo<A> { }
    static class B extends Exception implements Foo<B> { }

    void test(boolean cond) {
        try {
            if (cond) {
                throw new A();
            } else {
                throw new B();
            }
        } catch (A | B ex) {
            Foo<Integer> fa = (Foo<Integer>)ex;
        }
    }
}
