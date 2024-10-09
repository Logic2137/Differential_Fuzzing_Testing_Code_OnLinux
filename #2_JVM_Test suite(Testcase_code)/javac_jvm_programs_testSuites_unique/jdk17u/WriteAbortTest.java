import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.Color;
import java.awt.Graphics2D;
import java.nio.file.Files;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.stream.ImageOutputStream;

public class WriteAbortTest implements IIOWriteProgressListener {

    ImageWriter writer = null;

    ImageOutputStream ios = null;

    BufferedImage bimg = null;

    File file;

    boolean startAbort = false;

    boolean startAborted = false;

    boolean progressAbort = false;

    boolean progressAborted = false;

    Color srccolor = Color.red;

    int width = 100;

    int heght = 100;

    public WriteAbortTest(String format) throws Exception {
        try {
            System.out.println("Test for format " + format);
            bimg = new BufferedImage(width, heght, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bimg.createGraphics();
            g.setColor(srccolor);
            g.fillRect(0, 0, width, heght);
            g.dispose();
            file = File.createTempFile("src_", "." + format, new File("."));
            ImageInputStream ios = ImageIO.createImageOutputStream(file);
            ImageWriter writer = ImageIO.getImageWritersByFormatName(format).next();
            writer.setOutput(ios);
            writer.addIIOWriteProgressListener(this);
            startAbort = true;
            writer.write(bimg);
            startAbort = false;
            progressAbort = true;
            writer.write(bimg);
            progressAbort = false;
            ios.close();
            if (!(startAborted && progressAborted)) {
                throw new RuntimeException("All IIOWriteProgressListener abort" + " requests are not processed for format " + format);
            }
        } finally {
            Files.delete(file.toPath());
        }
    }

    @Override
    public void imageStarted(ImageWriter source, int imageIndex) {
        System.out.println("imageStarted called");
        if (startAbort) {
            source.abort();
        }
    }

    @Override
    public void imageProgress(ImageWriter source, float percentageDone) {
        System.out.println("imageProgress called");
        if (progressAbort) {
            source.abort();
        }
    }

    @Override
    public void writeAborted(ImageWriter source) {
        System.out.println("writeAborted called");
        if (startAbort) {
            System.out.println("imageStarted aborted ");
            startAborted = true;
        }
        if (progressAbort) {
            System.out.println("imageProgress aborted ");
            progressAborted = true;
        }
    }

    public static void main(String[] args) throws Exception {
        final String[] formats = { "bmp", "png", "gif", "jpg", "tif" };
        for (String format : formats) {
            new WriteAbortTest(format);
        }
    }

    @Override
    public void imageComplete(ImageWriter source) {
    }

    @Override
    public void thumbnailStarted(ImageWriter source, int imageIndex, int thumbnailIndex) {
    }

    @Override
    public void thumbnailProgress(ImageWriter source, float percentageDone) {
    }

    @Override
    public void thumbnailComplete(ImageWriter source) {
    }
}
