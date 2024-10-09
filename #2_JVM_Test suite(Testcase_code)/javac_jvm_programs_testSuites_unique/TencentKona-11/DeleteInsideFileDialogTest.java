



import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeleteInsideFileDialogTest {

    private static Path dir;
    private static Path file1;
    private static Path file2;
    private static Frame f;
    private static FileDialog fd;

    public static void main(String[] args) throws Exception {

        String instructions =
                "1) Delete file deleteMe.tst in the opened File Dialog window" +
                   " using the right click popup menu\n" +
                "2) Select thenSelectMe.tst file in the File Dialog and press" +
                   " Open (if this is not possible the test fails)\n";
        dir = Files.createTempDirectory("Test");
        file1 = Files.createFile(Paths.get(dir.toString(), "deleteMe.tst"));
        file2 = Files.createFile(Paths.get(dir.toString(), "thenSelectMe.tst"));
        try {
            f = new Frame("Instructions");
            f.add(new TextArea(instructions, 6, 60, TextArea.SCROLLBARS_NONE));
            f.pack();
            f.setLocation(100, 500);
            f.setVisible(true);

            fd = new FileDialog((Frame)null);
            fd.setDirectory(dir.toString());
            fd.setVisible(true);
            if (fd.getFile() == null) {
                throw new RuntimeException("Failed");
            }
        } finally {
            if (fd != null) {
                fd.dispose();
            }
            if (f != null) {
                f.dispose();
            }
            Files.deleteIfExists(file1);
            Files.deleteIfExists(file2);
            Files.deleteIfExists(dir);
        }
    }
}
