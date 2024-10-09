import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.print.*;

public class CompareImageable implements Printable {

    public int print(Graphics g, PageFormat pgFmt, int pgIndex) {
        int pageHeight = (int) pgFmt.getImageableHeight();
        int pageWidth = (int) pgFmt.getImageableWidth();
        System.out.println("imageable width = " + pageWidth + " height = " + pageHeight);
        return Printable.NO_SUCH_PAGE;
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Print Test");
        final JButton printBtn = new JButton("Print");
        final JCheckBox dialogBtn = new JCheckBox("Native dialog");
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(dialogBtn);
        panel.add(printBtn);
        frame.getContentPane().add(panel);
        printBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                CompareImageable test = new CompareImageable();
                PrinterJob pj = PrinterJob.getPrinterJob();
                if (dialogBtn.isSelected() && !pj.printDialog()) {
                    return;
                }
                if (dialogBtn.isSelected()) {
                    System.out.println("With print dialog...");
                } else {
                    System.out.println("Without print dialog...");
                }
                if (pj == null) {
                    System.out.println("No printer job found...");
                    return;
                }
                pj.setPrintable(test);
                try {
                    pj.print();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}
