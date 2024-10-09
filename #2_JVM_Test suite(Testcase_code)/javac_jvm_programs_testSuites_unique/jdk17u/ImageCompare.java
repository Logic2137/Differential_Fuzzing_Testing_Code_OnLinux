import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

public class ImageCompare {

    public static void compare(BufferedImage oldimg, BufferedImage newimg) {
        int width = oldimg.getWidth();
        int height = oldimg.getHeight();
        if (newimg.getWidth() != width || newimg.getHeight() != height) {
            throw new RuntimeException("Dimensions changed!");
        }
        Raster oldras = oldimg.getRaster();
        ColorModel oldcm = oldimg.getColorModel();
        Raster newras = newimg.getRaster();
        ColorModel newcm = newimg.getColorModel();
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                Object oldpixel = oldras.getDataElements(i, j, null);
                int oldrgb = oldcm.getRGB(oldpixel);
                int oldalpha = oldcm.getAlpha(oldpixel);
                Object newpixel = newras.getDataElements(i, j, null);
                int newrgb = newcm.getRGB(newpixel);
                int newalpha = newcm.getAlpha(newpixel);
                if (newrgb != oldrgb || newalpha != oldalpha) {
                    throw new RuntimeException("Pixels differ at " + i + ", " + j);
                }
            }
        }
    }
}
