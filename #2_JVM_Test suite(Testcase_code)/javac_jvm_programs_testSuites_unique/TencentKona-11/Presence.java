

import java.io.*;
import java.lang.annotation.ElementType;

import com.sun.tools.classfile.*;



public class Presence {
    public static void main(String[] args) throws Exception {
        new Presence().run();
    }

    public void run() throws Exception {
        File javaFile = writeTestFile();
        File classFile = compileTestFile(javaFile);

        ClassFile cf = ClassFile.read(classFile);
        test(cf);
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

    void test(ClassFile cf) {
        test(cf, Attribute.RuntimeVisibleTypeAnnotations, true);
        test(cf, Attribute.RuntimeInvisibleTypeAnnotations, false);
    }

    void test(ClassFile cf, Method m) {
        test(cf, m, Attribute.RuntimeVisibleTypeAnnotations, true);
        test(cf, m, Attribute.RuntimeInvisibleTypeAnnotations, false);
    }

    void test(ClassFile cf, Field m) {
        test(cf, m, Attribute.RuntimeVisibleTypeAnnotations, true);
        test(cf, m, Attribute.RuntimeInvisibleTypeAnnotations, false);
    }

    
    
    void test(ClassFile cf, String name, boolean visible) {
        int index = cf.attributes.getIndex(cf.constant_pool, name);
        if (index != -1) {
            Attribute attr = cf.attributes.get(index);
            assert attr instanceof RuntimeTypeAnnotations_attribute;
            RuntimeTypeAnnotations_attribute tAttr = (RuntimeTypeAnnotations_attribute)attr;
            all += tAttr.annotations.length;
            if (visible)
                visibles += tAttr.annotations.length;
            else
                invisibles += tAttr.annotations.length;
        }
    }

    
    
    void test(ClassFile cf, Method m, String name, boolean visible) {
        Attribute attr = null;
        Code_attribute cAttr = null;
        RuntimeTypeAnnotations_attribute tAttr = null;

        
        int index = m.attributes.getIndex(cf.constant_pool, name);
        if (index != -1) {
            attr = m.attributes.get(index);
            assert attr instanceof RuntimeTypeAnnotations_attribute;
            tAttr = (RuntimeTypeAnnotations_attribute)attr;
            all += tAttr.annotations.length;
            if (visible)
                visibles += tAttr.annotations.length;
            else
                invisibles += tAttr.annotations.length;
        }
        
        index = m.attributes.getIndex(cf.constant_pool, Attribute.Code);
        if(index!= -1) {
            attr = m.attributes.get(index);
            assert attr instanceof Code_attribute;
            cAttr = (Code_attribute)attr;
            index = cAttr.attributes.getIndex(cf.constant_pool, name);
            if(index!= -1) {
                attr = cAttr.attributes.get(index);
                assert attr instanceof RuntimeTypeAnnotations_attribute;
                tAttr = (RuntimeTypeAnnotations_attribute)attr;
                all += tAttr.annotations.length;
                if (visible)
                    visibles += tAttr.annotations.length;
                else
                    invisibles += tAttr.annotations.length;
               }
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
        File f = new File("TestPresence.java");
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
        out.println("import java.util.*;");
        out.println("import java.lang.annotation.*;");

        out.println("class TestPresence<@TestPresence.A T extends @TestPresence.A List<@TestPresence.A String>> { ");
        out.println("  @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})");
        out.println("  @interface A { }");

        out.println("  Map<@A String, Map<@A String, @A String>> f1;");

        out.println("  <@A TM extends @A List<@A String>>");
        out.println("  Map<@A String, @A List<@A String>>");
        out.println("  method(@A TestPresence<T> this, List<@A String> @A [] param1, String @A [] @A ... param2)");
        out.println("  throws @A Exception {");
        out.println("    @A String lc1 = null;");
        out.println("    @A List<@A String> lc2 = null;");
        out.println("    @A String @A [] [] @A[] lc3 = null;");
        out.println("    List<? extends @A List<@A String>> lc4 = null;");
        out.println("    Object lc5 = (@A List<@A String>) null;");
        out.println("    boolean lc6 = lc1 instanceof @A String;");
        out.println("    boolean lc7 = lc5 instanceof @A String @A [] @A [];");
        out.println("    new @A ArrayList<@A String>();");
        out.println("    Object lc8 = new @A String @A [4];");
        out.println("    try {");
        out.println("      Object lc10 = int.class;");
        out.println("    } catch (@A Exception e) { e.toString(); }");
        out.println("    return null;");
        out.println("  }");
        out.println("  void vararg1(String @A ... t) { } ");
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
        int expected_visibles = 0, expected_invisibles = 38;
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
