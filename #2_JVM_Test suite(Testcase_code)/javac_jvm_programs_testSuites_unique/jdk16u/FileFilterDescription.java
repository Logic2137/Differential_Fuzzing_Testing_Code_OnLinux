

import java.applet.Applet;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

public final class FileFilterDescription extends Applet {

    @Override
    public void init() {
    }

    @Override
    public void start() {
        try {
            test();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void test() throws Exception {
        final UIManager.LookAndFeelInfo[] infos = UIManager
                .getInstalledLookAndFeels();
        for (final UIManager.LookAndFeelInfo info : infos) {
            SwingUtilities.invokeAndWait(() -> {
                final JFileChooser chooser = new JFileChooser();
                setLookAndFeel(info);
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileFilter(new CustomFileFilter());
                SwingUtilities.updateComponentTreeUI(chooser);
                chooser.showDialog(null, "Open");
            });
        }
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo info) {
        try {
            UIManager.setLookAndFeel(info.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static class CustomFileFilter extends FileFilter {

        @Override
        public boolean accept(final File f) {
            return false;
        }

        @Override
        public String getDescription() {
            return "CustomFileFilter";
        }
    }
}
