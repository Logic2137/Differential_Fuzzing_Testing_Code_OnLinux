


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

public class RenderToCustomBufferTest {
     public static void main(String[] args) {
        final BufferedImage dst_custom = createCustomBuffer();
        final BufferedImage dst_dcm = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        renderTo(dst_custom);
        renderTo(dst_dcm);

        check(dst_custom, dst_dcm);
    }

    private static void check(BufferedImage a, BufferedImage b) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pa = a.getRGB(x, y);
                int pb = b.getRGB(x, y);

                if (pa != pb) {
                    String msg = String.format(
                        "Point [%d, %d] has different colors: %08X and %08X",
                        x, y, pa, pb);
                    throw new RuntimeException("Test failed: " + msg);
                }
            }
        }
    }

    private static BufferedImage createCustomBuffer() {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, false,
                Transparency.OPAQUE, DataBuffer.TYPE_FLOAT);
        WritableRaster wr = cm.createCompatibleWritableRaster(width, height);

        return new BufferedImage(cm, wr, false, null);
    }

    private static void renderTo(BufferedImage dst) {
        System.out.println("The buffer: " + dst);
        Graphics2D g = dst.createGraphics();

        final int w = dst.getWidth();
        final int h = dst.getHeight();

        g.setColor(Color.blue);
        g.fillRect(0, 0, w, h);

        g.setColor(Color.red);
        Font f = g.getFont();
        g.setFont(f.deriveFont(48f));

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
               RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        
        g.setClip(50, 50, 200, 100);

        g.drawString("AA Text", 52, 90);

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
               RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        
        g.setClip(50, 100, 100, 100);
        g.drawString("Text", 52, 148);

        g.dispose();
    }

    private static final int width = 230;
    private static final int height = 150;
}
