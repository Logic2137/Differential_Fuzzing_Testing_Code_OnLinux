



import java.awt.image.BufferedImage;

import javax.imageio.IIOImage;

public class IIOImageConstructor {

    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(1, 1,
                                                BufferedImage.TYPE_INT_RGB);
        try {
            IIOImage iioi = new IIOImage(image, null, null);
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException
                ("IIOImage constructor taking a RenderedImage fails!");
        }
    }
}
