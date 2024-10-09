import java.io.IOException;
import java.io.InputStream;
import java.lang.module.ModuleDescriptor;
import java.lang.module.InvalidModuleDescriptorException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CheckBadModuleInfo {

    public static void main(String[] args) throws IOException {
        Path mi = Path.of(System.getProperty("test.classes"), "module-info.class");
        try (InputStream in = Files.newInputStream(mi)) {
            try {
                ModuleDescriptor descriptor = ModuleDescriptor.read(in);
                System.out.println(descriptor);
                throw new RuntimeException("InvalidModuleDescriptorException expected");
            } catch (InvalidModuleDescriptorException e) {
                System.out.println(e);
            }
        }
    }
}
