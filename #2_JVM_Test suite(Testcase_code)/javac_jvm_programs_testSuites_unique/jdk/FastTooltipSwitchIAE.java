



import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class FastTooltipSwitchIAE {
    static Dimension oneByOneSize = new Dimension(1, 1);

    public static void main(String[] args) {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            try {
                SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
                SwingUtilities.invokeAndWait(FastTooltipSwitchIAE::doTest);
                System.out.println("Test passed for LookAndFeel " + laf.getClassName());
            } catch (Exception e) {
                throw new RuntimeException("Test failed for " + laf.getClassName(), e);
            }
        }
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) {
        try {
            System.out.println("LookAndFeel: " + laf.getClassName());
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (UnsupportedLookAndFeelException ignored) {
            System.err.println("Unsupported L&F: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void doTest() {
        JToolTip toolTip = new JToolTip();
        toolTip.setTipText("<html><h1>Hello world</h1></html>");
        toolTip.setMinimumSize(oneByOneSize);
        toolTip.setMaximumSize(oneByOneSize);
        toolTip.setPreferredSize(oneByOneSize);
        toolTip.setBounds(100, 100, 1, 1);

        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        toolTip.paint(g2d);

        g2d.dispose();
    }
}
