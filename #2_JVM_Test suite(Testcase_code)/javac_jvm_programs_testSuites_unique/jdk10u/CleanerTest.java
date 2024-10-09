



import com.sun.management.UnixOperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CleanerTest {
    public static void main(String[] args) throws Throwable {
        OperatingSystemMXBean mxBean =
            ManagementFactory.getOperatingSystemMXBean();
        UnixOperatingSystemMXBean unixMxBean = null;
        if (mxBean instanceof UnixOperatingSystemMXBean) {
            unixMxBean = (UnixOperatingSystemMXBean)mxBean;
        } else {
            System.out.println("Non-Unix system: skipping test.");
            return;
        }

        Path path = Paths.get(System.getProperty("test.dir", "."), "junk");
        try {
            FileChannel fc = FileChannel.open(path, StandardOpenOption.CREATE,
                StandardOpenOption.READ, StandardOpenOption.WRITE);

            ReferenceQueue refQueue = new ReferenceQueue();
            Reference fcRef = new PhantomReference(fc, refQueue);

            long fdCount0 = unixMxBean.getOpenFileDescriptorCount();
            fc = null;

            
            do {
                Thread.sleep(1);
                System.gc();
            } while (refQueue.poll() == null);

            
            while (unixMxBean.getOpenFileDescriptorCount() > fdCount0 - 1) {
                Thread.sleep(1);
            }

            long fdCount = unixMxBean.getOpenFileDescriptorCount();
            if (fdCount != fdCount0 - 1) {
                throw new RuntimeException("FD count expected " +
                    (fdCount0 - 1) + "; actual " + fdCount);
            }
        } finally {
            Files.delete(path);
        }
    }
}
