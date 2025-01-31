



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;

public class RotatedTextTest
{
    static final int size = 720;
    static final Font fnt = new Font(Font.SERIF, Font.PLAIN, 12);
    static final String text = "The quick brown fox jumps over the lazy dog";

    static void drawRotatedText(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        g2d.setFont(fnt);

        FontMetrics metrics = g2d.getFontMetrics();
        Rectangle2D bounds = metrics.getStringBounds(text, g2d);


        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, size, size);

        g2d.setColor(Color.white);

        float[] angles = new float[] { 0, 15, 45, 60, 90, -45, -90, -135, -180, 135, };

        for (float a : angles) {
            Graphics2D rotated = (Graphics2D)g2d.create();

            rotated.translate(size/2, size/2);
            rotated.rotate(Math.toRadians(a));
            rotated.translate(30, 0);

            int width_original = metrics.stringWidth(text);
            int width_rotated = rotated.getFontMetrics().stringWidth(text);

            rotated.drawString(text, 0, 0);
            rotated.drawString(String.format("  %.0f", a), width_original, 0);

            rotated.setColor(Color.green);
            rotated.draw(bounds);

            System.out.printf("Angle: %.0f, width diff: %d\n", a, (width_rotated - width_original));

            rotated.dispose();

            if (width_rotated != width_original) {
                throw new RuntimeException("Test failed for angle " + a);
            }
        }
    }

    public static void main(String args[])
    {
        final BufferedImage dst = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics g = dst.createGraphics();

        try {
            drawRotatedText(g);
        } finally {
            g.dispose();
            writeToFile(dst);
        }
    }

    static final String title;
    static final String file;

    static {
        String vendorName = System.getProperty("java.vendor");
        String version = System.getProperty("java.version");
        String runtimeName = System.getProperty("java.runtime.name");


        int index = runtimeName.indexOf(" Runtime Environment");
        runtimeName = runtimeName.substring(0, index).trim();

        title = vendorName + ", " + runtimeName + ", " + version;
        file = "rotated-text-" + title.replace(", ", "-")
                .replace(" ", "-") + ".png";
    }

    private static void writeToFile(final BufferedImage bi) {
        File imageFile = new File(file);
        FileOutputStream imageOutStream;
        BufferedOutputStream imageBOS = null;
        try {
            imageOutStream = new FileOutputStream(imageFile);
            imageBOS = new BufferedOutputStream(imageOutStream);

            ImageIO.setUseCache(false);
            ImageIO.write(bi, "png", imageBOS);

            imageBOS.close();
            imageOutStream.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
