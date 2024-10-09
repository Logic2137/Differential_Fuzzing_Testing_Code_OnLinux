import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class UnixSocketFile {

    private static final String TEST_SUB_DIR = "UnixSocketFile";

    private static final String SOCKET_FILE_NAME = "mysocket";

    private static final String CMD_BASE = "nc -lU";

    public static void main(String[] args) throws InterruptedException, IOException {
        Process proc = Runtime.getRuntime().exec("which nc");
        InputStream stdout = proc.getInputStream();
        int b = stdout.read();
        proc.destroy();
        if (b == -1) {
            System.err.println("Netcat command unavailable; skipping test.");
            return;
        }
        Process procHelp = Runtime.getRuntime().exec(CMD_BASE + " -h");
        if (procHelp.waitFor() != 0) {
            System.err.println("Netcat does not accept required options; skipping test.");
            return;
        }
        String testSubDir = System.getProperty("test.dir", ".") + File.separator + TEST_SUB_DIR;
        Path socketTestDir = Paths.get(testSubDir);
        Files.createDirectory(socketTestDir);
        String socketFilePath = testSubDir + File.separator + SOCKET_FILE_NAME;
        FileSystem fs = FileSystems.getDefault();
        try (WatchService ws = fs.newWatchService()) {
            WatchKey wk = socketTestDir.register(ws, StandardWatchEventKinds.ENTRY_CREATE);
            proc = Runtime.getRuntime().exec(CMD_BASE + " " + socketFilePath);
            WatchKey key = ws.take();
            if (key != wk) {
                throw new RuntimeException("Unknown entry created - expected: " + wk.watchable() + ", actual: " + key.watchable());
            }
            wk.cancel();
        }
        Path socketPath = fs.getPath(socketFilePath);
        if (!Files.exists(socketPath)) {
            throw new RuntimeException("Socket file " + socketFilePath + " was not created by \"nc\" command.");
        }
        BasicFileAttributeView attributeView = Files.getFileAttributeView(socketPath, BasicFileAttributeView.class);
        BasicFileAttributes oldAttributes = attributeView.readAttributes();
        FileTime oldAccessTime = oldAttributes.lastAccessTime();
        FileTime oldModifiedTime = oldAttributes.lastModifiedTime();
        System.out.println("Old times: " + oldAccessTime + " " + oldModifiedTime);
        FileTime newFileTime = FileTime.fromMillis(oldAccessTime.toMillis() + 1066);
        try {
            attributeView.setTimes(newFileTime, newFileTime, null);
            FileTime newAccessTime = null;
            FileTime newModifiedTime = null;
            BasicFileAttributes newAttributes = attributeView.readAttributes();
            newAccessTime = newAttributes.lastAccessTime();
            newModifiedTime = newAttributes.lastModifiedTime();
            System.out.println("New times: " + newAccessTime + " " + newModifiedTime);
            if ((newAccessTime != null && !newAccessTime.equals(newFileTime)) || (newModifiedTime != null && !newModifiedTime.equals(newFileTime))) {
                throw new RuntimeException("Failed to set correct times.");
            }
        } finally {
            proc.destroy();
            Files.delete(socketPath);
        }
    }
}
