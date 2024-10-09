



import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class BmpDefaultImageMetadataTest {
    ImageWriter writer = null;
    IIOMetadata imageData = null;
    ImageWriteParam writeParam = null;
    BufferedImage bimg = null;

    public BmpDefaultImageMetadataTest(String format) {
        try {
            bimg = new BufferedImage(200, 200, bimg.TYPE_INT_RGB);
            Graphics gg = bimg.getGraphics();
            gg.setColor(Color.red);
            gg.fillRect(50, 50, 100, 100);

            Iterator it = ImageIO.getImageWritersByFormatName(format);
            if (it.hasNext()) {
                writer = (ImageWriter) it.next();
            }
            if (writer == null) {
                throw new RuntimeException("No writer available for the given format."
                                           + " Test failed.");
            }
            writeParam = writer.getDefaultWriteParam();

            System.out.println("Testing Image Metadata for "+format+"\n");
            imageData = writer.getDefaultImageMetadata(new ImageTypeSpecifier(bimg), writeParam);
            if (imageData == null) {
                System.out.println("return value is null. No default image metadata is associated with "+format+" writer");
                throw new RuntimeException("Default image metadata is null."
                                           + " Test failed.");
            }
            int j = 0;
            String imageDataNames[] = null;
            if(imageData != null) {
                System.out.println("Is standard metadata format supported (Image) ? "+
                                   imageData.isStandardMetadataFormatSupported() );
                imageDataNames = imageData.getMetadataFormatNames();
                System.out.println("\nAll supported Metadata Format Names\n");
                if(imageDataNames!=null){
                    for(j=0; j<imageDataNames.length; j++)  {
                        System.out.println("FORMAT NAME: "+imageDataNames[j]);
                        if (imageDataNames[j].equals(imageData.getNativeMetadataFormatName())) {
                            System.out.println("This is a Native Metadata format\n");
                        } else {
                            System.out.println("\n");
                        }
                        System.out.println("");
                        System.out.println("IIOImageMetadata DOM tree for "+imageDataNames[j]);
                        System.out.println("");
                        Node imageNode = imageData.getAsTree(imageDataNames[j]);
                        displayMetadata(imageNode);
                        System.out.println("\n\n");
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Exception was thrown."
                                       + " Test failed.");
        }
    }

    public void displayMetadata(Node root) {
        displayMetadata(root, 0);
    }

    void indent(int level) {
        for (int i = 0; i < level; i++) {
            System.out.print(" ");
        }
    }

    void displayMetadata(Node node, int level) {
        indent(level); 
        System.out.print("<" + node.getNodeName());
        NamedNodeMap map = node.getAttributes();
        if (map != null) { 
            int length = map.getLength();
            for (int i = 0; i < length; i++) {
                Node attr = map.item(i);
                System.out.print(" " + attr.getNodeName() +
                                 "=\"" + attr.getNodeValue() + "\"");
            }
        }
        Node child = node.getFirstChild();

        if (node.getNodeValue() != null && !node.getNodeValue().equals("") ) {
            System.out.println(">");
            indent(level);
            System.out.println(node.getNodeValue());
            indent(level); 
            System.out.println("</" + node.getNodeName() + ">");
        } else  if (child != null) {
            System.out.println(">"); 
            while (child != null) { 
                displayMetadata(child, level + 1);
                child = child.getNextSibling();
            }
            indent(level); 
            System.out.println("</" + node.getNodeName() + ">");
        } else {
            System.out.println("/>");
        }
    }

    public static void main(String args[]) {
        BmpDefaultImageMetadataTest test = new BmpDefaultImageMetadataTest("bmp");
    }
}
