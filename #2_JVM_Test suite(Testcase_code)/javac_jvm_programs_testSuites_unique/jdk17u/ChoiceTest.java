import java.awt.Frame;
import java.awt.Choice;
import java.awt.Font;
import java.util.stream.Stream;

public class ChoiceTest {

    private static void UI() {
        Frame frame = new Frame("Test frame");
        Choice choice = new Choice();
        Stream.of(new String[] { "item 1", "item 2", "item 3" }).forEach(choice::add);
        frame.add(choice);
        frame.setBounds(100, 100, 400, 200);
        frame.setVisible(true);
        Font font = choice.getFont();
        int size = font.getSize();
        int height = choice.getBounds().height;
        try {
            if (height < size) {
                throw new RuntimeException("Test failed");
            }
        } finally {
            frame.dispose();
        }
    }

    public static void main(String[] args) throws Exception {
        ChoiceTest.UI();
    }
}
