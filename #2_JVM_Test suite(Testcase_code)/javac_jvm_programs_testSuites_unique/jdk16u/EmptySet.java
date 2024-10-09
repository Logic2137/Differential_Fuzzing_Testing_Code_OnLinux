



import java.nio.file.attribute.*;
import java.util.*;


public class EmptySet {
    public static void main(String[] args) {
        Set<AclEntryFlag> flags = new HashSet<>();
        AclEntry.newBuilder().setFlags(flags);

        Set<AclEntryPermission> perms = new HashSet<>();
        AclEntry.newBuilder().setPermissions(perms);
    }
}
