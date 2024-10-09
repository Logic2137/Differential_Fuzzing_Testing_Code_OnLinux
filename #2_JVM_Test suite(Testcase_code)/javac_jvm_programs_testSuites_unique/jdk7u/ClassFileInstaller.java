

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class ClassFileInstaller {
    
    public static void main(String... args) throws Exception {
        for (String arg : args) {
            ClassLoader cl = ClassFileInstaller.class.getClassLoader();

            
            String pathName = arg.replace('.', '/').concat(".class");
            InputStream is = cl.getResourceAsStream(pathName);

            
            Path p = Paths.get(pathName);
            if (pathName.contains("/")) {
                Files.createDirectories(p.getParent());
            }
            
            Files.copy(is, p, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
