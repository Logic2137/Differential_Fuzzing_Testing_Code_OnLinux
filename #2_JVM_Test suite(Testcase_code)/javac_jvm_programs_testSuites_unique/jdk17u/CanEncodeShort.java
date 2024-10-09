import java.awt.image.BufferedImage;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;

public class CanEncodeShort {

    private static final int[] types = new int[] { BufferedImage.TYPE_USHORT_565_RGB, BufferedImage.TYPE_USHORT_555_RGB };

    private static final String[] typeNames = new String[] { "TYPE_USHORT_565_RGB", "TYPE_USHORT_555_RGB" };

    public static void main(String[] args) {
        for (int i = 0; i < types.length; i++) {
            BufferedImage img = new BufferedImage(32, 32, types[i]);
            ImageTypeSpecifier spec = ImageTypeSpecifier.createFromRenderedImage(img);
            Iterator writers = ImageIO.getImageWriters(spec, "png");
            if (!writers.hasNext()) {
                throw new RuntimeException("Test failed: " + "no PNG writer found for type " + typeNames[i]);
            }
        }
    }
}
