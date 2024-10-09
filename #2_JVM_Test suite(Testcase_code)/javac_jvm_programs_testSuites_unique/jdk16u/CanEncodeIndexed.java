



import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;

public class CanEncodeIndexed {

    public static void main(String[] args) {
        BufferedImage img = new BufferedImage(32, 32,
                                              BufferedImage.TYPE_BYTE_INDEXED);

        ImageTypeSpecifier spec =
            ImageTypeSpecifier.createFromRenderedImage(img);

        Iterator writers = ImageIO.getImageWriters(spec, "jpeg");

        if (!writers.hasNext()) {
            throw new RuntimeException("Test failed: " +
                                       "no JPEG writer found for " +
                                       "image with IndexColorModel");
        }
    }
}
