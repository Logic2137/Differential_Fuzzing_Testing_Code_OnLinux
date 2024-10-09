public class MostSpecific29 {

    public static void main(String[] args) {
        new MostSpecific29().test();
    }

    interface Pred<T> {

        boolean test(T arg);
    }

    interface Fun<T, R> {

        R apply(T arg);
    }

    static void m1(Pred<? super Integer> f) {
    }

    static void m1(Fun<Integer, Boolean> f) {
        throw new AssertionError("Less-specific method invocation.");
    }

    void test() {
        m1((Integer n) -> true);
    }
}
