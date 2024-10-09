import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

public final class RepaintOnAWTShutdown implements Runnable {

    private static final CountDownLatch go = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new RepaintOnAWTShutdown());
        go.await(5, TimeUnit.SECONDS);
    }

    public void run() {
        JFrame frame = new JFrame();
        JPanel panel = new MyPanel();
        panel.setPreferredSize(new Dimension(100, 100));
        panel.setLayout(new FlowLayout());
        panel.add(new JTree());
        panel.add(new JList(new String[] { "one", "two" }));
        panel.add(new JTable(new String[][] { { "one", "two" } }, new String[] { "one", "two" }));
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private final class MyPanel extends JPanel {

        public void paint(Graphics g) {
            super.paint(g);
            go.countDown();
        }
    }
}
