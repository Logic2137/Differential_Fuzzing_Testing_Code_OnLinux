



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TargetDirectory {
    public static void main(String[] args) throws Exception {
        
        Path dir = Path.of("target");
        File target = Files.createDirectory(dir).toFile();
        File tmp = File.createTempFile("passes", null, target);
        if (!Files.exists(tmp.toPath())) {
            throw new RuntimeException("Temp file not created");
        }
        tmp.delete();

        
        if (Files.getFileStore(dir).supportsFileAttributeView("posix")) {
            PosixFileAttributeView view =
                Files.getFileAttributeView(dir, PosixFileAttributeView.class);
            Set<PosixFilePermission> perms = new HashSet<>();
            perms.add(PosixFilePermission.valueOf("OWNER_READ"));
            view.setPermissions(perms);
        } else if (Files.getFileStore(dir).supportsFileAttributeView("acl")) {
            AclFileAttributeView view = Files.getFileAttributeView(dir,
                AclFileAttributeView.class);
            List<AclEntry> entries = new ArrayList<>();
            for (AclEntry entry : view.getAcl()) {
                Set<AclEntryPermission> perms =
                    new HashSet<>(entry.permissions());
                perms.remove(AclEntryPermission.ADD_FILE);
                entries.add(AclEntry.newBuilder().setType(entry.type())
                    .setPrincipal(entry.principal()).setPermissions(perms)
                    .build());
            }
            view.setAcl(entries);
        } else {
            throw new RuntimeException("Required attribute view not supported");
        }

        
        try {
            File.createTempFile("readonly", null, target);
            throw new RuntimeException("Exception not thrown for read-only target directory");
        } catch (IOException expected) {
        } finally {
            target.delete();
        }

        
        try {
            File.createTempFile("nonexistent", null, new File("void"));
            throw new RuntimeException("Exception not thrown for non-existent target directory");
        } catch (IOException expected) {
        }

        
        target = Files.createFile(Path.of("file")).toFile();
        try {
            File.createTempFile("file", null, target);
            throw new RuntimeException("Exception not thrown for file target");
        } catch (IOException expected) {
        } finally {
            target.delete();
        }
    }
}
