import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.stream.ImageInputStream;
import java.awt.Color;
import java.awt.Graphics2D;
import java.nio.file.Files;

public class ReadAbortTest implements IIOReadProgressListener {

    ImageReader reader = null;

    ImageInputStream iis = null;

    BufferedImage bimg = null;

    File file;

    boolean startAbort = false;

    boolean startAborted = false;

    boolean progressAbort = false;

    boolean progressAborted = false;

    Color srccolor = Color.red;

    int width = 100;

    int heght = 100;

    public ReadAbortTest(String format) throws Exception {
        try {
            System.out.println("Test for format " + format);
            bimg = new BufferedImage(width, heght, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bimg.createGraphics();
            g.setColor(srccolor);
            g.fillRect(0, 0, width, heght);
            g.dispose();
            file = File.createTempFile("src_", "." + format, new File("."));
            ImageIO.write(bimg, format, file);
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            Iterator iter = ImageIO.getImageReaders(iis);
            while (iter.hasNext()) {
                reader = (ImageReader) iter.next();
                break;
            }
            reader.setInput(iis);
            reader.addIIOReadProgressListener(this);
            startAbort = true;
            bimg = reader.read(0);
            startAbort = false;
            progressAbort = true;
            bimg = reader.read(0);
            progressAbort = false;
            iis.close();
            if (!(startAborted && progressAborted)) {
                throw new RuntimeException("All IIOReadProgressListener abort" + " requests are not processed for format " + format);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            Files.delete(file.toPath());
        }
    }

    @Override
    public void imageStarted(ImageReader source, int imageIndex) {
        System.out.println("imageStarted called");
        if (startAbort) {
            source.abort();
        }
    }

    @Override
    public void imageProgress(ImageReader source, float percentageDone) {
        System.out.println("imageProgress called");
        if (progressAbort) {
            source.abort();
        }
    }

    @Override
    public void readAborted(ImageReader source) {
        System.out.println("readAborted called");
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
            new ReadAbortTest(format);
        }
    }

    @Override
    public void imageComplete(ImageReader source) {
    }

    @Override
    public void sequenceStarted(ImageReader reader, int i) {
    }

    @Override
    public void sequenceComplete(ImageReader reader) {
    }

    @Override
    public void thumbnailStarted(ImageReader reader, int i, int i1) {
    }

    @Override
    public void thumbnailProgress(ImageReader reader, float f) {
    }

    @Override
    public void thumbnailComplete(ImageReader reader) {
    }
}
