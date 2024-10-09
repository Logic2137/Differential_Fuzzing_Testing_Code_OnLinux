import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageOutputStreamSpi;

public class NullStreamCheckTest {

    private static final IIORegistry localRegistry = IIORegistry.getDefaultInstance();

    static LocalOutputStream outputStream = new LocalOutputStream();

    static LocalInputStream inputStream = new LocalInputStream();

    static final int width = 50, height = 50;

    static BufferedImage inputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    private static boolean verifyOutputExceptionMessage(IOException ex) {
        String message = ex.getMessage();
        return (!message.equals("Can't create an ImageOutputStream!"));
    }

    private static boolean verifyInputExceptionMessage(IOException ex) {
        String message = ex.getMessage();
        return (!message.equals("Can't create an ImageInputStream!"));
    }

    private static void verifyFileWrite() throws IOException {
        File outputTestFile = File.createTempFile("outputTestFile", ".png");
        try {
            ImageIO.write(inputImage, "png", outputTestFile);
        } catch (IOException ex) {
            if (verifyOutputExceptionMessage(ex))
                throw ex;
        } finally {
            outputTestFile.delete();
        }
    }

    private static void verifyStreamWrite() throws IOException {
        try {
            ImageIO.write(inputImage, "png", outputStream);
        } catch (IOException ex) {
            if (verifyOutputExceptionMessage(ex))
                throw ex;
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                throw ex;
            }
        }
    }

    private static void verifyFileRead() throws IOException {
        File inputTestFile = File.createTempFile("inputTestFile", ".png");
        try {
            ImageIO.read(inputTestFile);
        } catch (IOException ex) {
            if (verifyInputExceptionMessage(ex))
                throw ex;
        } finally {
            inputTestFile.delete();
        }
    }

    private static void verifyStreamRead() throws IOException {
        try {
            ImageIO.read(inputStream);
        } catch (IOException ex) {
            if (verifyInputExceptionMessage(ex))
                throw ex;
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                throw ex;
            }
        }
    }

    private static void verifyUrlRead() throws IOException {
        URL url;
        File inputTestUrlFile = File.createTempFile("inputTestFile", ".png");
        try {
            try {
                url = inputTestUrlFile.toURI().toURL();
            } catch (MalformedURLException ex) {
                throw ex;
            }
            try {
                ImageIO.read(url);
            } catch (IOException ex) {
                if (verifyInputExceptionMessage(ex))
                    throw ex;
            }
        } finally {
            inputTestUrlFile.delete();
        }
    }

    public static void main(String[] args) throws IOException, MalformedURLException {
        localRegistry.deregisterAll(ImageOutputStreamSpi.class);
        verifyFileWrite();
        verifyStreamWrite();
        localRegistry.deregisterAll(ImageInputStreamSpi.class);
        verifyFileRead();
        verifyStreamRead();
        verifyUrlRead();
    }

    static class LocalOutputStream extends OutputStream {

        @Override
        public void write(int i) throws IOException {
        }
    }

    static class LocalInputStream extends InputStream {

        @Override
        public int read() throws IOException {
            return 0;
        }
    }
}
