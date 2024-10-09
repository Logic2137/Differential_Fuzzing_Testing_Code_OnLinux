



import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;

public class NullInputOutput {

    public static void main(String[] args) throws Exception {
        IIORegistry registry = IIORegistry.getDefaultInstance();

        
        Iterator readerspis = registry.getServiceProviders(ImageReaderSpi.class,
                                                           false);
        while (readerspis.hasNext()) {
            ImageReaderSpi readerspi = (ImageReaderSpi)readerspis.next();
            ImageReader reader = readerspi.createReaderInstance();
            try {
                reader.read(0);
            } catch (IllegalStateException ise) {
                
            }
        }

        
        BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Iterator writerspis = registry.getServiceProviders(ImageWriterSpi.class,
                                                           false);
        while (writerspis.hasNext()) {
            ImageWriterSpi writerspi = (ImageWriterSpi)writerspis.next();
            ImageWriter writer = writerspi.createWriterInstance();
            try {
                writer.write(bi);
            } catch (IllegalStateException ise) {
                
            }
        }
    }
}
