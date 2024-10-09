

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import static javax.swing.UIManager.getInstalledLookAndFeels;


public final class SilenceOfDeprecatedMenuBar implements Runnable {

    public static void main(final String[] args) throws Exception {
        for (final UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
            SwingUtilities.invokeAndWait(new SilenceOfDeprecatedMenuBar());
        }
    }

    @Override
    public void run() {
        final JFrame frame = new DeprecatedFrame();
        try {
            final JMenuBar bar = new JMenuBar();
            frame.setJMenuBar(bar);
            frame.setBounds(100, 100, 100, 100);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            if (bar != frame.getJMenuBar()) {
                throw new RuntimeException("Wrong JMenuBar");
            }
        } finally {
            frame.dispose();
        }
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
            System.out.println("LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static class DeprecatedFrame extends JFrame {

        @Override
        protected JRootPane createRootPane() {
            return new JRootPane() {
                @Override
                public JMenuBar getMenuBar() {
                    throw new RuntimeException("Should not be here");
                }
                @Override
                public void setMenuBar(final JMenuBar menu) {
                    throw new RuntimeException("Should not be here");
                }
            };
        }
    }
}
