

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ServiceDialogValidateTest {
    private static Thread mainThread;
    private static boolean testPassed;
    private static boolean testGeneratedInterrupt;

    private static void printTest() {
        PrintService defService = null, service[] = null;
        HashPrintRequestAttributeSet prSet = new HashPrintRequestAttributeSet();
        DocFlavor flavor = DocFlavor.INPUT_STREAM.JPEG;

        service = PrintServiceLookup.lookupPrintServices(flavor, null);
        defService = PrintServiceLookup.lookupDefaultPrintService();

        if ((service == null) || (service.length == 0)) {
            throw new RuntimeException("No Printer services found");
        }
        File f = new File("output.ps");
        Destination d = new Destination(f.toURI());
        prSet.add(d);
        if (defService != null) {
            System.out.println("isAttrCategory Supported? " +
                    defService.isAttributeCategorySupported(Destination.class));
            System.out.println("isAttrValue Supported? " +
                    defService.isAttributeValueSupported(d, flavor, null));
        }

        defService = ServiceUI.printDialog(null, 100, 100, service, defService,
                flavor, prSet);

        ServiceUI.printDialog(null, 100, 100, service, defService,
                DocFlavor.SERVICE_FORMATTED.PAGEABLE,
                new HashPrintRequestAttributeSet());
    }

    
    public static void main(java.lang.String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            doTest(ServiceDialogValidateTest::printTest);
        });
        mainThread = Thread.currentThread();
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            if (!testPassed && testGeneratedInterrupt) {
                throw new RuntimeException("PrintToFile option is not disabled "
                        + "for flavors that do not support destination and/or"
                        + " disabled for flavors that supports destination");
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
        String description
                = " Visual inspection of print dialog is required.\n"
                + " 2 print dialog will be shown.\n "
                + " Please verify Print-To-File option is disabled "
                + " in the 1st print dialog.\n"
                + " Press Cancel to close the print dialog.\n"
                + " Please verify Print-To-File option is enabled "
                + " in 2nd print dialog\n"
                + " Press Cancel to close the print dialog.\n"
                + " If the print dialog's Print-to-File behaves as mentioned, "
                + " press PASS else press FAIL";

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


