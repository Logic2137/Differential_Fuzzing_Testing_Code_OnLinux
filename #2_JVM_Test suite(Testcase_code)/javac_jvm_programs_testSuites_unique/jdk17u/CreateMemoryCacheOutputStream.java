import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

public class CreateMemoryCacheOutputStream {

    public static void main(String[] args) {
        ImageIO.setUseCache(false);
        OutputStream os = new ByteArrayOutputStream();
        ImageOutputStream stream = null;
        try {
            stream = ImageIO.createImageOutputStream(os);
        } catch (Exception e) {
            throw new RuntimeException("Got exception " + e);
        }
        if (stream == null) {
            throw new RuntimeException("Got null stream!");
        }
    }
}
