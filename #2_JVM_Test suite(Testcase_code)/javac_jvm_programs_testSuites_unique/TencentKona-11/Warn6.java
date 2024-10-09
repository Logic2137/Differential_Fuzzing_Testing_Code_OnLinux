



public class Warn6<T> {
    @SafeVarargs
    public Warn6(T... args) {
    }

    public static void main(String[] args) {
        Iterable<String> i = null;

        Warn6<Iterable<String>> foo2 = new Warn6<>(i, i);
        Warn6<Iterable<String>> foo3 = new Warn6<Iterable<String>>(i, i);
    }
}

