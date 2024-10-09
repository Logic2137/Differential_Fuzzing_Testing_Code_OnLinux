import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class JpegMultipleEOITest {

    public static void main(String[] args) throws IOException {
        Iterator readers = ImageIO.getImageReadersByFormatName("JPEG");
        ImageReader reader = null;
        while (readers.hasNext()) {
            reader = (ImageReader) readers.next();
            if (reader.canReadRaster()) {
                break;
            }
        }
        if (reader != null) {
            String fileName = "JpegMultipleEOI.jpg";
            String sep = System.getProperty("file.separator");
            String dir = System.getProperty("test.src", ".");
            String filePath = dir + sep + fileName;
            System.out.println("Test file: " + filePath);
            File imageFile = new File(filePath);
            ImageInputStream stream = ImageIO.createImageInputStream(imageFile);
            reader.setInput(stream);
            int pageNum = 1;
            try {
                reader.getWidth(pageNum + reader.getMinIndex());
            } catch (IndexOutOfBoundsException e) {
            }
        }
    }
}
