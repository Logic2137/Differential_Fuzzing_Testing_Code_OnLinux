



import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ByteBinaryTest {

    private static final int[] expectedVals =
        { 0xffffffff, 0xff000000, 0xffffffff };

    public static void main(String[] args) {
        BufferedImage bi = new BufferedImage(100, 100,
                                             BufferedImage.TYPE_BYTE_BINARY);

        Graphics g = bi.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 100, 100);
        g.setColor(Color.black);
        g.fillRect(20, 20, 40, 40);
        g.setColor(Color.white);
        g.fillRect(25, 25, 25, 25);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean success;

        try {
            success = ImageIO.write(bi, "jpeg", baos);
        } catch (IOException ioe) {
            throw new RuntimeException("Could not write JPEG to stream");
        }

        if (!success) {
            throw new RuntimeException("Could not find valid JPEG writer...");
        }

        byte[] bytearr = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytearr);
        BufferedImage bi2 = null;

        try {
            bi2 = ImageIO.read(bais);
        } catch (IOException ioe) {
            throw new RuntimeException("Could not read JPEG stream");
        }

        int[] actualVals = new int[3];

        actualVals[0] = bi2.getRGB(27, 5);
        actualVals[1] = bi2.getRGB(27, 22);
        actualVals[2] = bi2.getRGB(35, 35);

        for (int i = 0; i < actualVals.length; i++) {
            if (actualVals[i] != expectedVals[i]) {
                throw new RuntimeException("Pixel mismatch at index: " + i);
            }
        }
    }
}
