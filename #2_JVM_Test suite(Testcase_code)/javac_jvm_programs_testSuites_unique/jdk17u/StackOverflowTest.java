import java.nio.channels.Selector;

public class StackOverflowTest {

    public static void main(String[] args) throws Exception {
        try (var sel = Selector.open()) {
            recursiveSelect(sel);
        } catch (StackOverflowError e) {
        }
    }

    static void recursiveSelect(Selector sel) throws Exception {
        sel.selectNow();
        recursiveSelect(sel);
    }
}
