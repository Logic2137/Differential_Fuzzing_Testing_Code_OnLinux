import java.awt.Color;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.SimpleDoc;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.io.ByteArrayOutputStream;

public class CustomCompositePrintTest implements Printable, Composite, CompositeContext {

    private BufferedImage mTestImage;

    static volatile int composeCallCount = 0;

    public CustomCompositePrintTest(BufferedImage testImage) {
        mTestImage = testImage;
    }

    public static void main(String[] args) throws Exception {
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        String mime = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
        StreamPrintServiceFactory[] factories = StreamPrintServiceFactory.lookupStreamPrintServiceFactories(flavor, mime);
        if (factories.length == 0) {
            System.out.println("No print service found.");
            return;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        StreamPrintService service = factories[0].getPrintService(output);
        BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.yellow);
        g2d.fillRect(0, 0, 200, 200);
        SimpleDoc doc = new SimpleDoc(new CustomCompositePrintTest(img), DocFlavor.SERVICE_FORMATTED.PRINTABLE, new HashDocAttributeSet());
        DocPrintJob job = service.createPrintJob();
        job.print(doc, new HashPrintRequestAttributeSet());
        if (composeCallCount < 2) {
            throw new RuntimeException("Compose called " + composeCallCount + "times. Expected at least 2");
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex == 0) {
            Graphics2D g2 = (Graphics2D) graphics;
            g2.translate(pf.getImageableX(), pf.getImageableY());
            g2.setComposite(this);
            g2.drawImage(mTestImage, 0, 0, null);
            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }

    @Override
    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return this;
    }

    @Override
    public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
        composeCallCount++;
        int w = dstOut.getWidth();
        int h = dstOut.getHeight();
        int[] samples3Band = { 0xff, 0x0, 0xff };
        int[] samples4Band = { 0xff, 0x0, 0xff, 0xff };
        int bands = dstOut.getNumBands();
        int[] samples = null;
        if (bands == 3) {
            samples = new int[] { 0xff, 0x0, 0xff };
        } else if (bands == 4) {
            samples = new int[] { 0xff, 0x0, 0xff, 0xff };
        } else {
            return;
        }
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                dstOut.setPixel(i, j, samples);
            }
        }
    }

    @Override
    public void dispose() {
    }
}
