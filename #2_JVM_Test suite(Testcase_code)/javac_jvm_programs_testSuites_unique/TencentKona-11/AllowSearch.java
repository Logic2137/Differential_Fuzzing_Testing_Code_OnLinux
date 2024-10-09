import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.png.PNGImageReader;

public class AllowSearch {

    private static void test(ImageReader reader, String format) throws IOException {
        boolean gotISE = false;
        File f = null;
        ImageInputStream stream = null;
        try {
            f = File.createTempFile("imageio", ".tmp");
            stream = ImageIO.createImageInputStream(f);
            reader.setInput(stream, true);
            try {
                int numImages = reader.getNumImages(true);
            } catch (IOException ioe) {
                gotISE = false;
            } catch (IllegalStateException ise) {
                gotISE = true;
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
            reader.dispose();
            if (f != null) {
                Files.delete(f.toPath());
            }
        }
        if (!gotISE) {
            throw new RuntimeException("Failed to get desired exception for " + format + " reader!");
        }
    }

    public static void main(String[] args) throws IOException {
        ImageReader gifReader = new GIFImageReader(null);
        ImageReader jpegReader = new JPEGImageReader(null);
        ImageReader pngReader = new PNGImageReader(null);
        test(gifReader, "GIF");
        test(jpegReader, "JPEG");
        test(pngReader, "PNG");
    }
}
