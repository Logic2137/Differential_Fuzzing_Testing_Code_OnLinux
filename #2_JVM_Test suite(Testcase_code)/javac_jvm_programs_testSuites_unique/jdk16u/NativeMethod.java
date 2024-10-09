



import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class NativeMethod {
    public static void main(String... args) throws Exception {
        new NativeMethod().test();
    }

    private final StackWalker walker;
    NativeMethod() {
        this.walker = StackWalker.getInstance(Option.SHOW_REFLECT_FRAMES);
    }

    void test() throws Exception {
        
        Method m = NativeMethod.class.getDeclaredMethod("walk");
        m.invoke(this);
    }

    void walk() {
        List<StackFrame> nativeFrames = walker.walk(s ->
            s.filter(StackFrame::isNativeMethod)
             .collect(Collectors.toList())
        );

        assertTrue(nativeFrames.size() > 0, "native frame not found");
        for (StackFrame f : nativeFrames) {
            assertTrue(f.getFileName() != null, "source file expected to be found");
            assertTrue(f.getLineNumber() < 0, "line number expected to be unavailable");
            assertTrue(f.getByteCodeIndex() < 0, "bci expected to be unavailable");
        }

    }

    private static void assertTrue(boolean value, String msg) {
        if (value != true)
            throw new AssertionError(msg);
    }
}
