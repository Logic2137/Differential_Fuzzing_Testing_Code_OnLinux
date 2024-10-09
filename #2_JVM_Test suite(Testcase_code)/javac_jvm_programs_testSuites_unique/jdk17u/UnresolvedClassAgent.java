import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;

class MyUnresolvedClass {

    static void bar() {
    }
}

class MyRedefinedClass {

    static void foo() {
        MyUnresolvedClass.bar();
    }
}

public class UnresolvedClassAgent {

    public static void main(String... args) {
    }

    public static void premain(String args, Instrumentation inst) throws Exception {
        try {
            MyRedefinedClass.foo();
        } catch (NoClassDefFoundError err) {
            System.out.println("NoClassDefFoundError (expected)");
        }
        File f = new File(System.getProperty("test.classes"), "MyRedefinedClass.class");
        byte[] buf = new byte[(int) f.length()];
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            dis.readFully(buf);
        }
        ClassDefinition cd = new ClassDefinition(MyRedefinedClass.class, buf);
        inst.redefineClasses(new ClassDefinition[] { cd });
        try {
            MyRedefinedClass.foo();
        } catch (NoClassDefFoundError err) {
            System.out.println("NoClassDefFoundError (expected again)");
        }
    }
}
