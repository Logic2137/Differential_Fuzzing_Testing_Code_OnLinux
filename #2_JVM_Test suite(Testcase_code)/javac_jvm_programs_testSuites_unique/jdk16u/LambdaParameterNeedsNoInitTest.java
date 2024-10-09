


import java.util.function.Predicate;

public class LambdaParameterNeedsNoInitTest {

    public static void main(String[] args) {
        new Inner();
    }

    private static class Inner {
        Predicate<String> synonymComparator = a -> a.isEmpty();
        Inner() {
            if (true) {
                return;
            }
            synonymComparator.test("");
        }
    }
}
