import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;




public class ByteClassLoader extends URLClassLoader {

    final static boolean verbose
            = Boolean.getBoolean("byteclassloader.verbose");

    final boolean read;
    final JarOutputStream jos;
    final String jar_name;

    
    public ByteClassLoader(String jar_name, boolean read, boolean write)
            throws FileNotFoundException, IOException {
        super(read
                ? new URL[]{new URL("file:" + jar_name + ".jar")}
                : new URL[0]);
        this.read = read;
        this.jar_name = jar_name;
        this.jos = write
                ? new JarOutputStream(
                new BufferedOutputStream(
                new FileOutputStream(jar_name + ".jar"))) : null;
        if (read && write) {
            throw new Error("At most one of read and write may be true.");
        }
    }

    private static void writeJarredFile(JarOutputStream jos, String file, String suffix, byte[] bytes) {
        String fileName = file.replace(".", "/") + "." + suffix;
        JarEntry ze = new JarEntry(fileName);
        try {
            ze.setSize(bytes.length);
            jos.putNextEntry(ze);
            jos.write(bytes);
            jos.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    
    public Class<?> loadBytes(String name, byte[] classData) throws ClassNotFoundException {
        if (jos != null) {
            if (verbose) {
                System.out.println("ByteClassLoader: writing " + name);
            }
            writeJarredFile(jos, name, "class", classData);
        }

        Class<?> clazz = null;
        if (read) {
            if (verbose) {
                System.out.println("ByteClassLoader: reading " + name + " from " + jar_name);
            }
            clazz = loadClass(name);
        } else {
            clazz = defineClass(name, classData, 0, classData.length);
            resolveClass(clazz);
        }
        return clazz;
    }

    public void close() {
        if (jos != null) {
            try {
                if (verbose) {
                    System.out.println("ByteClassLoader: closing " + jar_name);
                }
                jos.close();
            } catch (IOException ex) {
            }
        }
    }
}
