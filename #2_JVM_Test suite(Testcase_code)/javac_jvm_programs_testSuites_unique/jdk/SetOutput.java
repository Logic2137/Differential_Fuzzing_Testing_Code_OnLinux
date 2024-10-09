



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.sun.imageio.plugins.png.PNGImageWriter;

public class SetOutput {

    public static void main(String[] args) throws IOException {
        ImageWriter iw = new PNGImageWriter(null);
        File f = File.createTempFile("imageio", "tmp");
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(f)) {
            iw.setOutput(ios);
        } finally {
            Files.delete(f.toPath());
        }
    }
}
