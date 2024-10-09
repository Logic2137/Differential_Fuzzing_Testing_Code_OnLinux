



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class WriterResetTest {
    public static void main(String[] args) throws IOException {
        ImageWriter w = ImageIO.getImageWritersByFormatName("GIF").next();
        if (w == null) {
            throw new RuntimeException("No writers available!");
        }

        ByteArrayOutputStream baos =
            new ByteArrayOutputStream();

        ImageOutputStream ios =
            ImageIO.createImageOutputStream(baos);

        w.setOutput(ios);

        BufferedImage img = createTestImage();

        try {
            w.reset();
            w.write(img);
        } catch (IllegalStateException e) {
            System.out.println("Test passed");
        } catch (Throwable e) {
            throw new RuntimeException("Test failed", e);
        }
    }

    private static BufferedImage createTestImage() {
        BufferedImage img = new BufferedImage(100, 100,
                                              BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 100, 100);
        g.setColor(Color.black);
        g.fillRect(20, 20, 60, 60);

        return img;
    }
}
