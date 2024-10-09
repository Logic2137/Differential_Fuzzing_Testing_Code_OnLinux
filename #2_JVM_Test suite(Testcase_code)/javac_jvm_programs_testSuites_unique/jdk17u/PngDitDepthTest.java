import org.w3c.dom.Node;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.util.Iterator;

public class PngDitDepthTest {

    public static void main(String[] args) throws IIOInvalidTreeException {
        Iterator iter = ImageIO.getImageWritersByFormatName("png");
        ImageWriter writer = (ImageWriter) iter.next();
        ColorModel colorModel = ColorModel.getRGBdefault();
        SampleModel sampleModel = colorModel.createCompatibleSampleModel(640, 480);
        IIOMetadata metaData = writer.getDefaultImageMetadata(new ImageTypeSpecifier(colorModel, sampleModel), null);
        String formatName = metaData.getNativeMetadataFormatName();
        Node metaDataNode = metaData.getAsTree(formatName);
        try {
            metaData.setFromTree(formatName, metaDataNode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        metaDataNode = metaData.getAsTree(formatName);
        metaData.setFromTree(formatName, metaDataNode);
    }
}
