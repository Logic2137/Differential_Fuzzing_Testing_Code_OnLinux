
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;
import java.awt.geom.AffineTransform;


public class ScaledTransform {

    private static volatile boolean passed = false;

    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.
                getLocalGraphicsEnvironment();

        if (ge.isHeadlessInstance()) {
            return;
        }

        for (GraphicsDevice gd : ge.getScreenDevices()) {
            for (GraphicsConfiguration gc : gd.getConfigurations()) {
                testScaleFactor(gc);
            }
        }
    }

    private static void testScaleFactor(final GraphicsConfiguration gc) {
        final Dialog dialog = new Dialog((Frame) null, "Test", true, gc);

        try {
            dialog.setSize(100, 100);
            Panel panel = new Panel() {

                @Override
                public void paint(Graphics g) {
                    if (g instanceof Graphics2D) {
                        AffineTransform gcTx = gc.getDefaultTransform();
                        AffineTransform gTx
                                = ((Graphics2D) g).getTransform();
                        passed = gcTx.getScaleX() == gTx.getScaleX()
                                && gcTx.getScaleY() == gTx.getScaleY();
                    } else {
                        passed = true;
                    }
                    dialog.setVisible(false);
                }
            };
            dialog.add(panel);
            dialog.setVisible(true);

            if (!passed) {
                throw new RuntimeException("Transform is not scaled!");
            }
        } finally {
            dialog.dispose();
        }
    }
}
