import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import javax.imageio.plugins.tiff.*;

public class IFDTest {

    private ImageWriter getTIFFWriter() {
        java.util.Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("TIFF");
        if (!writers.hasNext()) {
            throw new RuntimeException("No readers available for TIFF format");
        }
        return writers.next();
    }

    private void writeImage() throws Exception {
        File file = File.createTempFile("IFDTest", "tif", new File("."));
        file.deleteOnExit();
        OutputStream s = new BufferedOutputStream(new FileOutputStream("test.tiff"));
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(s)) {
            ImageWriter writer = getTIFFWriter();
            writer.setOutput(ios);
            BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
            Graphics g = img.getGraphics();
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, 20, 20);
            g.dispose();
            IIOMetadata metadata = writer.getDefaultImageMetadata(new ImageTypeSpecifier(img), writer.getDefaultWriteParam());
            TIFFDirectory dir = TIFFDirectory.createFromMetadata(metadata);
            int type = TIFFTag.TIFF_IFD_POINTER;
            int nTag = ExifParentTIFFTagSet.TAG_EXIF_IFD_POINTER;
            TIFFTag tag = new TIFFTag("Exif IFD", nTag, 1 << type);
            TIFFTagSet[] sets = { ExifTIFFTagSet.getInstance() };
            TIFFField f = new TIFFField(tag, type, 42L, new TIFFDirectory(sets, tag));
            dir.addTIFFField(f);
            writer.write(new IIOImage(img, null, dir.getAsMetadata()));
            ios.flush();
            writer.dispose();
        } finally {
            s.close();
            file.delete();
        }
    }

    public static void main(String[] args) throws Exception {
        (new IFDTest()).writeImage();
    }
}
