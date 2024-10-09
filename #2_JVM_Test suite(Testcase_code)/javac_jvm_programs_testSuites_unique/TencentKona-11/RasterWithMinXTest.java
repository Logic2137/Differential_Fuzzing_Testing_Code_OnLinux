



import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class RasterWithMinXTest {

    public static void main(String[] args) {
        String format = "jpeg";

        
        ImageOutputStream output = new MemoryCacheImageOutputStream(new ByteArrayOutputStream());

        
        BufferedImage bi = new BufferedImage(256, 256,
                                             BufferedImage.TYPE_3BYTE_BGR);

        
        int[] rgbArray = new int[256];
        for(int i = 0; i < 256; i++) {
            Arrays.fill(rgbArray, i);
            bi.setRGB(0, i, 256, 1, rgbArray, 0, 256);
        }

        
        WritableRaster r = (WritableRaster)bi.getRaster().createTranslatedChild(64,64);

        Iterator i =  ImageIO.getImageWritersByFormatName(format);
        ImageWriter iw = null;
        while(i.hasNext() && iw == null) {
            Object o = i.next();
            if (o instanceof com.sun.imageio.plugins.jpeg.JPEGImageWriter) {
                iw = (ImageWriter)o;
            }
        }
        if (iw == null) {
            throw new RuntimeException("No available image writer");
        }

         ImageWriteParam iwp = iw.getDefaultWriteParam();
         IIOMetadata metadata = iw.getDefaultImageMetadata(new ImageTypeSpecifier(bi.getColorModel(), r.getSampleModel()), iwp);

         IIOImage img = new IIOImage(r, null, metadata);

         iw.setOutput(output);
         try {
             iw.write(img);
         } catch (RasterFormatException e) {
             e.printStackTrace();
             throw new RuntimeException("RasterException occurs. Test Failed!");
         } catch (Exception ex) {
             ex.printStackTrace();
             throw new RuntimeException("Unexpected Exception");
         }

         
         iwp.setSourceRegion(new Rectangle(32,32,192,192));
         metadata = iw.getDefaultImageMetadata(new ImageTypeSpecifier(bi.getColorModel(), r.getSampleModel()), iwp);
         try {
             iw.write(metadata, img, iwp);
         } catch (RasterFormatException e) {
             e.printStackTrace();
             throw new RuntimeException("SetSourceRegion causes the RasterException. Test Failed!");
         } catch (Exception ex) {
             ex.printStackTrace();
             throw new RuntimeException("Unexpected Exception");
         }

    }
}
