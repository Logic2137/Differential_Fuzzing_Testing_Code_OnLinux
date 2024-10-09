



import sun.awt.shell.ShellFolder;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class FileSystemRootTest {
    public static void main(String[] args) throws Exception {
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();

        
        File def = fileSystemView.getDefaultDirectory();
        File root = fileSystemView.getParentDirectory(
                        fileSystemView.getParentDirectory(
                            fileSystemView.getParentDirectory(def)));

        if (! (root instanceof ShellFolder && ShellFolder.isFileSystemRoot(root))) {
            throw new RuntimeException("Test failed: root drive reported as false");
        }

        if (fileSystemView.getSystemDisplayName(root).isEmpty()) {
            throw new RuntimeException("Root drive display name is empty.");
        }
    }
}
