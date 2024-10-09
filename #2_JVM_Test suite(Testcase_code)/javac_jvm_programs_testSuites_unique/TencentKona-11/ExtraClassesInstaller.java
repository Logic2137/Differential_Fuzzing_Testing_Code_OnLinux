

package nsk.jdi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExtraClassesInstaller {
    public static void main(String[] args) {
        Path target = Paths.get("bin")
                           .resolve(args[0]);
        ClassLoader cl = ExtraClassesInstaller.class.getClassLoader();
        for (int i = 1; i < args.length; ++i) {

            String pathName = args[i];
            try {
                try (InputStream is = cl.getResourceAsStream(pathName)) {
                    if (is == null) {
                        throw new Error("can't find " + pathName);
                    }
                    Path file = target.resolve(pathName);
                    Path dir = file.getParent();
                    try {
                        Files.createDirectories(dir);
                    } catch (IOException e) {
                        throw new Error("can't create dir " + dir, e);
                    }
                    try {
                        Files.copy(is, file);
                    } catch (IOException e) {
                        throw new Error("can't write to " + file, e);
                    }
                }
            } catch (IOException e) {
                throw new Error("IOE on closing " + pathName + " resource stream", e);
            }
        }
    }
}
