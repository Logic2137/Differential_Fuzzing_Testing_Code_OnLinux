



import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NthItemNodeListTest {

    public static void main(String[] args) throws IOException {
        
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB),
                "gif", tmp);

        
        ImageInputStream in = new MemoryCacheImageInputStream(
                new ByteArrayInputStream(tmp.toByteArray()));
        ImageReader reader = ImageIO.getImageReaders(in).next();
        reader.setInput(in);

        
        IIOMetadata meta = reader.getImageMetadata(0);
        if (meta == null || !meta.isStandardMetadataFormatSupported()) {
            throw new Error("Test failure: Missing metadata");
        }
        Element root = (Element) meta.
                getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);

        NodeList nodeList = root.
                getElementsByTagName(root.getFirstChild().getNodeName());
        
        Node n = (nodeList.item(nodeList.getLength()));
    }
}

