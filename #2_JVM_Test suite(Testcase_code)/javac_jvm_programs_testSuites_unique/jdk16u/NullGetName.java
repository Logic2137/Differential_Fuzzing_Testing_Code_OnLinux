




import javax.print.*;
import javax.print.attribute.*;
import javax.print.event.*;
import java.awt.print.*;


public class NullGetName {

    public static void main(String[] args) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
                try {
                printerJob.setPrintService(new ImagePrintService());
                } catch (PrinterException e) {
                }
    }
}


class ImagePrintService implements PrintService {


    public Class[] getSupportedAttributeCategories() {
        
        return null;
    }

    public boolean isAttributeCategorySupported(Class category) {
        
        return false;
    }

    public String getName() {
        
        return null;
    }

    public DocFlavor[] getSupportedDocFlavors() {
        
        return null;
    }


    public boolean isDocFlavorSupported(DocFlavor flavor) {
        if(DocFlavor.SERVICE_FORMATTED.PAGEABLE.equals(flavor))
            return true;
        if(DocFlavor.SERVICE_FORMATTED.PRINTABLE.equals(flavor))
            return true;
        return false;
    }

    public DocPrintJob createPrintJob() {
        
        return null;
    }

    public ServiceUIFactory getServiceUIFactory() {
        
        return null;
    }


    public PrintServiceAttributeSet getAttributes() {
        
        return null;
    }

    public void addPrintServiceAttributeListener(
            PrintServiceAttributeListener listener) {
        

    }

    public void removePrintServiceAttributeListener(
            PrintServiceAttributeListener listener) {
        

    }

    public Object getDefaultAttributeValue(Class category) {
        
        return null;
    }

        public <T extends PrintServiceAttribute> T
        getAttribute(Class<T> category) {
            
        return null;
    }

    public boolean isAttributeValueSupported(Attribute attrval,
            DocFlavor flavor, AttributeSet attributes) {
        
        return false;
    }

    public AttributeSet getUnsupportedAttributes(DocFlavor flavor,
            AttributeSet attributes) {
        
        return null;
    }

    public Object getSupportedAttributeValues(Class category, DocFlavor flavor,
            AttributeSet attributes) {
        
        return null;
    }

}
