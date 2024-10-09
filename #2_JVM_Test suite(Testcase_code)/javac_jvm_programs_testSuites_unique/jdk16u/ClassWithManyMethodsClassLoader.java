

import java.io.DataInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;


public class ClassWithManyMethodsClassLoader extends ClassLoader {
    
    private static boolean deleteFiles = Boolean.parseBoolean(
        System.getProperty("ClassWithManyMethodsClassLoader.deleteFiles", "true"));

    private JavaCompiler javac;

    public ClassWithManyMethodsClassLoader() {
        javac = ToolProvider.getSystemJavaCompiler();
    }

    private String generateSource(String className, String methodPrefix, int methodCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("public class ")
            .append(className)
            .append("{\n");

        for (int i = 0; i < methodCount; i++) {
            sb.append("public void ")
                .append(methodPrefix)
                .append(i)
                .append("() {}\n");
        }

        sb.append("\n}");

        return sb.toString();
    }

    private byte[] generateClassBytes(String className, String methodPrefix, int methodCount) throws IOException {
        String src = generateSource(className, methodPrefix, methodCount);
        File file = new File(className + ".java");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.append(src);
            pw.flush();
        }
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        int exitcode = javac.run(null, null, err, file.getCanonicalPath());
        if (exitcode != 0) {
            
            System.err.print(err);
            if (err.toString().contains("java.lang.OutOfMemoryError: Java heap space")) {
                throw new OutOfMemoryError("javac failed with resources exhausted");
            } else {
              throw new RuntimeException("javac failure when compiling: " +
                      file.getCanonicalPath());
            }
        } else {
            if (deleteFiles) {
                file.delete();
            }
        }

        File classFile = new File(className + ".class");
        byte[] bytes;
        try (DataInputStream dis = new DataInputStream(new FileInputStream(classFile))) {
            bytes = new byte[dis.available()];
            dis.readFully(bytes);
        }
        if (deleteFiles) {
            classFile.delete();
        }

        return bytes;
    }

    public Class<?> create(String className, String methodPrefix, int methodCount) throws IOException {
        byte[] bytes = generateClassBytes(className, methodPrefix, methodCount);
        return defineClass(className, bytes, 0, bytes.length);
    }
}
