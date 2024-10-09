

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ClassFileInstaller {
    
    public static boolean DEBUG = Boolean.getBoolean("ClassFileInstaller.debug");

    
    public static void main(String... args) throws Exception {
        if (args.length > 1 && args[0].equals("-jar")) {
            if (args.length < 2) {
                throw new RuntimeException("Usage: ClassFileInstaller <options> <classes>\n" +
                                           "where possible options include:\n" +
                                           "  -jar <path>             Write to the JAR file <path>");
            }
            writeJar(args[1], null, args, 2, args.length);
        } else {
            if (DEBUG) {
                System.out.println("ClassFileInstaller: Writing to " + System.getProperty("user.dir"));
            }
            for (String arg : args) {
                writeClassToDisk(arg);
            }
        }
    }

    public static class Manifest {
        private InputStream in;

        private Manifest(InputStream in) {
            this.in = in;
        }

        static Manifest fromSourceFile(String fileName) throws Exception {
            String pathName = System.getProperty("test.src") + File.separator + fileName;
            return new Manifest(new FileInputStream(pathName));
        }

        
        
        
        
        
        
        static Manifest fromString(String manifest) throws Exception {
            return new Manifest(new ByteArrayInputStream(manifest.getBytes()));
        }

        public InputStream getInputStream() {
            return in;
        }
    }

    private static void writeJar(String jarFile, Manifest manifest, String classes[], int from, int to) throws Exception {
        if (DEBUG) {
            System.out.println("ClassFileInstaller: Writing to " + getJarPath(jarFile));
        }

        (new File(jarFile)).delete();
        FileOutputStream fos = new FileOutputStream(jarFile);
        ZipOutputStream zos = new ZipOutputStream(fos);

        
        
        if (manifest != null) {
            writeToDisk(zos, "META-INF/MANIFEST.MF", manifest.getInputStream());
        }

        for (int i=from; i<to; i++) {
            writeClassToDisk(zos, classes[i]);
        }

        zos.close();
        fos.close();
    }

    
    public static String writeJar(String jarFile, String... classes) throws Exception {
        writeJar(jarFile, null, classes, 0, classes.length);
        return getJarPath(jarFile);
    }

    public static String writeJar(String jarFile, Manifest manifest, String... classes) throws Exception {
        writeJar(jarFile, manifest, classes, 0, classes.length);
        return getJarPath(jarFile);
    }

    
    public static String getJarPath(String jarFileName) {
        return new File(jarFileName).getAbsolutePath();
    }

    public static void writeClassToDisk(String className) throws Exception {
        writeClassToDisk((ZipOutputStream)null, className);
    }
    private static void writeClassToDisk(ZipOutputStream zos, String className) throws Exception {
        writeClassToDisk(zos, className, "");
    }

    public static void writeClassToDisk(String className, String prependPath) throws Exception {
        writeClassToDisk(null, className, prependPath);
    }
    private static void writeClassToDisk(ZipOutputStream zos, String className, String prependPath) throws Exception {
        ClassLoader cl = ClassFileInstaller.class.getClassLoader();

        
        String pathName = className.replace('.', '/').concat(".class");
        InputStream is = cl.getResourceAsStream(pathName);
        if (is == null) {
            throw new RuntimeException("Failed to find " + pathName);
        }
        if (prependPath.length() > 0) {
            pathName = prependPath + "/" + pathName;
        }
        writeToDisk(zos, pathName, is);
    }

    public static void writeClassToDisk(String className, byte[] bytecode) throws Exception {
        writeClassToDisk(null, className, bytecode);
    }
    private static void writeClassToDisk(ZipOutputStream zos, String className, byte[] bytecode) throws Exception {
        writeClassToDisk(zos, className, bytecode, "");
    }

    public static void writeClassToDisk(String className, byte[] bytecode, String prependPath) throws Exception {
        writeClassToDisk(null, className, bytecode, prependPath);
    }
    private static void writeClassToDisk(ZipOutputStream zos, String className, byte[] bytecode, String prependPath) throws Exception {
        
        String pathName = className.replace('.', '/').concat(".class");
        if (prependPath.length() > 0) {
            pathName = prependPath + "/" + pathName;
        }
        writeToDisk(zos, pathName, new ByteArrayInputStream(bytecode));
    }

    private static void writeToDisk(ZipOutputStream zos, String pathName, InputStream is) throws Exception {
        if (DEBUG) {
            System.out.println("ClassFileInstaller: Writing " + pathName);
        }
        if (zos != null) {
            ZipEntry ze = new ZipEntry(pathName);
            zos.putNextEntry(ze);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf))>0){
                zos.write(buf, 0, len);
            }
        } else {
            
            Path p = Paths.get(pathName);
            if (pathName.contains("/")) {
                Files.createDirectories(p.getParent());
            }
            
            Files.copy(is, p, StandardCopyOption.REPLACE_EXISTING);
        }
        is.close();
    }
}
