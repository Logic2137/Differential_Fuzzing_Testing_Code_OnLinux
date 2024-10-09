import java.io.File;
import java.io.FileOutputStream;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.standard.OrientationRequested;

public class TestOrientationSupportForStreamPrnSrv {

    public static void main(java.lang.String[] args) throws Exception {
        PrintService service = null;
        PrintService defService = PrintServiceLookup.lookupDefaultPrintService();
        File f = null;
        FileOutputStream fos = null;
        String mType = "application/postscript";
        DocFlavor[] flavors = null;
        f = new File("streamexample1.ps");
        fos = new FileOutputStream(f);
        StreamPrintServiceFactory[] factories = StreamPrintServiceFactory.lookupStreamPrintServiceFactories(DocFlavor.INPUT_STREAM.JPEG, mType);
        if (factories.length > 0) {
            System.out.println("output format " + factories[0].getOutputFormat());
            service = factories[0].getPrintService(fos);
            flavors = factories[0].getSupportedDocFlavors();
        }
        System.out.println("Stream Print Service " + service);
        if (service == null) {
            throw new RuntimeException("No Stream Print Service found");
        }
        System.out.println("is OrientationRequested supported? " + service.isAttributeCategorySupported(OrientationRequested.class));
        for (int k = 0; k < flavors.length; k++) {
            Object obj = service.getSupportedAttributeValues(OrientationRequested.class, flavors[k], null);
            if (flavors[k].equals(DocFlavor.INPUT_STREAM.JPEG)) {
                if (obj == null) {
                    throw new RuntimeException("" + "StreamPrintServ.getSupportedAttributeValues " + "returns null for image/jpeg DocFlavor for " + "supported Orientation category");
                }
            }
        }
    }
}
