import java.awt.image.BufferedImage;
import javax.imageio.ImageTypeSpecifier;

public class ImageTypeSpecifierBitsPerBand {

    public static void main(String[] args) {
        int biType = BufferedImage.TYPE_USHORT_565_RGB;
        ImageTypeSpecifier type = ImageTypeSpecifier.createFromBufferedImageType(biType);
        int b0 = type.getBitsPerBand(0);
        int b1 = type.getBitsPerBand(1);
        int b2 = type.getBitsPerBand(2);
        if (b0 != 5 || b1 != 6 || b2 != 5) {
            throw new RuntimeException("Got incorrect bits per band value!");
        }
        boolean gotIAE = false;
        try {
            int b3 = type.getBitsPerBand(3);
        } catch (IllegalArgumentException e) {
            gotIAE = true;
        }
        if (!gotIAE) {
            throw new RuntimeException("Failed to get IllegalArgumentException for band == 3!");
        }
    }
}
