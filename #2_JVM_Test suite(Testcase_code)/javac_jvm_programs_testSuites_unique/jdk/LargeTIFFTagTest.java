



import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

public class LargeTIFFTagTest {
    public static void main(String[] args) throws IOException {
        
        
        int length = 1024024;
        byte[] ba = new byte[length];
        
        
        byte[] header = new byte[] { (byte)0x49, (byte) 0x49,
                (byte)0x2a, (byte)0x00, (byte)0x08, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00,
                (byte)0x73, (byte)0x87, (byte)0x07, (byte)0x00,
                (byte)0x02, (byte)0xA0, (byte)0x0F, (byte)0x00,
                (byte)0x16, (byte)0x00, (byte)0x00, (byte)0x00};
        
        for (int i = 0; i < 22; i++) {
            ba[i] = header[i];
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        ImageInputStream stream = ImageIO.createImageInputStream(bais);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);

        if(readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(stream);
            try {
                reader.readAll(0, null);
            } catch (IllegalArgumentException e) {
                
                
                System.out.println(e.toString());
                System.out.println("Caught IllegalArgumentException ignore it");
            }
        } else {
            throw new RuntimeException("No readers available for TIFF format");
        }
    }
}
