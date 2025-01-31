import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GIFCodeSizeTest {

    static final byte[] DATA = { (byte) 0x47, (byte) 0x49, (byte) 0x46, (byte) 0x38, (byte) 0x37, (byte) 0x61, (byte) 0x02, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x80, (byte) 0x00, (byte) 0x80, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2c, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x02, (byte) 0x84, (byte) 0x51, (byte) 0x00, (byte) 0x3b };

    public static void main(String[] args) {
        try {
            ImageIO.read(new ByteArrayInputStream(DATA));
        } catch (IOException e) {
        }
    }
}
