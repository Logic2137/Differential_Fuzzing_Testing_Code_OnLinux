import java.awt.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

public class PDialogTest {

    public static void main(String[] args) {
        PageFormat page = new PageFormat();
        while (true) {
            page = java.awt.print.PrinterJob.getPrinterJob().pageDialog(page);
        }
    }
}
