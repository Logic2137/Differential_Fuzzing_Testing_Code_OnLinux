



import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ReadBITMAPV3INFOHEADERTest {
    public static void main(String[] args) throws IOException {
        String dir = System.getProperty("test.src");
        String sep = System.getProperty("file.separator");
        
        ImageIO.read(new File(dir + sep + "DIB_size-56_ARGB_16bits.bmp"));
        ImageIO.read(new File(dir + sep + "DIB_size-56_RGB_16bits.bmp"));
        ImageIO.read(new File(dir + sep + "DIB_size-56_XRGB_32bits.bmp"));
    }
}

