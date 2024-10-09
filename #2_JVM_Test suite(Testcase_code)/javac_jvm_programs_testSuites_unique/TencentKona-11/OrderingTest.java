



import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ServiceRegistry;

public class OrderingTest {

    public OrderingTest() {

         ServiceRegistry reg = IIORegistry.getDefaultInstance();
         ImageReaderSpi gifSpi = (ImageReaderSpi) reg.getServiceProviderByClass(com.sun.imageio.plugins.gif.GIFImageReaderSpi.class);
         ImageReaderSpi pngSpi = (ImageReaderSpi) reg.getServiceProviderByClass(com.sun.imageio.plugins.png.PNGImageReaderSpi.class);

         boolean ordered = reg.setOrdering(ImageReaderSpi.class, gifSpi, pngSpi);

         ordered = reg.setOrdering(ImageReaderSpi.class, pngSpi, gifSpi);

         boolean unordered = reg.unsetOrdering(ImageReaderSpi.class, gifSpi,
                                               pngSpi);
         boolean unordered1 = reg.unsetOrdering(ImageReaderSpi.class, gifSpi,
                                                pngSpi);

         if (unordered1) {
             throw new RuntimeException("FAIL: Ordering 2 spi objects in the  "
                                        + "reverse direction does not remove the previous ordering "
                                        + "set between the spi objects and hence unsetOrdering() "
                                        + "returns true for the same spi objects when called consecutively");
         } else {
             System.out.println("PASS");
         }

     }

     public static void main(String args[]) {
         OrderingTest test = new OrderingTest();
     }
}
