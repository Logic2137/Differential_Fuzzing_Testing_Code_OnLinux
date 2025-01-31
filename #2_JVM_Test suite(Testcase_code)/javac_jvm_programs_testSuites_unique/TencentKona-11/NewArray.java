import java.io.*;
import com.sun.tools.classfile.*;

public class NewArray {

    public static void main(String[] args) throws Exception {
        new NewArray().run();
    }

    public void run() throws Exception {
        File javaFile = writeTestFile();
        File classFile = compileTestFile(javaFile);
        ClassFile cf = ClassFile.read(classFile);
        for (Method m : cf.methods) {
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

    void test(ClassFile cf, Method m, String name, boolean visible) {
        Attribute attr = null;
        Code_attribute cAttr = null;
        RuntimeTypeAnnotations_attribute tAttr = null;
        int index = m.attributes.getIndex(cf.constant_pool, Attribute.Code);
        if (index != -1) {
            attr = m.attributes.get(index);
            assert attr instanceof Code_attribute;
            cAttr = (Code_attribute) attr;
            index = cAttr.attributes.getIndex(cf.constant_pool, name);
            if (index != -1) {
                attr = cAttr.attributes.get(index);
                assert attr instanceof RuntimeTypeAnnotations_attribute;
                tAttr = (RuntimeTypeAnnotations_attribute) attr;
                all += tAttr.annotations.length;
                if (visible)
                    visibles += tAttr.annotations.length;
                else
                    invisibles += tAttr.annotations.length;
            }
        }
    }

    File writeTestFile() throws IOException {
        File f = new File("Test.java");
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
        out.println("import java.lang.annotation.*;");
        out.println("import java.util.*;");
        out.println("class Test { ");
        out.println("  @Target(ElementType.TYPE_USE) @interface A { }");
        out.println("  void test() {");
        out.println("    Object a = new @A String @A [5] @A  [];");
        out.println("    Object b = new @A String @A [5] @A [3];");
        out.println("    Object c = new @A String @A [] @A [] {};");
        out.println("  }");
        out.println("}");
        out.close();
        return f;
    }

    File compileTestFile(File f) {
        int rc = com.sun.tools.javac.Main.compile(new String[] { "-g", f.getPath() });
        if (rc != 0)
            throw new Error("compilation failed. rc=" + rc);
        String path = f.getPath();
        return new File(path.substring(0, path.length() - 5) + ".class");
    }

    void countAnnotations() {
        int expected_visibles = 0, expected_invisibles = 9;
        int expected_all = expected_visibles + expected_invisibles;
        if (expected_all != all) {
            errors++;
            System.err.println("expected " + expected_all + " annotations but found " + all);
        }
        if (expected_visibles != visibles) {
            errors++;
            System.err.println("expected " + expected_visibles + " visibles annotations but found " + visibles);
        }
        if (expected_invisibles != invisibles) {
            errors++;
            System.err.println("expected " + expected_invisibles + " invisibles annotations but found " + invisibles);
        }
    }

    int errors;

    int all;

    int visibles;

    int invisibles;
}
