import java.io.ByteArrayInputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BMPPixelSpacingTest {

    public static void main(String[] args) throws Exception {
        byte[] bmpHeaderData = { (byte) 0x42, (byte) 0x4d, (byte) 0x7e, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x28, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x64, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff };
        ImageInputStream imageInput = ImageIO.createImageInputStream(new ByteArrayInputStream(bmpHeaderData));
        for (Iterator<ImageReader> it = ImageIO.getImageReaders(imageInput); it.hasNext(); ) {
            ImageReader reader = it.next();
            reader.setInput(imageInput);
            IIOMetadata metadata = reader.getImageMetadata(0);
            Node rootNode = metadata.getAsTree("javax_imageio_1.0");
            NodeList nl = rootNode.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if ((node.getNodeName()).equals("Dimension")) {
                    NodeList cl = node.getChildNodes();
                    int horizontalNodeIndex = 1;
                    int verticalNodeIndex = 2;
                    Node horizontalNode = cl.item(horizontalNodeIndex);
                    Node verticalNode = cl.item(verticalNodeIndex);
                    NamedNodeMap horizontalAttr = horizontalNode.getAttributes();
                    NamedNodeMap verticalAttr = verticalNode.getAttributes();
                    int attributeIndex = 0;
                    Node horizontalValue = horizontalAttr.item(attributeIndex);
                    Node verticalValue = verticalAttr.item(attributeIndex);
                    float horizontalNodeValue = Float.parseFloat((horizontalValue.getNodeValue()));
                    float verticalNodeValue = Float.parseFloat((verticalValue.getNodeValue()));
                    float expectedHorizontalValue, expectedVerticalValue;
                    expectedHorizontalValue = expectedVerticalValue = 1000.0F / 2;
                    if ((Float.compare(horizontalNodeValue, expectedHorizontalValue) != 0) || (Float.compare(verticalNodeValue, expectedVerticalValue) != 0)) {
                        throw new RuntimeException("Invalid pixel spacing");
                    }
                }
            }
        }
    }
}
