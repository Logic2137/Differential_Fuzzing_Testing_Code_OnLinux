import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import static javax.swing.UIManager.getInstalledLookAndFeels;

public class Test6981576 extends TitledBorder {

    private static volatile Throwable failed;

    public static void main(String[] args) throws Throwable {
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            failed = e;
        });
        for (final UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            EventQueue.invokeAndWait(() -> setLookAndFeel(laf));
            EventQueue.invokeAndWait(() -> {
                JPanel panel = new JPanel();
                panel.setBorder(new Test6981576());
                frame = new JFrame("Test6981576");
                frame.add(panel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(300, 300);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });
            EventQueue.invokeAndWait(() -> {
                frame.repaint();
            });
            EventQueue.invokeAndWait(() -> {
                frame.dispose();
            });
        }
        if (failed != null) {
            throw failed;
        }
    }

    private static JFrame frame;

    private Test6981576() {
        super("");
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        getBorder().paintBorder(c, g, x, y, width, height);
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
            System.out.println("LookAndFeel: " + laf.getClassName());
        } catch (final UnsupportedLookAndFeelException ignored) {
            System.out.println("Unsupported LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
