import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.UIManager;

public class HiDPIPropertiesUnixTest {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        String testCase = args[0];
        switch(testCase) {
            case "UISCALE_DISABLED":
                testScale(1.0, 1.0);
                break;
            case "UISCALE_3":
                testScale(3.0, 3.0);
                break;
            case "UISCALE_4":
                testScale(4.0, 4.0);
                break;
            default:
                throw new RuntimeException("Unknown test case: " + testCase);
        }
    }

    private static void testScale(double scaleX, double scaleY) {
        Dialog dialog = new Dialog((Frame) null, true) {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                AffineTransform tx = ((Graphics2D) g).getTransform();
                dispose();
                if (scaleX != tx.getScaleX() || scaleY != tx.getScaleY()) {
                    throw new RuntimeException(String.format("Wrong scale:" + "[%f, %f] instead of [%f, %f].", tx.getScaleX(), tx.getScaleY(), scaleX, scaleY));
                }
            }
        };
        dialog.setSize(200, 300);
        dialog.setVisible(true);
    }
}
