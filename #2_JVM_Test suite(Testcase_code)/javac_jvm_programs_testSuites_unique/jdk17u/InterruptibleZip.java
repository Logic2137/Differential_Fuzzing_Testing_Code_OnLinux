import java.io.*;
import java.util.zip.*;

public class InterruptibleZip {

    public static void main(String[] args) throws Exception {
        Thread.currentThread().interrupt();
        ZipFile zf = new ZipFile(new File(System.getProperty("test.src", "."), "input.jar"));
        ZipEntry ze = zf.getEntry("Available.java");
        InputStream is = zf.getInputStream(ze);
        byte[] buf = new byte[512];
        int n = is.read(buf);
        boolean interrupted = Thread.interrupted();
        System.out.printf("interrupted=%s n=%d name=%s%n", interrupted, n, ze.getName());
        if (!interrupted) {
            throw new Error("Wrong interrupt status");
        }
        if (n != buf.length) {
            throw new Error("Read error");
        }
    }
}
