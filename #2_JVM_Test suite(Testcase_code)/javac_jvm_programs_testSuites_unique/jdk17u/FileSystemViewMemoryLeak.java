import java.io.File;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;
import javax.swing.filechooser.FileSystemView;

public class FileSystemViewMemoryLeak {

    public static void main(String[] args) {
        test();
    }

    static long endtime = System.nanoTime() + TimeUnit.SECONDS.toNanos(300);

    private static void test() {
        File root = new File("C:\\");
        System.out.println("Root Exists: " + root.exists());
        System.out.println("Root Absolute Path: " + root.getAbsolutePath());
        System.out.println("Root Is Directory?: " + root.isDirectory());
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        NumberFormat nf = NumberFormat.getNumberInstance();
        int iMax = 50000;
        long lastPercentFinished = 0L;
        for (int i = 0; i < iMax; i++) {
            if (isComplete()) {
                System.out.println("Time is over");
                return;
            }
            long percentFinished = Math.round(((i * 1000d) / (double) iMax));
            if (lastPercentFinished != percentFinished) {
                double pf = ((double) percentFinished) / 10d;
                String pfMessage = String.valueOf(pf) + " % (" + i + "/" + iMax + ")";
                long totalMemory = Runtime.getRuntime().totalMemory() / 1024;
                long freeMemory = Runtime.getRuntime().freeMemory() / 1024;
                long maxMemory = Runtime.getRuntime().maxMemory() / 1024;
                String memMessage = "[Memory Used: " + nf.format(totalMemory) + " kb Free=" + nf.format(freeMemory) + " kb Max: " + nf.format(maxMemory) + " kb]";
                System.out.println(pfMessage + " " + memMessage);
                lastPercentFinished = percentFinished;
            }
            boolean floppyDrive = fileSystemView.isFloppyDrive(root);
            boolean computerNode = fileSystemView.isComputerNode(root);
            boolean drive = fileSystemView.isDrive(root);
        }
    }

    private static boolean isComplete() {
        return endtime - System.nanoTime() < 0;
    }
}
