

 

import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

public class ImageCompressionTypesTest {

    static ImageWriter writer = null;

    public ImageCompressionTypesTest(String format) {
        Iterator it = ImageIO.getImageWritersByFormatName(format);
        while (it.hasNext()) {
            writer = (ImageWriter) it.next();
            break;
        }
        ImageWriteParam param = writer.getDefaultWriteParam();

        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        System.out.println("Checking compression types for : " + format);
        String compTypes[] = param.getCompressionTypes();
        if (compTypes.length > 1) {
            for (int i = 0; i < compTypes.length; i++) {
                for (int j = i + 1; j < compTypes.length; j++) {
                    if (compTypes[i].equalsIgnoreCase(compTypes[j])) {
                        throw new RuntimeException("Duplicate compression"
                                + " type exists for image format " + format);
                    }
                }
            }
        }
    }

    public static void main(String args[]) {
        final String[] formats = {"bmp", "png", "gif", "jpg", "tiff"};
        for (String format : formats) {
            new ImageCompressionTypesTest(format);
        }
    }
}

