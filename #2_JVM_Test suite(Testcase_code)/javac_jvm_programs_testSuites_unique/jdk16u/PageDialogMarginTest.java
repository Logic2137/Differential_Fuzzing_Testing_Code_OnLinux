


import java.awt.Component;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class PageDialogMarginTest {

    public static void main(String args[]) throws Exception {
        String[] instructions
                = {
                    "Page Dialog will be shown.",
                    "Change top(in) margin value from 1.0 to 2.0",
                    "Then select OK."
                };
        SwingUtilities.invokeAndWait(() -> {
            JOptionPane.showMessageDialog((Component) null,
                    instructions, "Instructions",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        PrinterJob pj = PrinterJob.getPrinterJob();
        try {
            HashPrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            PageFormat pf;
            pf = pj.pageDialog(aset);
            double left = pf.getImageableX();
            double top = pf.getImageableY();
            System.out.println("pageDialog - left/top from pageFormat: " + left / 72
                    + " " + top / 72);
            System.out.println("pageDialog - left/top from attribute set: "
                    + getPrintableXFromASet(aset) + " "
                    + getPrintableYFromASet(aset));
            if (top / 72 != 2.0f || getPrintableYFromASet(aset) != 2.0f) {
                throw new RuntimeException("Top margin value not updated");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static double getPrintableXFromASet(PrintRequestAttributeSet aset) {
        try {
            return ((MediaPrintableArea) aset.get(
                    MediaPrintableArea.class)).getX(MediaPrintableArea.INCH);
        } catch (Exception e) {
            return -1.0;
        }
    }

    static double getPrintableYFromASet(PrintRequestAttributeSet aset) {
        try {
            return ((MediaPrintableArea) aset.get(
                    MediaPrintableArea.class)).getY(MediaPrintableArea.INCH);
        } catch (Exception e) {
            return -1.0;
        }
    }

}
