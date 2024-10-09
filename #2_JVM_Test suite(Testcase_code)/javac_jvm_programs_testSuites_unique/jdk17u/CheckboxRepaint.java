import java.awt.*;

public final class CheckboxRepaint extends Checkbox {

    public static void main(final String[] args) {
        for (int i = 0; i < 10; ++i) {
            final Frame frame = new Frame();
            frame.setSize(300, 300);
            frame.setLocationRelativeTo(null);
            CheckboxRepaint checkbox = new CheckboxRepaint();
            frame.add(checkbox);
            frame.setVisible(true);
            sleep();
            checkbox.test();
            frame.dispose();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        if (!EventQueue.isDispatchThread()) {
            throw new RuntimeException("Wrong thread");
        }
        test();
    }

    void test() {
        setState(getState());
        setCheckboxGroup(getCheckboxGroup());
        setLabel("");
        setLabel(null);
        setLabel(getLabel());
        setFont(null);
        setFont(getFont());
        setBackground(null);
        setBackground(getBackground());
        setForeground(null);
        setForeground(getForeground());
        setEnabled(isEnabled());
    }
}
