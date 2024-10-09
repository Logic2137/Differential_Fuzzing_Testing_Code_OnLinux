import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageWriterCompressionTest {

    private static final Set<String> IGNORE_FILE_SUFFIXES = new HashSet<String>(Arrays.asList(new String[] { "bmp", "gif", "jpg", "jpeg" }));

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        final BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2d = image.createGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.scale(2.0, 2.0);
            g2d.setColor(Color.red);
            g2d.draw(new Rectangle2D.Float(10, 10, 100, 100));
            g2d.setColor(Color.blue);
            g2d.fill(new Rectangle2D.Float(12, 12, 98, 98));
            g2d.setColor(Color.green);
            g2d.setFont(new Font(Font.SERIF, Font.BOLD, 14));
            for (int i = 0; i < 15; i++) {
                g2d.drawString("Testing Compression ...", 20, 20 + i * 16);
            }
            final String[] fileSuffixes = ImageIO.getWriterFileSuffixes();
            final Set<String> testedWriterClasses = new HashSet<String>();
            for (String suffix : fileSuffixes) {
                if (!IGNORE_FILE_SUFFIXES.contains(suffix)) {
                    final Iterator<ImageWriter> itWriters = ImageIO.getImageWritersBySuffix(suffix);
                    final ImageWriter writer;
                    final ImageWriteParam writerParams;
                    if (itWriters.hasNext()) {
                        writer = itWriters.next();
                        if (testedWriterClasses.add(writer.getClass().getName())) {
                            writerParams = writer.getDefaultWriteParam();
                            if (writerParams.canWriteCompressed()) {
                                testCompression(image, writer, writerParams, suffix);
                            }
                        }
                    } else {
                        throw new RuntimeException("Unable to get writer !");
                    }
                }
            }
        } catch (IOException ioe) {
            throw new RuntimeException("IO failure", ioe);
        } finally {
            g2d.dispose();
        }
    }

    private static void testCompression(final BufferedImage image, final ImageWriter writer, final ImageWriteParam writerParams, final String suffix) throws IOException {
        System.out.println("Compression types: " + Arrays.toString(writerParams.getCompressionTypes()));
        try {
            writerParams.setCompressionMode(ImageWriteParam.MODE_DISABLED);
            saveImage(image, writer, writerParams, "disabled", suffix);
        } catch (Exception e) {
            System.out.println("CompressionMode Disabled not supported: " + e.getMessage());
        }
        try {
            writerParams.setCompressionMode(ImageWriteParam.MODE_DEFAULT);
            saveImage(image, writer, writerParams, "default", suffix);
        } catch (Exception e) {
            System.out.println("CompressionMode Default not supported: " + e.getMessage());
        }
        writerParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writerParams.setCompressionType(selectCompressionType(suffix, writerParams.getCompressionTypes()));
        System.out.println("Selected Compression type: " + writerParams.getCompressionType());
        long prev = Long.MAX_VALUE;
        for (int i = 10; i >= 0; i--) {
            float quality = 0.1f * i;
            writerParams.setCompressionQuality(quality);
            long len = saveImage(image, writer, writerParams, String.format("explicit-%.1f", quality), suffix);
            if (len <= 0) {
                throw new RuntimeException("zero file length !");
            } else if (len > prev) {
                throw new RuntimeException("Incorrect file length: " + len + " larger than previous: " + prev + " !");
            }
            prev = len;
        }
    }

    private static String selectCompressionType(final String suffix, final String[] types) {
        switch(suffix) {
            case "tif":
            case "tiff":
                return "LZW";
            default:
                return types[0];
        }
    }

    private static long saveImage(final BufferedImage image, final ImageWriter writer, final ImageWriteParam writerParams, final String mode, final String suffix) throws IOException {
        final File imgFile = new File("WriterCompressionTest-" + mode + '.' + suffix);
        System.out.println("Writing file: " + imgFile.getAbsolutePath());
        final ImageOutputStream imgOutStream = ImageIO.createImageOutputStream(new FileOutputStream(imgFile));
        try {
            writer.setOutput(imgOutStream);
            writer.write(null, new IIOImage(image, null, null), writerParams);
        } finally {
            imgOutStream.close();
        }
        return imgFile.length();
    }
}
