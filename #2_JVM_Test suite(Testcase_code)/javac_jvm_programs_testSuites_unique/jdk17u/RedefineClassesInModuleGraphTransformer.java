import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class RedefineClassesInModuleGraphTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String name, Class<?> classBeingRedefined, ProtectionDomain pd, byte[] buffer) throws IllegalClassFormatException {
        System.out.println("transforming " + name);
        if (name.equals("java/lang/Module") || name.equals("java/lang/ModuleLayer") || name.equals("java/lang/module/ResolvedModule")) {
            throw new RuntimeException("Classes used by the module graph should never be transformed by Java agent");
        }
        return null;
    }
}
