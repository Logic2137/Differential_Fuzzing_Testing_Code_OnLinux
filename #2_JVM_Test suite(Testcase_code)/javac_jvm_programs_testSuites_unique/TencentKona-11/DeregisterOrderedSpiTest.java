



import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ServiceRegistry;

public class DeregisterOrderedSpiTest {

     public DeregisterOrderedSpiTest() {

         try {

             ServiceRegistry reg = IIORegistry.getDefaultInstance();
             ImageReaderSpi gifSpi = (ImageReaderSpi) reg.getServiceProviderByClass(com.sun.imageio.plugins.gif.GIFImageReaderSpi.class);
             ImageReaderSpi pngSpi = (ImageReaderSpi) reg.getServiceProviderByClass(com.sun.imageio.plugins.png.PNGImageReaderSpi.class);
             ImageReaderSpi jpgSpi = (ImageReaderSpi) reg.getServiceProviderByClass(com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi.class);
             ImageReaderSpi bmpSpi = (ImageReaderSpi) reg.getServiceProviderByClass(com.sun.imageio.plugins.bmp.BMPImageReaderSpi.class);

             boolean ordered = reg.setOrdering(ImageReaderSpi.class, pngSpi,
                                               gifSpi);

             ordered = reg.setOrdering(ImageReaderSpi.class, gifSpi, jpgSpi);
             ordered = reg.setOrdering(ImageReaderSpi.class, bmpSpi, gifSpi);
             reg.deregisterServiceProvider(gifSpi);
             System.out.println("PASS");

         } catch (Exception e) {
             System.out.println("FAIL");
             throw new RuntimeException("Deregistering a spi object involved in some "
                                        + "ordering throws the following exception: " + e.toString());
         }
     }

     public static void main(String args[]) {
         DeregisterOrderedSpiTest test = new DeregisterOrderedSpiTest();
     }
}
