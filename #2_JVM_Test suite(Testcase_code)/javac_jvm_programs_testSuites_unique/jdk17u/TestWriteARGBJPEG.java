import java.io.IOException;
import java.util.Iterator;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class TestWriteARGBJPEG {

    public static void main(String[] args) throws IOException {
        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean ret = ImageIO.write(bi, "jpeg", baos);
        System.out.println("ImageIO.write(..) returned " + ret);
        ImageTypeSpecifier its = new ImageTypeSpecifier(bi);
        Iterator<ImageWriter> writers = ImageIO.getImageWriters(its, "jpeg");
        boolean hasWriter = writers.hasNext();
        if (writers.hasNext()) {
            System.out.println("A writer was found.");
            ImageWriter iw = writers.next();
            MemoryCacheImageOutputStream mos = new MemoryCacheImageOutputStream(baos);
            iw.setOutput(mos);
            iw.write(bi);
        }
        ImageWriter iw = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriterSpi iwSpi = iw.getOriginatingProvider();
        boolean canEncode = iwSpi.canEncodeImage(bi);
        System.out.println("SPI canEncodeImage returned " + canEncode);
        try {
            MemoryCacheImageOutputStream mos = new MemoryCacheImageOutputStream(baos);
            iw.setOutput(mos);
            iw.write(bi);
        } catch (IOException e) {
            if (canEncode) {
                throw e;
            }
        }
    }
}
