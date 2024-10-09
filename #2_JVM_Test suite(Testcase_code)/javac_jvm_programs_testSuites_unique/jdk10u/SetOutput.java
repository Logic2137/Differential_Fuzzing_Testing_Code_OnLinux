



import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.sun.imageio.plugins.png.PNGImageWriter;

public class SetOutput {

    public static void main(String[] args) throws IOException {
        ImageWriter iw = new PNGImageWriter(null);
        File f = File.createTempFile("imageio", "tmp");
        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        try {
            iw.setOutput(ios);
        } catch (NullPointerException npe) {
            f.delete();
            throw new RuntimeException("Got NullPointerException!");
        }
        f.delete();
    }
}
