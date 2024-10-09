import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TestMediaTraySelection implements Printable {

    private static Thread mainThread;

    private static boolean testPassed;

    private static boolean testGeneratedInterrupt;

    private static PrintService prservices;

    public static void main(String[] args) throws Exception {
        prservices = PrintServiceLookup.lookupDefaultPrintService();
        if (prservices == null) {
            System.out.println("No print service found");
            return;
        }
        System.out.println(" Print service " + prservices);
        SwingUtilities.invokeAndWait(() -> {
            doTest(TestMediaTraySelection::printTest);
        });
        mainThread = Thread.currentThread();
        try {
            Thread.sleep(90000);
        } catch (InterruptedException e) {
            if (!testPassed && testGeneratedInterrupt) {
                throw new RuntimeException("Banner page did not print");
            }
        }
        if (!testGeneratedInterrupt) {
            throw new RuntimeException("user has not executed the test");
        }
    }

    private static void printTest() {
        MediaTray tray = null;
        tray = getMediaTray(prservices, "Tray 4");
        PrintRequestAttributeSet atrset = new HashPrintRequestAttributeSet();
        atrset.add(tray);
        PrinterJob pjob = PrinterJob.getPrinterJob();
        pjob.setPrintable(new TestMediaTraySelection());
        try {
            pjob.print(atrset);
        } catch (PrinterException e) {
            e.printStackTrace();
            fail();
        }
    }

    static MediaTray getMediaTray(PrintService ps, String name) {
        Media[] media = (Media[]) ps.getSupportedAttributeValues(Media.class, DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
        for (Media m : media) {
            if (m instanceof MediaTray) {
                System.out.println("MediaTray=" + m.toString());
                if (m.toString().trim().indexOf(name) > -1) {
                    return (MediaTray) m;
                }
            }
        }
        return null;
    }

    public static synchronized void pass() {
        testPassed = true;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }

    public static synchronized void fail() {
        testPassed = false;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }

    private static void doTest(Runnable action) {
        String description = " Please verify the \"Tray 4\" of printer is used for printout\n" + " and not standard/auto tray. If yes, press PASS else press FAIL";
        final JDialog dialog = new JDialog();
        dialog.setTitle("printBannerTest");
        JTextArea textArea = new JTextArea(description);
        textArea.setEditable(false);
        final JButton testButton = new JButton("Start Test");
        final JButton passButton = new JButton("PASS");
        passButton.setEnabled(false);
        passButton.addActionListener((e) -> {
            dialog.dispose();
            pass();
        });
        final JButton failButton = new JButton("FAIL");
        failButton.setEnabled(false);
        failButton.addActionListener((e) -> {
            dialog.dispose();
            fail();
        });
        testButton.addActionListener((e) -> {
            testButton.setEnabled(false);
            action.run();
            passButton.setEnabled(true);
            failButton.setEnabled(true);
        });
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(textArea, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(testButton);
        buttonPanel.add(passButton);
        buttonPanel.add(failButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setVisible(true);
        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("main dialog closing");
                testGeneratedInterrupt = false;
                mainThread.interrupt();
            }
        });
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pi) {
        System.out.println("pi = " + pi);
        if (pi > 0) {
            return NO_SUCH_PAGE;
        }
        g.drawString("Testing : ", 200, 200);
        return PAGE_EXISTS;
    }
}
