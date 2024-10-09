import java.awt.print.*;
import javax.print.attribute.*;

public class PrintDialog {

    public static void main(java.lang.String[] args) {
        PrinterJob pj = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet pSet = new HashPrintRequestAttributeSet();
        System.out.println("Verify page setup dialog appears correctly then cancel or OK");
        pj.pageDialog(pSet);
        System.out.println("Verify all tabs of print dialog appear correctly then cancel or OK");
        pj.printDialog(pSet);
        return;
    }
}
