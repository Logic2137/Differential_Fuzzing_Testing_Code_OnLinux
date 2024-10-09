

package nsk.share.classload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import java.util.Arrays;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

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
        String fileName = className.replace(".", File.separator) + ".class";
        return Arrays.stream(System.getProperty("java.class.path").split(File.pathSeparator))
                     .map(Paths::get)
                     .map(p -> p.resolve(dir))
                     .map(p -> p.resolve(fileName))
                     .filter(p -> Files.exists(p))
                     .map(Path::toAbsolutePath)
                     .map(Path::toString)
                     .findAny()
                     .orElse(null);
    }

    
    public static String getRedefineClassFileName(String className) {
            return getRedefineClassFileName("newclass", className);
    }
}
