

package p;

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class BootstrapClassPathAgent {

    public static void premain(String args, Instrumentation inst) {
        System.out.println("agent loader=" + BootstrapClassPathAgent.class.getClassLoader());
        inst.addTransformer(new MyTransformer());
    }

    static class MyTransformer implements ClassFileTransformer {

        public byte[] transform(Module module,
                                String className,
                                Class<?> classBeingRedefined,
                                ProtectionDomain    protectionDomain,
                                byte[] classfileBuffer) {
            System.out.println(className + ", module=" + module);
            return null;
        }
    }
}
