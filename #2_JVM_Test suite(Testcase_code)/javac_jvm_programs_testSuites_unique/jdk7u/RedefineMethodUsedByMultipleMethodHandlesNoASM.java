



import java.io.*;
import java.lang.instrument.*;
import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.management.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.security.*;
import java.util.jar.*;

import javax.tools.*;

public class RedefineMethodUsedByMultipleMethodHandlesNoASM {

    static class Foo {
        public static Object getName() {
            int fooInt = 1;
            if (true) {
                
                
                
                fooInt <<= 0x7;
            }
            return "foo" + fooInt;
        }
    }

    public static void main(String[] args) throws Throwable {

        Lookup lookup = MethodHandles.lookup();
        Method fooMethod = Foo.class.getDeclaredMethod("getName");

        
        MethodHandle fooMH1 = lookup.unreflect(fooMethod);
        MethodHandle fooMH2 = lookup.unreflect(fooMethod);

        System.out.println("Foo.getName() = " + Foo.getName());
        System.out.println("fooMH1.invoke = " + fooMH1.invokeExact());
        System.out.println("fooMH2.invoke = " + fooMH2.invokeExact());

        
        
        redefineFoo();

        
        System.gc();

        
        
        Object newResult = fooMH1.invokeExact();
        System.out.println("fooMH1.invoke = " + fooMH1.invokeExact());
        if (!((String) newResult).equals("foo32")) {
            throw new RuntimeException("failed, fooMH1 invoke gets '" + newResult + "'");
        }
    }

    
    static void add(JarOutputStream jar, Class<?> c) throws IOException {
        String classAsPath = c.getName().replace('.', '/') + ".class";
        jar.putNextEntry(new JarEntry(classAsPath));
        InputStream stream = c.getClassLoader().getResourceAsStream(classAsPath);

        int b;
        while ((b = stream.read()) != -1) {
            jar.write(b);
        }
    }

    static void redefineFoo() throws Exception {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        Attributes mainAttrs = manifest.getMainAttributes();
        mainAttrs.putValue("Agent-Class", FooAgent.class.getName());
        mainAttrs.putValue("Can-Redefine-Classes", "true");
        mainAttrs.putValue("Can-Retransform-Classes", "true");

        Path jar = Files.createTempFile("myagent", ".jar");
        try {
            JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(jar.toFile()), manifest);
            add(jarStream, FooAgent.class);
            add(jarStream, FooTransformer.class);
            jarStream.close();
            runAgent(jar);
        } finally {
            Files.deleteIfExists(jar);
        }
    }

    public static void runAgent(Path agent) throws Exception {
        String vmName = ManagementFactory.getRuntimeMXBean().getName();
        int p = vmName.indexOf('@');
        assert p != -1 : "VM name not in <pid>@<host> format: " + vmName;
        String pid = vmName.substring(0, p);
        ClassLoader cl = ToolProvider.getSystemToolClassLoader();
        Class<?> c = Class.forName("com.sun.tools.attach.VirtualMachine", true, cl);
        Method attach = c.getDeclaredMethod("attach", String.class);
        Method loadAgent = c.getDeclaredMethod("loadAgent", String.class);
        Method detach = c.getDeclaredMethod("detach");
        Object vm = attach.invoke(null, pid);
        loadAgent.invoke(vm, agent.toString());
        detach.invoke(vm);
    }

    public static class FooAgent {

        public static void agentmain(@SuppressWarnings("unused") String args, Instrumentation inst) throws Exception {
            assert inst.isRedefineClassesSupported();
            assert inst.isRetransformClassesSupported();
            inst.addTransformer(new FooTransformer(), true);
            Class<?>[] classes = inst.getAllLoadedClasses();
            for (int i = 0; i < classes.length; i++) {
                Class<?> c = classes[i];
                if (c == Foo.class) {
                    inst.retransformClasses(new Class[]{c});
                }
            }
        }
    }


    
    static class FooTransformer implements ClassFileTransformer {

        @Override
        public byte[] transform(ClassLoader cl, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                                byte[] classfileBuffer) throws IllegalClassFormatException {


            if (Foo.class.equals(classBeingRedefined)) {

                try {
                    System.out.println("redefining " + classBeingRedefined);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    InputStream is = new ByteArrayInputStream(classfileBuffer);
                    copyWithSubstitution(is, new byte[] {(byte)0x10,(byte)0x07,(byte)0x78},
                                         new byte[] {(byte)0x10,(byte)0x05,(byte)0x78},
                                         baos);
                    return baos.toByteArray();
                } catch(Exception e) {
                     e.printStackTrace();
                }
            }
            return classfileBuffer;
        }

        
        public void copyWithSubstitution(InputStream is, byte[] oldBytes, byte [] newBytes, OutputStream out) throws Exception {

            byte[] buffer = new byte[oldBytes.length];

            while (is.available() > 0) {
                int i = 0xff & is.read();
                if (i != oldBytes[0]) {
                    out.write(i);
                    continue;
                }
                int pos = 0;
                while (pos < oldBytes.length && oldBytes[pos] == (byte) i) {
                    buffer[pos] = (byte) i;
                    pos++;
                    i = is.read();
                }
                
                
                
                
                if (pos > 0) {
                if (pos == oldBytes.length) {
                    System.out.println("copyWithSubstitution: replacing: ");
                    printBytesOn(System.out, buffer);
                    System.out.println("copyWithSubstitution: with:");
                    printBytesOn(System.out, newBytes);
                    out.write(newBytes, 0, newBytes.length);
                } else {
                    out.write(buffer, 0, pos);
                }
                }
                
                out.write(i);
            }
            out.close();
        }


    public static void printBytesOn(PrintStream out, byte[] bytes) {
        int numColumns =  16;
        int column = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (column == 0) {
                out.print(i);
                out.print("\t");
            }
            out.print("0x" + Integer.toHexString(255 & bytes[i])
                     + "\t");
            column++;
            if (column == numColumns) {
                out.println();
                column = 0;
            }
        }
        out.println();
    }
    }
}
