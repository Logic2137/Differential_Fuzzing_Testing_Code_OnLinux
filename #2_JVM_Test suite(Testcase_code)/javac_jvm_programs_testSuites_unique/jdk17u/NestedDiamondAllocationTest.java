public class NestedDiamondAllocationTest {

    static class Clazz2 {

        static class A {
        }

        public A a;
    }

    static class FooNest<Q> {

        FooNest(Q q, Foo<Q> foo) {
        }
    }

    static class Foo<T> {
    }

    static Clazz2 clazz = new Clazz2();

    public static void main(String[] args) {
        FooNest fooNest = new FooNest<>(clazz.a, new Foo<>() {
        });
    }
}
