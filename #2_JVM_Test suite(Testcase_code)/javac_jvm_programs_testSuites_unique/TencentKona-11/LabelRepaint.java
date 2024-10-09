

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;


public final class LabelRepaint extends Label {

    public static void main(final String[] args) {
        for (int i = 0; i < 10; ++i) {
            final Frame frame = new Frame();
            frame.setSize(300, 300);
            frame.setLocationRelativeTo(null);
            LabelRepaint label = new LabelRepaint();
            frame.add(label);
            frame.setVisible(true);
            sleep();
            label.test();
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
        setAlignment(getAlignment());

        setText("");
        setText(null);
        setText(getText());

        setFont(null);
        setFont(getFont());

        setBackground(null);
        setBackground(getBackground());

        setForeground(null);
        setForeground(getForeground());

        setEnabled(isEnabled());
    }
}
