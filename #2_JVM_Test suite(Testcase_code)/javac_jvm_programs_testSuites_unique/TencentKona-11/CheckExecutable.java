



import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import static java.nio.file.attribute.PosixFilePermission.*;
import java.util.EnumSet;
import java.util.Set;

public class CheckExecutable {

    
    private static final String IGNORE = "glob:{*.diz,jmc.ini}";

    public static void main(String args[]) throws IOException {
        String JAVA_HOME = System.getProperty("java.home");
        Path bin = Paths.get(JAVA_HOME, "bin");

        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(IGNORE);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(bin)) {
            EnumSet<PosixFilePermission> execPerms
                = EnumSet.of(GROUP_EXECUTE, OTHERS_EXECUTE, OWNER_EXECUTE);

            for (Path entry : stream) {
                Path file = entry.getFileName();
                if (!Files.isRegularFile(entry) || matcher.matches(file)) {
                    continue;
                }

                if (!Files.isExecutable(entry))
                    throw new RuntimeException(entry + " is not executable!");

                try {
                    Set<PosixFilePermission> perm
                        = Files.getPosixFilePermissions(entry);
                    if (!perm.containsAll(execPerms)) {
                        throw new RuntimeException(entry
                            + " has not all executable permissions!\n"
                            + "Should have: " + execPerms + "\nbut has: " + perm);
                    }
                } catch (UnsupportedOperationException uoe) { }

            }

        }
    }
}
