

import java.util.concurrent.CountDownLatch;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;



public class Test8019180 implements Runnable {
    private static final CountDownLatch LATCH = new CountDownLatch(1);
    private static final String[] ITEMS = {"First", "Second", "Third", "Fourth"};

    public static void main(String[] args) throws InterruptedException {
        SwingUtilities.invokeLater(new Test8019180());
        LATCH.await();
    }

    private JComboBox<String> test;

    @Override
    public void run() {
        if (this.test == null) {
            this.test = new JComboBox<>(ITEMS);
            this.test.addActionListener(this.test);
            JFrame frame = new JFrame();
            frame.add(test);
            frame.pack();
            frame.setVisible(true);
            SwingUtilities.invokeLater(this);
        } else {
            int index = this.test.getSelectedIndex();
            this.test.setSelectedIndex(1 + index);
            if (0 > this.test.getSelectedIndex()) {
                System.err.println("ERROR: no selection");
                System.exit(8019180);
            }
            SwingUtilities.getWindowAncestor(this.test).dispose();
            LATCH.countDown();
        }
    }
}
