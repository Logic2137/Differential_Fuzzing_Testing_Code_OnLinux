


import java.awt.Robot;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;

public class JEditorPaneTest {

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        try {
            File file = File.createTempFile("Temp_", ".txt");
            file.deleteOnExit();
            writeFile(file);
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    JEditorPane editorPane = new JEditorPane();
                    try {
                        editorPane.setPage(file.toURI().toURL());
                    } catch (IOException ex) {
                        file.delete();
                        throw new RuntimeException("Test Failed" + ex);
                    }
                }
            });
            robot.waitForIdle();
            if (!file.renameTo(file)) {
                file.delete();
                throw new RuntimeException("Test Failed");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to create File" + ex);
        }
    }

    private static void writeFile(File file) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Test Text");
            bw.close();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write File" + ex);
        }

    }
}
