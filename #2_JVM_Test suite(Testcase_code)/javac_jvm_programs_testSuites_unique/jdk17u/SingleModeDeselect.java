import java.awt.*;

public class SingleModeDeselect {

    public static final void main(String[] args) {
        final Frame frame = new Frame();
        final List list = new List();
        list.add(" item 0 ");
        list.add(" item 1 ");
        frame.add(list);
        frame.setLayout(new FlowLayout());
        frame.setBounds(100, 100, 300, 300);
        frame.setVisible(true);
        list.select(0);
        list.deselect(1);
        try {
            Robot robot = new Robot();
            robot.waitForIdle();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unexpected failure");
        }
        if (list.getSelectedIndex() != 0) {
            throw new RuntimeException("Test failed: List.getSelectedIndex() returns " + list.getSelectedIndex());
        }
    }
}
