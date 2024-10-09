
package vm.share;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


public class FileUtils {

    private static ClassLoader cl = ClassLoader.getSystemClassLoader();

    public static byte[] readFile(File f) throws IOException {
        FileInputStream is = new FileInputStream(f);
        try {
            return readStream(is);
        } finally {
            is.close();
        }
    }

    public static byte[] readClass(String name) throws IOException {
        return readResource(name.replace('.', '/') + ".class");
    }

    public static byte[] readResource(String name) throws IOException {
        InputStream is = FileUtils.cl.getResourceAsStream(name);
        if (is == null)
            throw new IOException("Can't read resource " + name);

        try {
            return readStream(is);
        } finally {
            is.close();
        }
    }

    public static byte[] readStream(InputStream is) throws IOException {
        byte buf[] = new byte[0xFFFF];
        int offset = 0;
        int r;
        while ((r = is.read(buf, offset, buf.length - offset)) > 0)
            offset += r;
        return Arrays.copyOf(buf, offset);
    }

    public static void writeBytesToFile(File file, byte[] buf)
            throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        try {
            fos.write(buf);
        } finally {
            fos.close();
        }
    }

}
