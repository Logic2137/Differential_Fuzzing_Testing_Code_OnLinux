import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class PrinterJobDialogBugDemo extends JFrame implements Printable {

    public static void main(String[] args) {
        new PrinterJobDialogBugDemo();
    }

    private PrinterJobDialogBugDemo() {
        super("Printer Job Dialog Bug Demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 700);
        JButton btnPrint = new JButton("Print...");
        btnPrint.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                showPrintDialog();
            }
        });
        Container contentPane = getContentPane();
        contentPane.add(new JLabel("<html>This is the main Application Window. " + "To demonstrate the problem:" + "<ol>" + "<li>Click the Print button at the bottom of this window. " + "The Print dialog will appear." + "<li>Select another application window." + "<li>On the Windows taskbar, click the coffee-cup icon for " + "this demo application.  This brings this window to the " + "front but the Print dialog remains hidden. " + "Since this window " + "is no longer selectable, it can't be moved aside to expose " + "the Print dialog that is now behind it." + "</ol>", SwingConstants.CENTER), BorderLayout.NORTH);
        contentPane.add(btnPrint, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void showPrintDialog() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        PrintRequestAttributeSet printRequestAttrSet = new HashPrintRequestAttributeSet();
        printJob.printDialog(printRequestAttrSet);
    }

    public int print(java.awt.Graphics g, java.awt.print.PageFormat pageFormat, int pageIndex) {
        if (pageIndex == 0) {
            return (PAGE_EXISTS);
        } else {
            return (NO_SUCH_PAGE);
        }
    }
}
