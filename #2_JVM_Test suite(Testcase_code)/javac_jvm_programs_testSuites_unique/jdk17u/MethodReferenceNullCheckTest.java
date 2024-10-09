import java.util.function.Supplier;

public class MethodReferenceNullCheckTest {

    public static void main(String[] args) {
        String s = null;
        boolean npeFired = false;
        try {
            Supplier<Boolean> ss = s::isEmpty;
        } catch (NullPointerException npe) {
            npeFired = true;
        } finally {
            if (!npeFired)
                throw new AssertionError("NPE should have been thrown");
        }
    }
}
