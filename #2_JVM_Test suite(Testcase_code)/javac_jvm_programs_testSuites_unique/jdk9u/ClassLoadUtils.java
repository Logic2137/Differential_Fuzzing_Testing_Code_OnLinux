
package utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

public class ClassLoadUtils {

    private ClassLoadUtils() {
    }

    
    public static String getClassPath(String className) {
        String fileName = className.replace(".", File.separator) + ".class";
        String[] classPath = System.getProperty("java.class.path").split(File.pathSeparator);
        File target = null;
        int i;
        for (i = 0; i < classPath.length; ++i) {
            target = new File(classPath[i] + File.separator + fileName);
            System.out.println("Try: " + target);
            if (target.exists()) {
                break;
            }
        }
        if (i != classPath.length) {
            return classPath[i];
        }
        return null;
    }

    
    public static String getClassPathFileName(String className) {
        String fileName = className.replace(".", File.separator) + ".class";
        String[] classPath = System.getProperty("java.class.path").split(File.pathSeparator);
        File target = null;
        int i;
        for (i = 0; i < classPath.length; ++i) {
            target = new File(classPath[i] + File.separator + fileName);
            System.out.println("Try: " + target);
            if (target.exists()) {
                break;
            }
        }
        if (i != classPath.length) {
            try {
                return target.getCanonicalPath();
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    public static String getRedefineClassFileName(String dir, String className) {
        String fileName = getClassPathFileName(className);
        if (fileName == null) {
            return null;
        }
        if (fileName.contains("classes")) {
            return fileName.replace("classes", dir);
        } else {
            String classPath = getClassPath(className);
            if (classPath != null) {
                return classPath + File.separator + "newclass" + File.separator + className.replace(".", File.separator) + ".class";
            } else {
                return null;
            }
        }
    }

    
    public static String getRedefineClassFileName(String className) {
        return getRedefineClassFileName("newclass", className);
    }

    
    public static byte[] readFile(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        long countl = file.length();
        if (countl > Integer.MAX_VALUE) {
            throw new IOException("File is too huge");
        }
        int count = (int) countl;
        byte[] buffer = new byte[count];
        int n = 0;
        try {
            while (n < count) {
                int k = in.read(buffer, n, count - n);
                if (k < 0) {
                    throw new IOException("Unexpected EOF");
                }
                n += k;
            }
        } finally {
            in.close();
        }
        return buffer;
    }

    
    public static byte[] readFile(String name) throws IOException {
        return readFile(new File(name));
    }
}
