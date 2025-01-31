



import java.io.File;

import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

public class bug6698013 extends JApplet {

    final static VirtualFile root = new VirtualFile("testdir", true);

    final static VirtualFile rootFile = new VirtualFile("testdir/test.txt", false);

    final static VirtualFile subdir = new VirtualFile("testdir/subdir", true);

    final static VirtualFile subdirFile = new VirtualFile("testdir/subdir/subtest.txt", false);

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> new bug6698013().init());
    }

    public void init() {
        JFileChooser chooser = new JFileChooser(new VirtualFileSystemView());
        chooser.setCurrentDirectory(root);
        chooser.showOpenDialog(null);
    }
}

class VirtualFileSystemView extends FileSystemView {

    public boolean isRoot(File dir) {
        return bug6698013.root.equals(dir);
    }

    public File createNewFolder(File dir) {
        return null;
    }

    public File[] getRoots() {
        return new File[]{bug6698013.root};
    }

    public boolean isDrive(File dir) {
        return false;
    }

    public boolean isFloppyDrive(File dir) {
        return false;
    }

    public File getParentDirectory(File dir) {
        if (dir == null) {
            return null;
        }

        return new VirtualFile(dir.getPath(), true).getParentFile();
    }

    public File[] getFiles(File dir, boolean hide_hidden) {
        if (dir.equals(bug6698013.root)) {
            return new File[]{bug6698013.rootFile, bug6698013.subdir};
        }

        if (dir.equals(bug6698013.subdir)) {
            return new File[]{bug6698013.subdirFile};
        }

        return null;
    }

    public File getHomeDirectory() {
        return bug6698013.root;
    }

    public File getDefaultDirectory() {
        return getHomeDirectory();
    }

    public String getSystemDisplayName(File file) {
        return file.getName();
    }

    public Boolean isTraversable(File file) {
        return Boolean.valueOf(file.isDirectory());
    }
}


class VirtualFile extends File {

    private static final long serialVersionUID = 0L;

    private String path;

    private boolean directory;

    public VirtualFile(String path, boolean directory) {
        super(path);
        this.path = path;
        this.directory = directory;
    }

    public File getParentFile() {
        int index = path.lastIndexOf('/');

        if (index == -1) {
            return null;
        }

        return new VirtualFile(path.substring(0, index), true);
    }

    public File getCanonicalFile() {
        return this;
    }

    public String getParent() {
        File parent_file = getParentFile();

        return parent_file == null ? null : parent_file.getPath();
    }

    public String getName() {
        int index = path.lastIndexOf('/');

        return index == -1 ? path : path.substring(index + 1);
    }

    public String getPath() {
        return path;
    }

    public String getAbsolutePath() {
        return path;
    }

    public String getCanonicalPath() {
        return path;
    }

    public String toString() {
        return path;
    }

    public boolean equals(Object obj) {
        return obj instanceof VirtualFile && path.equals(obj.toString());
    }

    public int hashCode() {
        return path.hashCode();
    }

    public boolean canWrite() {
        return true;
    }

    public boolean isDirectory() {
        return directory;
    }

    public boolean exists() {
        return true;
    }
}
