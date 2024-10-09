
package vm.share.transform;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public abstract class AbstractClassFileTransformer
        implements ClassFileTransformer {
    protected abstract boolean shouldBeTransformed(String name);

    protected abstract byte[] transformClass(byte[] bytes);

    @Override
    public byte[] transform(ClassLoader loader, String className,
            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {
        if (shouldBeTransformed(className)) {
            return transformClass(classfileBuffer);
        }
        return null;
    }
}
