



import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class ReadLongLanguageTagTest {

    private static BufferedImage img;
    private static ImageWriter writer;
    private static ImageWriteParam param;
    private static IIOMetadata metadata;
    private static byte[] imageByteArray;

    private static void initialize(int type) {
        int width = 1;
        int height = 1;
        img = new BufferedImage(width, height, type);
        Graphics2D g2D = img.createGraphics();
        g2D.setColor(new Color(255, 255, 255));
        g2D.fillRect(0, 0, width, width);
        g2D.dispose();

        Iterator<ImageWriter> iterWriter =
                ImageIO.getImageWritersBySuffix("png");
        writer = iterWriter.next();

        param = writer.getDefaultWriteParam();
        ImageTypeSpecifier specifier =
                ImageTypeSpecifier.
                        createFromBufferedImageType(type);
        metadata = writer.getDefaultImageMetadata(specifier, param);
    }

    private static void createITXTNode()
            throws IIOInvalidTreeException {
        IIOMetadataNode iTXt_Entry = new IIOMetadataNode("iTXtEntry");
        iTXt_Entry.setAttribute("keyword", "ImageIO");
        iTXt_Entry.setAttribute("compressionFlag", "FALSE");
        iTXt_Entry.setAttribute("compressionMethod", "0");
        iTXt_Entry.setAttribute("languageTag", "en-Java" +
                "JavaJavaJavaJavaJavaJavaJavaJavaJavaJavaJavaJava" +
                "JavaJavaJavaJavaJavaJavaJavaJavaJavaJavaJavaJava");
        iTXt_Entry.setAttribute("translatedKeyword", "");
        iTXt_Entry.setAttribute("text", "");

        IIOMetadataNode iTXt = new IIOMetadataNode("iTXt");
        iTXt.appendChild(iTXt_Entry);
        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_png_1.0");
        root.appendChild(iTXt);
        metadata.mergeTree("javax_imageio_png_1.0", root);
    }

    private static void writeImage() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);
        writer.write(metadata, new IIOImage(img, null, metadata), param);
        writer.dispose();

        baos.flush();
        imageByteArray = baos.toByteArray();
        baos.close();
    }

    private static void readITXTChunk() throws IOException {
        initialize(BufferedImage.TYPE_BYTE_GRAY);
        
        createITXTNode();

        writeImage();

        InputStream input= new ByteArrayInputStream(imageByteArray);
        try {
            Iterator<ImageReader> iterReader =
                    ImageIO.getImageReadersBySuffix("PNG");
            if (iterReader.hasNext()) {
                ImageReader pngImageReader = iterReader.next();
                ImageReadParam param = pngImageReader.getDefaultReadParam();
                ImageInputStream imageStream =
                        ImageIO.createImageInputStream(input);
                pngImageReader.setInput(imageStream, false, false);
                pngImageReader.read(0, param);
            } else {
                throw new RuntimeException("Requested PNGImageReader" +
                        " not available");
            }
        } finally {
            input.close();
        }
    }

    public static void main(String[] args) throws IOException {
        
        readITXTChunk();
    }
}
