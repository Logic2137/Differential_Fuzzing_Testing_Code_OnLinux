import java.awt.Component;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.DialogTypeSelection;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class PageDlgApp {

    public static void main(String[] args) throws Exception {
        String[] instructions = { "Visual inspection of the dialog is needed. ", "It should be a Printer Job Setup Dialog", "Do nothing except Cancel", "You must NOT press OK" };
        SwingUtilities.invokeAndWait(() -> {
            JOptionPane.showMessageDialog((Component) null, instructions, "information", JOptionPane.INFORMATION_MESSAGE);
        });
        PrinterJob pj = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet pSet = new HashPrintRequestAttributeSet();
        pSet.add(DialogTypeSelection.NATIVE);
        if ((pj.pageDialog(pSet)) != null) {
            throw new RuntimeException("PrinterJob.pageDialog(PrintRequestAttributeSet)" + " does not return null when dialog is cancelled");
        }
    }
}
