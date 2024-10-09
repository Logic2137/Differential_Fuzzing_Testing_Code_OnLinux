



import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bug8066904 {

    public static void main(String[] args) throws IOException {
        
        byte[] corruptedBmp = { (byte) 0x42, (byte) 0x4d, (byte) 0x7e,
                (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x3e, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x28, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x64, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x64,
                (byte) 0x00, (byte) 0x40, (byte) 0x06, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff };

        
        try {
            ImageIO.read(new ByteArrayInputStream(corruptedBmp));
        } catch(Exception ex) {
            if (!(ex instanceof IOException))
                throw ex;
        }
    }
}
