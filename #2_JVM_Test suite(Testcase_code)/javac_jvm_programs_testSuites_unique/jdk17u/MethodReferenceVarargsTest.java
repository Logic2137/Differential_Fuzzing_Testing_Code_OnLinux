public class MethodReferenceVarargsTest<T> {

    public T invoke(Object... args) {
        return null;
    }

    public static <T extends String> void test() {
        MethodReferenceVarargsTest<T> bug = new MethodReferenceVarargsTest<>();
        java.util.function.Function<String, T> b = bug::invoke;
        java.util.function.Function<String, T> f = (args) -> bug.invoke(args);
    }
}
