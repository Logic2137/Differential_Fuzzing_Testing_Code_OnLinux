import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.FileStore;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SetTimesNanos {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    public static void main(String[] args) throws Exception {
        if (!IS_WINDOWS) {
            Class unixNativeDispatcherClass = Class.forName("sun.nio.fs.UnixNativeDispatcher");
            Method futimensSupported = unixNativeDispatcherClass.getDeclaredMethod("futimensSupported");
            futimensSupported.setAccessible(true);
            if (!(boolean) futimensSupported.invoke(null)) {
                System.err.println("futimens() not supported; skipping test");
                return;
            }
        }
        Path dirPath = Path.of("test");
        Path dir = Files.createDirectory(dirPath);
        FileStore store = Files.getFileStore(dir);
        System.out.format("FileStore: \"%s\" on %s (%s)%n", dir, store.name(), store.type());
        Set<String> testedTypes = IS_WINDOWS ? Set.of("NTFS") : Set.of("apfs", "ext4", "xfs", "zfs");
        if (!testedTypes.contains(store.type())) {
            System.err.format("%s not in %s; skipping test", store.type(), testedTypes);
            return;
        }
        testNanos(dir);
        Path file = Files.createFile(dir.resolve("test.dat"));
        testNanos(file);
    }

    private static void testNanos(Path path) throws IOException {
        long timeNanos = 1_483_261_261L * 1_000_000_000L + 123_456_789L;
        FileTime pathTime = FileTime.from(timeNanos, TimeUnit.NANOSECONDS);
        BasicFileAttributeView view = Files.getFileAttributeView(path, BasicFileAttributeView.class);
        view.setTimes(pathTime, pathTime, null);
        if (IS_WINDOWS) {
            timeNanos = 100L * (timeNanos / 100L);
        }
        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
        String[] timeNames = new String[] { "modification", "access" };
        FileTime[] times = new FileTime[] { attrs.lastModifiedTime(), attrs.lastAccessTime() };
        for (int i = 0; i < timeNames.length; i++) {
            long nanos = times[i].to(TimeUnit.NANOSECONDS);
            if (nanos != timeNanos) {
                throw new RuntimeException("Expected " + timeNames[i] + " timestamp to be '" + timeNanos + "', but was '" + nanos + "'");
            }
        }
    }
}
