import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;

public class RememberAppend {

    private static final byte[] bytes = "ABC ".getBytes();

    public static void main(String[] args) throws Throwable {
        File f = File.createTempFile("tmp.file", null);
        f.deleteOnExit();
        try (FileOutputStream fos1 = new FileOutputStream(f.getPath(), true)) {
            fos1.write(bytes);
        }
        try (FileOutputStream fos1 = new FileOutputStream(f.getPath(), true);
            FileOutputStream fos2 = new FileOutputStream(fos1.getFD())) {
            fos2.write(bytes);
        }
        if (f.length() != 2 * bytes.length) {
            throw new RuntimeException("Append flag ignored");
        }
    }
}
