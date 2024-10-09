import java.io.File;
import javax.imageio.ImageIO;

public class ImageIOWriteNull {

    public static void main(String[] args) {
        try {
            ImageIO.write(null, null, (File) null);
            throw new RuntimeException("Failed to get IAE!");
        } catch (IllegalArgumentException iae) {
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception: " + e);
        }
    }
}
