



import javax.swing.*;
import java.awt.Graphics;
import java.awt.Robot;

public class bug8016551 {
    private static volatile RuntimeException exception = null;

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            
        }

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    Icon icon = UIManager.getIcon("InternalFrame.closeIcon");
                    if (icon == null) {
                        return;
                    }

                    JMenuItem item = new TestMenuItem(icon);
                    JFrame f = new JFrame();
                    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    f.getContentPane().add(item);
                    f.pack();
                    f.setVisible(true);
                } catch (ClassCastException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Robot robot = new Robot();
        robot.waitForIdle();

        if (exception != null) {
            throw exception;
        }
    }

    static class TestMenuItem extends JMenuItem {
        TestMenuItem(Icon icon) {
            super(icon);
        }

        @Override
        public void paint(Graphics g) {
            try {
                super.paint(g);
            } catch (RuntimeException e) {
                exception = e;
            }
        }
    }
}

