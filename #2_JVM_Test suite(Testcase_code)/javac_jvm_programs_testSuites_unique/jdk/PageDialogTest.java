



import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PageDialogTest {

    public static Frame frame;
    public static Dialog dialog;
    public static volatile boolean testResult;
    public static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void createUI() {
        frame = new Frame("Test 6302514");
        String instructions =
                "1. Click on the 'Show Dialog' button to show a 'Toolkit Modal Dialog' \n" +
                "2. Click on the 'Open PageDialog' button to show 'Page Dialog'.\n" +
                "3. The test fails if the page dialog blocks the toolkit\n"+
                " else test pass.\n" +
                "4. Close Page dialog and 'Toolkit modal dialog'\n" +
                "5. Click appropriate button to mark the test case pass or fail.\n" ;

        TextArea instructionsTextArea = new TextArea( instructions, 8,
                50, TextArea.SCROLLBARS_NONE );
        instructionsTextArea.setEditable(false);
        frame.add(BorderLayout.NORTH, instructionsTextArea);

        Panel buttonPanel = new Panel(new FlowLayout());
        Button passButton = new Button("pass");
        passButton.setActionCommand("pass");
        passButton.addActionListener(e -> {
            testResult = true;
            countDownLatch.countDown();
            dialog.dispose();
            frame.dispose();
        });

        Button failButton = new Button("fail");
        failButton.addActionListener(e->{
            testResult = false;
            countDownLatch.countDown();
            dialog.dispose();
            frame.dispose();
        });

        Button showDialog = new Button("Show Dialog");
        showDialog.addActionListener(e->{
            createToolkitModalDialog();
        });

        buttonPanel.add(showDialog);
        buttonPanel.add(passButton);
        buttonPanel.add(failButton);
        frame.add(BorderLayout.SOUTH, buttonPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void createToolkitModalDialog() {
        dialog = new Dialog((Frame) null, "Toolkit modal dialog",
                Dialog.ModalityType.TOOLKIT_MODAL);
        dialog.setLayout(new FlowLayout());
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dialog.dispose();
            }
        });

        Button openPageDialogButton = new Button("Open PageDialog");
        openPageDialogButton.addActionListener(e->{
            PrinterJob.getPrinterJob().pageDialog(new PageFormat());
        });
        dialog.add(openPageDialogButton);
        dialog.setSize(250, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public static void main(String []args) throws InterruptedException {
        createUI();
        if ( !countDownLatch.await(5, TimeUnit.MINUTES)) {
            throw new RuntimeException("Timeout : user did not perform any " +
                    "action on the UI.");
        }
        if ( !testResult) {
            throw new RuntimeException("Test failed");
        }
    }
}

