



import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HiddenFrames {
    public static void main(String... args) throws Exception {
        new HiddenFrames().test();
        new HiddenFrames(Option.SHOW_REFLECT_FRAMES).test();
        new HiddenFrames(Option.SHOW_HIDDEN_FRAMES).test();
    }

    private final Option option;
    private final StackWalker walker;
    private final List<StackFrame> lambdas = new ArrayList<>();
    private final List<StackFrame> reflects = new ArrayList<>();

    HiddenFrames() {
        this.option = null;
        this.walker = StackWalker.getInstance();
    }
    HiddenFrames(Option option) {
        this.option = option;
        this.walker = StackWalker.getInstance(option);
    }

    void test() throws Exception {
        walk();
        walkFromReflection();
    }

    void walk() {
       Stream.of(0).forEach(i -> walker.walk(s ->
       {
           s.forEach(this::checkFrame);
           return null;
       }));

        
        
        if (option == null && !lambdas.isEmpty()) {
            throw new RuntimeException("Hidden frames are shown");
        }

        if (option == Option.SHOW_HIDDEN_FRAMES && lambdas.isEmpty()) {
            throw new RuntimeException("No hidden Lambda frame");
        }
    }

    void walkFromReflection() throws Exception {
        Method m = HiddenFrames.class.getDeclaredMethod("walk");
        m.invoke(this);

        if (option == null && !lambdas.isEmpty()) {
            throw new RuntimeException("Hidden frames are shown");
        }

        if (option == Option.SHOW_HIDDEN_FRAMES && lambdas.isEmpty()) {
            throw new RuntimeException("No hidden Lambda frame");
        }

        if (option != null && reflects.isEmpty()) {
            throw new RuntimeException("No reflection frame");
        }
    }

    void checkFrame(StackFrame frame) {
        String cn = frame.getClassName();
        if (cn.startsWith("java.lang.reflect.") || cn.startsWith("jdk.internal.reflect.")) {
            reflects.add(frame);
        }
        if (cn.contains("$$Lambda$")) {
            lambdas.add(frame);
        }
    }
}
