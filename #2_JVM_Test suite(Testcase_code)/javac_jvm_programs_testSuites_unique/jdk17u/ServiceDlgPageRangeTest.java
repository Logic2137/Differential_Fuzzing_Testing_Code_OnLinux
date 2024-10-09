import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.PageRanges;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ServiceDlgPageRangeTest {

    private static Thread mainThread;

    private static boolean testPassed;

    private static boolean testGeneratedInterrupt;

    private static DocFlavor flavor = DocFlavor.INPUT_STREAM.JPEG;

    private static PrintService[] services;

    public static void printTest() {
        System.out.println("\nDefault print service: " + PrintServiceLookup.lookupDefaultPrintService());
        System.out.println("is flavor: " + flavor + " supported? " + services[0].isDocFlavorSupported(flavor));
        System.out.println("is Page Ranges category supported? " + services[0].isAttributeCategorySupported(PageRanges.class));
        System.out.println("is PageRanges[2] value supported ? " + services[0].isAttributeValueSupported(new PageRanges(2), flavor, null));
        HashPrintRequestAttributeSet prSet = new HashPrintRequestAttributeSet();
        PrintService selService = ServiceUI.printDialog(null, 200, 200, services, services[0], flavor, prSet);
        System.out.println("\nSelected Values\n");
        Attribute[] attr = prSet.toArray();
        for (int x = 0; x < attr.length; x++) {
            System.out.println("Attribute: " + attr[x].getName() + " Value: " + attr[x]);
        }
    }

    public static void main(java.lang.String[] args) throws Exception {
        services = PrintServiceLookup.lookupPrintServices(flavor, null);
        if (services.length == 0) {
            System.out.println("No print service found!!");
            return;
        }
        SwingUtilities.invokeAndWait(() -> {
            doTest(ServiceDlgPageRangeTest::printTest);
        });
        mainThread = Thread.currentThread();
        try {
            Thread.sleep(600000);
        } catch (InterruptedException e) {
            if (!testPassed && testGeneratedInterrupt) {
                throw new RuntimeException("PageRanges option is not disabled " + "for for Non serv-formatted flvrs");
            }
        }
        if (!testGeneratedInterrupt) {
            throw new RuntimeException("user has not executed the test");
        }
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
        String description = " Visual inspection of print dialog is required.\n" + " A print dialog will be shown.\n " + " Please verify Pages(From/To) option is disabled.\n" + " Press Cancel to close the print dialog.\n" + " If Pages(From/To) option is disabled, press PASS else press FAIL";
        final JDialog dialog = new JDialog();
        dialog.setTitle("printSelectionTest");
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
}
