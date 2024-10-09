class T6987475pos {

    static abstract class Base<A> {

        public void go(String s) {
        }

        public abstract void go(A a);
    }

    static abstract class BaseReverse<A> {

        public abstract void go(A a);

        public void go(String s) {
        }
    }

    static class Impl1 extends Base<String> {
    }

    static class Impl2 extends BaseReverse<String> {
    }
}
