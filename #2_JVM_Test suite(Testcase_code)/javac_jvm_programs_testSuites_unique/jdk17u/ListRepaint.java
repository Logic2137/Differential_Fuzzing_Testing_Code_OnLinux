import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.List;

public final class ListRepaint extends List {

    public static void main(final String[] args) {
        for (int i = 0; i < 10; ++i) {
            final Frame frame = new Frame();
            frame.setSize(300, 300);
            frame.setLocationRelativeTo(null);
            ListRepaint list = new ListRepaint();
            list.add("1");
            list.add("2");
            list.add("3");
            list.add("4");
            list.select(0);
            frame.add(list);
            frame.setVisible(true);
            sleep();
            list.test();
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
        select(0);
        setFont(null);
        setFont(getFont());
        setBackground(null);
        setBackground(getBackground());
        setForeground(null);
        setForeground(getForeground());
        setEnabled(isEnabled());
    }
}
