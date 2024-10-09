import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;

public class GetPrintServices {

    public static void main(String[] args) throws Exception {
        for (PrintService service : PrintServiceLookup.lookupPrintServices(null, null)) {
            String serviceName = service.getName();
            PrinterName name = service.getAttribute(PrinterName.class);
            String printerName = name.getValue();
            PrintService serviceByName = lookupByName(printerName);
            System.out.println("service " + service);
            System.out.println("serviceByName " + serviceByName);
            if (!service.equals(serviceByName)) {
                throw new RuntimeException("NOK " + serviceName + " expected: " + service.getClass().getName() + " got: " + serviceByName.getClass().getName());
            }
        }
        System.out.println("Test PASSED");
    }

    private static PrintService lookupByName(String name) {
        AttributeSet attributes = new HashAttributeSet();
        attributes.add(new PrinterName(name, null));
        for (PrintService service : PrintServiceLookup.lookupPrintServices(null, attributes)) {
            return service;
        }
        return null;
    }
}
