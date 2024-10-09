import java.io.File;
import java.awt.Desktop;
import java.awt.Robot;
import java.io.IOException;
import java.awt.AWTException;

public class MoveToTrashTest {

    private static File file = null;

    private static boolean fileStatus = false;

    public static void main(String[] args) {
        if (!Desktop.getDesktop().isSupported(Desktop.Action.MOVE_TO_TRASH)) {
            System.out.println("Move to trash action is not supported on the" + " platform under test. Marking the test passed");
        } else {
            try {
                file = File.createTempFile("TestFile", "txt");
            } catch (IOException ex) {
                throw new RuntimeException("Test failed. Exception thrown: ", ex);
            }
            new Thread(null, MoveToTrashTest::checkFileExistence, "FileCheck", 0, false).start();
            fileStatus = Desktop.getDesktop().moveToTrash(file);
        }
    }

    private static void checkFileExistence() {
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            throw new RuntimeException("Test failed. Exception thrown: ", ex);
        }
        robot.delay(1500);
        if (!fileStatus) {
            throw new RuntimeException("Test failed due to error while deleting the file");
        } else {
            if (file.exists()) {
                throw new RuntimeException("Test failed");
            } else {
                System.out.println("Test passed");
            }
        }
    }
}
