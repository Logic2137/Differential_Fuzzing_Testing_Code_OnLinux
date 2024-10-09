import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

public class PrintResAttr {

    public static void main(String[] args) throws Exception {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < services.length; i++) {
            if (services[i].isAttributeCategorySupported(PrinterResolution.class)) {
                System.out.println("Testing " + services[i]);
                PrinterResolution[] res = (PrinterResolution[]) services[i].getSupportedAttributeValues(PrinterResolution.class, null, null);
                System.out.println("# supp res= " + res.length);
                for (int r = 0; r < res.length; r++) System.out.println(res[r]);
            }
        }
    }
}
