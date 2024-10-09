

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public final class DrawRotatedString {

    private static final int SIZE = 500;

    public static void main(final String[] args) throws IOException {
        BufferedImage bi = createBufferedImage(true);
        verify(bi);
        bi = createBufferedImage(false);
        verify(bi);
        System.out.println("Passed");
    }

    private static void verify(BufferedImage bi) throws IOException {
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < 99; ++j) {
                
                if (bi.getRGB(i, j) != Color.RED.getRGB()) {
                    ImageIO.write(bi, "png", new File("image.png"));
                    throw new RuntimeException("Failed: wrong text location");
                }
            }
        }
    }

    private static BufferedImage createBufferedImage(final boolean  aa) {
        final BufferedImage bi = new BufferedImage(SIZE, SIZE,
                                                   BufferedImage.TYPE_INT_RGB);
        final Graphics2D bg = bi.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            aa ? RenderingHints.VALUE_ANTIALIAS_ON
                               : RenderingHints.VALUE_ANTIALIAS_OFF);
        bg.setColor(Color.RED);
        bg.fillRect(0, 0, SIZE, SIZE);
        bg.translate(100, 100);
        bg.rotate(Math.toRadians(90));
        bg.setColor(Color.BLACK);
        bg.setFont(bg.getFont().deriveFont(20.0f));
        bg.drawString("MMMMMMMMMMMMMMMM", 0, 0);
        bg.dispose();
        return bi;
    }
}
