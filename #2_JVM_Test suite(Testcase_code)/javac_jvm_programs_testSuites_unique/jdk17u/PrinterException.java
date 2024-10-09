import java.awt.Frame;
import java.awt.JobAttributes;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class PrinterException {

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        Thread t = new Thread(() -> {
            robot.delay(2000);
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            robot.waitForIdle();
        });
        Toolkit tk = Toolkit.getDefaultToolkit();
        PrintJob pj = null;
        int[][] pageRange = new int[][] { new int[] { 1, 1 } };
        JobAttributes ja = new JobAttributes(1, java.awt.JobAttributes.DefaultSelectionType.ALL, JobAttributes.DestinationType.FILE, JobAttributes.DialogType.NATIVE, "filename.ps", Integer.MAX_VALUE, 1, JobAttributes.MultipleDocumentHandlingType.SEPARATE_DOCUMENTS_UNCOLLATED_COPIES, pageRange, "", JobAttributes.SidesType.ONE_SIDED);
        Frame testFrame = new Frame("print");
        try {
            if (tk != null) {
                t.start();
                pj = tk.getPrintJob(testFrame, null, ja, null);
            }
        } finally {
            testFrame.dispose();
        }
    }
}
