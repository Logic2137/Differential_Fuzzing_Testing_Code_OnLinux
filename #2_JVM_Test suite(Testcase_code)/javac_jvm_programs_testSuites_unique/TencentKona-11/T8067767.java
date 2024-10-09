


class T8067767 {

    static class Pair<A, B> {
        static <A, B> Pair<A, B> of(A a, B b) {
            throw new RuntimeException();
        }
    }

    static class List<T> {
        static <T> List<T> of(T... tx) {
            throw new RuntimeException();
        }
    }

    static final List<Pair<String, String>> PAIRS = List.of(
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"),
            Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"), Pair.of("a", "b"));
}
