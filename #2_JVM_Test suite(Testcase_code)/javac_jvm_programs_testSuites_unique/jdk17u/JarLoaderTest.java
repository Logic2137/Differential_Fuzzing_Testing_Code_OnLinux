import java.io.*;
import java.net.*;
import java.util.zip.*;

public class JarLoaderTest {

    public static void main(String[] args) throws Exception {
        File f = new File("urlcl" + 1 + ".jar");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(f));
        zos.putNextEntry(new ZipEntry("TestResource"));
        byte[] b = "This is a test resource".getBytes();
        zos.write(b, 0, b.length);
        zos.close();
        URLClassLoader cl = new URLClassLoader(new URL[] { new URL("jar:" + f.toURI().toURL() + "!/") }, null);
        cl.getResource("TestResource");
        cl.close();
        f.delete();
        if (f.exists()) {
            System.out.println("Test FAILED: Closeables failed to close handle to jar file");
            for (URL u : cl.getURLs()) {
                if (u.getProtocol().equals("jar")) {
                    ((JarURLConnection) u.openConnection()).getJarFile().close();
                }
                f.delete();
            }
            throw new RuntimeException("File could not be deleted");
        } else {
            System.out.println("Test PASSED");
        }
    }
}
