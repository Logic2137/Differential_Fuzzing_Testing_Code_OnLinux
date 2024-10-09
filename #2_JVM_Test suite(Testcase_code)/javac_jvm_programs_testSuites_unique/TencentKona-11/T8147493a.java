



abstract class T8147493a {
    interface One {}
    interface Two<I extends One> { I get(); }
    interface Three<T> {}
    interface Four<T> {}

    <E extends Two<?>, L extends Three<E>> Four<L> f(Class raw, E destination) {
        return g(raw, destination.get());
    }

    abstract <I extends One, E extends Two<I>, L extends Three<E>> Four<L> g(
            Class<L> labelClass, I destinationId);
}
