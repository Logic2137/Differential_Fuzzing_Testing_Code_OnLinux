import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class PrintLatinCJKTest implements Printable, ActionListener {

    static PrintLatinCJKTest testInstance = new PrintLatinCJKTest();

    private PageFormat pf;

    static String info = "To test 8022536, if a remote printer is the system default," + "it should show in the dialog as the selected printer.\n" + "You need a printer for this test. If you have none, let " + "the test pass. If there is a printer, press Print, send " + "the output to the printer, and examine it. It should have " + "text looking like this : \u4e00\u4e01\u4e02\u4e03\u4e04English.";

    public static void showFrame() {
        JFrame f = new JFrame();
        JTextArea jta = new JTextArea(info, 4, 30);
        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);
        f.add("Center", jta);
        JButton b = new JButton("Print");
        b.addActionListener(testInstance);
        f.add("South", b);
        f.pack();
        f.setVisible(true);
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }
        g.translate((int) pf.getImageableX(), (int) pf.getImageableY());
        g.setFont(new Font("Dialog", Font.PLAIN, 36));
        g.drawString("\u4e00\u4e01\u4e02\u4e03\u4e04English", 20, 100);
        return Printable.PAGE_EXISTS;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(testInstance);
            if (job.printDialog()) {
                job.print();
            }
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showFrame();
            }
        });
    }
}
