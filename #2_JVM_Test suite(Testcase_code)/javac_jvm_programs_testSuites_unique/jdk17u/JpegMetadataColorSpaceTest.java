import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class JpegMetadataColorSpaceTest {

    public static void main(String[] args) throws IOException {
        String fileName = "nomarkers.jpg";
        String sep = System.getProperty("file.separator");
        String dir = System.getProperty("test.src", ".");
        String filePath = dir + sep + fileName;
        System.out.println("Test file: " + filePath);
        File file = new File(filePath);
        ImageInputStream stream = ImageIO.createImageInputStream(file);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
        if (readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(stream);
            IIOMetadata metadata = reader.getImageMetadata(0);
            IIOMetadataNode standardTree = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
            IIOMetadataNode colorSpaceType = (IIOMetadataNode) standardTree.getElementsByTagName("ColorSpaceType").item(0);
            String colorSpaceName = colorSpaceType.getAttribute("name");
            if (colorSpaceName.equals("RGB"))
                throw new RuntimeException("Identified incorrect ColorSpace");
        }
    }
}
