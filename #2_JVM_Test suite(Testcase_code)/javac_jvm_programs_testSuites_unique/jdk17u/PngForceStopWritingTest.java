import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

public class PngForceStopWritingTest {

    public static void main(String[] args) throws IOException {
        OutputStream outputStream = new NullOutputStream();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        try {
            ImageIO.write(createImage(2048), "PNG", imageOutputStream);
        } catch (IOException e) {
            imageOutputStream.close();
        }
    }

    private static BufferedImage createImage(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = image.createGraphics();
        g.setPaint(new GradientPaint(0, 0, Color.blue, size, size, Color.red));
        g.fillRect(0, 0, size, size);
        g.dispose();
        return image;
    }

    static class NullOutputStream extends OutputStream {

        long count = 0;

        @Override
        public void write(int b) throws IOException {
            count++;
            if (count > 30000L) {
                throw new IOException("Force stop image writing");
            }
        }
    }
}
