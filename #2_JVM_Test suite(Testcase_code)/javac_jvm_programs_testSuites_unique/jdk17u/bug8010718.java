import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class bug8010718 {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.addChoosableFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
                    }

                    @Override
                    public String getDescription() {
                        return "XML";
                    }
                });
                fileChooser.addChoosableFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
                    }

                    @Override
                    public String getDescription() {
                        return "TXT";
                    }
                });
                if (fileChooser.getFileFilter() == null) {
                    throw new RuntimeException("getFileFilter() should not return null");
                }
            }
        });
    }
}
