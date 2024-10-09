
package nsk.share;

import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Arrays;

public class ClassFileFinder {

    private ClassFileFinder() {
    }

    public static Path findClassFile(String name, String classPath) {
        return Arrays.stream(classPath.split(File.pathSeparator)).map(java.nio.file.Paths::get).map(p -> p.resolve(name.replace('.', File.separatorChar) + ".class")).filter(p -> java.nio.file.Files.exists(p)).map(Path::toAbsolutePath).findAny().orElse(null);
    }
}
