import java.awt.*;
import java.awt.image.BufferedImage;

public class DeviceBounds {

    public static void main(String[] args) {
        BufferedImage[] images = new BufferedImage[] { new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR), new BufferedImage(400, 400, BufferedImage.TYPE_3BYTE_BGR), new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR) };
        int count = 0;
        for (BufferedImage i : images) {
            Graphics2D g = i.createGraphics();
            Rectangle[] bounds = new Rectangle[images.length];
            bounds[count] = g.getDeviceConfiguration().getBounds();
            System.out.println(bounds[count]);
            g.dispose();
            if (bounds[count].width != Integer.MAX_VALUE) {
                throw new RuntimeException("Wrong getBounds");
            }
            count++;
        }
    }
}
