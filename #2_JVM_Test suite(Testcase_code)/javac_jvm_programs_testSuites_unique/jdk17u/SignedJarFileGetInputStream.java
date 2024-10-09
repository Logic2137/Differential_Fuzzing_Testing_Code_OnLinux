import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;

public class SignedJarFileGetInputStream {

    public static void main(String[] args) throws Throwable {
        JarFile jar = new JarFile(new File(System.getProperty("test.src", "."), "Signed.jar"));
        for (Enumeration e = jar.entries(); e.hasMoreElements(); ) {
            JarEntry entry = (JarEntry) e.nextElement();
            InputStream is = jar.getInputStream(new ZipEntry(entry.getName()));
            is.close();
        }
        InputStream is = jar.getInputStream(new ZipEntry("Test.class"));
        is.close();
        byte[] buffer = new byte[1];
        try {
            is.read();
            throw new AssertionError("Should have thrown IOException");
        } catch (IOException success) {
        }
        try {
            is.read(buffer);
            throw new AssertionError("Should have thrown IOException");
        } catch (IOException success) {
        }
        try {
            is.read(buffer, 0, buffer.length);
            throw new AssertionError("Should have thrown IOException");
        } catch (IOException success) {
        }
        try {
            is.available();
            throw new AssertionError("Should have thrown IOException");
        } catch (IOException success) {
        }
    }
}
