

import java.io.*;
import com.sun.tools.classfile.*;



public class JSR175Annotations {
    public static void main(String[] args) throws Exception {
        new JSR175Annotations().run();
    }

    public void run() throws Exception {
        File javaFile = writeTestFile();
        File classFile = compileTestFile(javaFile);

        ClassFile cf = ClassFile.read(classFile);
        for (Field f : cf.fields) {
            test(cf, f);
        }
        for (Method m: cf.methods) {
            test(cf, m);
        }

        countAnnotations();

        if (errors > 0)
            throw new Exception(errors + " errors found");
        System.out.println("PASSED");
    }

    void test(ClassFile cf, Method m) {
        test(cf, m, Attribute.RuntimeVisibleTypeAnnotations, true);
        test(cf, m, Attribute.RuntimeInvisibleTypeAnnotations, false);
    }

    void test(ClassFile cf, Field m) {
        test(cf, m, Attribute.RuntimeVisibleTypeAnnotations, true);
        test(cf, m, Attribute.RuntimeInvisibleTypeAnnotations, false);
    }

    
    
    void test(ClassFile cf, Method m, String name, boolean visible) {
        int index = m.attributes.getIndex(cf.constant_pool, name);
        if (index != -1) {
            Attribute attr = m.attributes.get(index);
            assert attr instanceof RuntimeTypeAnnotations_attribute;
            RuntimeTypeAnnotations_attribute tAttr = (RuntimeTypeAnnotations_attribute)attr;
            all += tAttr.annotations.length;
            if (visible)
                visibles += tAttr.annotations.length;
            else
                invisibles += tAttr.annotations.length;
        }
    }

    
    
    void test(ClassFile cf, Field m, String name, boolean visible) {
        int index = m.attributes.getIndex(cf.constant_pool, name);
        if (index != -1) {
            Attribute attr = m.attributes.get(index);
            assert attr instanceof RuntimeTypeAnnotations_attribute;
            RuntimeTypeAnnotations_attribute tAttr = (RuntimeTypeAnnotations_attribute)attr;
            all += tAttr.annotations.length;
            if (visible)
                visibles += tAttr.annotations.length;
            else
                invisibles += tAttr.annotations.length;
        }
    }

    File writeTestFile() throws IOException {
        File f = new File("Test.java");
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
        out.println("import java.lang.annotation.*;");
        out.println("abstract class Test { ");
        out.println("  @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})");
        out.println("  @Retention(RetentionPolicy.RUNTIME)");
        out.println("  @interface A { }");
        out.println("  @A String m;");
        out.println("  @A String method(@A String a) {");
        out.println("    return a;");
        out.println("  }");
        out.println("}");
        out.close();
        return f;
    }

    File compileTestFile(File f) {
        int rc = com.sun.tools.javac.Main.compile(new String[] {"-g", f.getPath() });
        if (rc != 0)
            throw new Error("compilation failed. rc=" + rc);
        String path = f.getPath();
        return new File(path.substring(0, path.length() - 5) + ".class");
    }

    void countAnnotations() {
        int expected_visibles = 0, expected_invisibles = 0;
        int expected_all = expected_visibles + expected_invisibles;

        if (expected_all != all) {
            errors++;
            System.err.println("expected " + expected_all
                    + " annotations but found " + all);
        }

        if (expected_visibles != visibles) {
            errors++;
            System.err.println("expected " + expected_visibles
                    + " visibles annotations but found " + visibles);
        }

        if (expected_invisibles != invisibles) {
            errors++;
            System.err.println("expected " + expected_invisibles
                    + " invisibles annotations but found " + invisibles);
        }

    }

    int errors;
    int all;
    int visibles;
    int invisibles;
}
