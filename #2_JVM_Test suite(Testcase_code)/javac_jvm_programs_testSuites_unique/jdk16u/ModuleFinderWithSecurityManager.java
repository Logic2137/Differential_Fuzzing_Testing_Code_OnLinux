



import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class ModuleFinderWithSecurityManager {
    public static void main(String[] args) throws Exception {
        boolean allow = args[0].equals("allow");

        
        if (allow) {
            String testSrc = System.getProperty("test.src");
            if (testSrc == null)
                testSrc = ".";
            Path policyFile = Paths.get(testSrc, "java.policy");
            System.setProperty("java.security.policy", policyFile.toString());
        }

        System.setSecurityManager(new SecurityManager());

        ModuleFinder finder = null;
        try {
            finder = ModuleFinder.ofSystem();
            if (!allow) throw new RuntimeException("SecurityException expected");
        } catch (SecurityException e) {
            if (allow) throw new RuntimeException("SecurityException not expected");
        }

        
        if (finder != null) {
            ModuleReference base = finder.find("java.base").orElse(null);
            if (base == null)
                throw new RuntimeException("java.base not found");
            Set<ModuleReference> allModules = finder.findAll();
            if (!allModules.contains(base))
                throw new RuntimeException("java.base not in all modules");
        }
    }
}
