

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;


public class CopyClassFile {

    private static final ClassLoader cl = CopyClassFile.class.getClassLoader();

    private static String destinationDir;
    private static String className;
    private static String classFile;

    private static boolean removeSource = false;

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Illegal usage: class name and destination directory should be specified");
        }

        int classNameIndex = parseOptions(args);

        className = args[classNameIndex];
        destinationDir = args[classNameIndex + 1];
        classFile = className.replaceAll("\\.", File.separator) + ".class";

        URL url = cl.getResource(classFile);
        if (url == null) {
            throw new RuntimeException("Could not find a class: " + classFile);
        }

        File[] files = new File(url.toURI())
                .getParentFile()
                .listFiles((dir, name) -> name.startsWith(cutPackageName(className)) && name.endsWith(".class"));

        Arrays.stream(files).forEach(CopyClassFile::copyFile);
    }

    private static int parseOptions(String[] args) {
        int optionsEnd = 0;
        while (args[optionsEnd].startsWith("-")) {
            switch (args[optionsEnd].substring(1)) {
                case "r" :
                    removeSource = true;
                    break;
                default:
                    throw new RuntimeException("Unrecognized option passed to CopyClassFile: " + args[optionsEnd]);
            }
            optionsEnd++;
        }
        return optionsEnd;
    }

    private static String cutPackageName(String className) {
        int dotIndex = className.lastIndexOf(".") + 1;
        if (dotIndex <= 0) {
            return className;
        } else {
            return className.substring(dotIndex);
        }
    }

    private static void copyFile(File f) {
        try {
            Path classFilePath = Paths.get(classFile);
            String packagePath = classFilePath.getParent() == null ? "" : classFilePath.getParent().toString();
            Path p = Paths.get(destinationDir + packagePath + File.separator + f.getName());
            Files.createDirectories(p.getParent());
            try (InputStream is = new FileInputStream(f)) {
                Files.copy(is, p, StandardCopyOption.REPLACE_EXISTING);
            }

            if (removeSource && !f.delete()) {
                throw new RuntimeException("Failed to delete a file");
            }

        } catch (IOException ex) {
            throw new RuntimeException("Could not copy file " + f, ex);
        }
    }
}
