

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;



public class HugeGradientTest extends Frame {
        public static volatile boolean success = false;

        public HugeGradientTest() {
                Image dstImg = getGraphicsConfiguration()
                        .createCompatibleVolatileImage(30, 30);
                Graphics2D g = (Graphics2D) dstImg.getGraphics();

                g.setPaint(new LinearGradientPaint(0f, Short.MAX_VALUE, 0f, Short.MAX_VALUE +31,
                        new float[]{0f, 1f}, new Color[]{Color.BLACK, Color.RED}));
                g.translate(0, -Short.MAX_VALUE);
                g.fillRect (0, 0, Short.MAX_VALUE*2 , Short.MAX_VALUE*2);

                BufferedImage readBackImg = new BufferedImage(dstImg.getWidth(null),
                dstImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
                readBackImg.getGraphics().drawImage(dstImg, 0, 0, null);

                for (int x = 0; x < readBackImg.getWidth(); x++) {
                        for (int y = 0; y < readBackImg.getHeight(); y++) {
                                int redVal = (readBackImg.getRGB(x, y) & 0x00FF0000) >> 16;

                                if (redVal > 127) {
                                        return;
                                }
                        }
                }

                throw new RuntimeException("Test Failed");
        }

        public static void main(String[] args) throws Exception {
                SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                                new HugeGradientTest();
                        }
                });
        }
}
