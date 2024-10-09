



import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.nio.file.Files;

public class bug6868611 {
    private static final int COUNT = 1000;
    private static File tempFolder;
    private static File files[] = new File[COUNT];

    public static void main(String[] args) throws Exception {
        int fileCount = 0;
        try {
            tempFolder = Files.createTempDirectory("temp_folder").toFile();

            
            for (fileCount = 0; fileCount < COUNT; fileCount++) {
                files[fileCount] = new
                        File(tempFolder, "temp" + fileCount + ".txt");
                files[fileCount].createNewFile();
            }

            
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    FileSystemView.getFileSystemView().
                            getFiles(tempFolder, false);
                }
            });

            for (int i = 0; i < COUNT; i++) {
                Thread thread = new MyThread(tempFolder);

                thread.start();

                Thread.sleep((long) (Math.random() * 100));

                thread.interrupt();
            }
        } finally {
            
            for (int i = 0; i < fileCount; i++) {
                Files.delete(files[i].toPath());
            }
            Files.delete(tempFolder.toPath());
        }
    }

    private static class MyThread extends Thread {
        private final File dir;

        private MyThread(File dir) {
            this.dir = dir;
        }

        public void run() {
            FileSystemView fileSystemView = FileSystemView.getFileSystemView();

            fileSystemView.getFiles(dir, false);
        }
    }
}

