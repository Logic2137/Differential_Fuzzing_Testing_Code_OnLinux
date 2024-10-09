



import java.io.File;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;

public class JpegRawImageTypeTest {

    public static void main(String[] args) throws Exception {

        
        String fileName = "nomarkers.jpg";
        String sep = System.getProperty("file.separator");
        String dir = System.getProperty("test.src", ".");
        String filePath = dir+sep+fileName;
        System.out.println("Test file: " + filePath);
        File imageFile = new File(filePath);

        ImageInputStream inputStream = ImageIO.
            createImageInputStream(imageFile);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(inputStream);

        if(readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(inputStream);

            ImageTypeSpecifier typeSpecifier = reader.getRawImageType(0);
            
            if (typeSpecifier == null) {
                throw new RuntimeException("ImageReader returns null raw image"
                    + " type");
            }
        }
    }
}
