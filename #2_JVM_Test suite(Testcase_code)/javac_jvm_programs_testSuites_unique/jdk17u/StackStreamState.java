import java.lang.StackWalker.StackFrame;
import java.util.stream.Stream;

public class StackStreamState {

    public static void main(String... args) {
        StackStreamState test = new StackStreamState();
        test.testStatic();
        test.testInstance();
        test.testLocal();
    }

    private static Stream<StackFrame> staticStream;

    private Stream<StackFrame> instanceStream;

    private final StackWalker walker = StackWalker.getInstance();

    void testStatic() {
        walker.walk(s -> {
            staticStream = s;
            return null;
        });
        checkStreamState(staticStream);
    }

    void testInstance() {
        walker.walk(s -> {
            instanceStream = s;
            return null;
        });
        checkStreamState(instanceStream);
    }

    void testLocal() {
        Stream<StackFrame> stream = walker.walk(s -> {
            return s;
        });
        checkStreamState(stream);
    }

    void checkStreamState(Stream<StackFrame> stream) {
        try {
            stream.count();
            throw new RuntimeException("IllegalStateException not thrown");
        } catch (IllegalStateException e) {
            System.out.println("Got expected IllegalStateException: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }
}
