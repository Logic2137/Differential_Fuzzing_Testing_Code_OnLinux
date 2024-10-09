



import java.io.Serializable;
import java.util.function.Consumer;

class IntersectionTypeBugTest {
    <T extends Object & Serializable & Consumer<String>> void consume(final T cons, final String s) {}

    void process(final String s) {}

    public void foo() {
        consume(this::process, "Hello World");
    }

    
    static class AnotherTest<T> {
        void foo() {
            Object r = (Object & Serializable & R<T>) () -> {};
        }

        interface R<I> {
            void foo();
        }
    }
}
