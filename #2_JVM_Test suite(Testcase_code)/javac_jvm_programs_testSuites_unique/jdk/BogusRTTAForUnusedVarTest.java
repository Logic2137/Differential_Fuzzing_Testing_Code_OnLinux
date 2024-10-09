



import com.sun.tools.classfile.*;

import java.io.File;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.sun.tools.classfile.Attribute;
import com.sun.tools.classfile.RuntimeVisibleTypeAnnotations_attribute;
import com.sun.tools.classfile.TypeAnnotation;
import com.sun.tools.classfile.TypeAnnotation.Position;
import com.sun.tools.javac.util.Assert;

public class BogusRTTAForUnusedVarTest {

    class Foo {
        void something() {
            {
                @MyAnno Object o = new Object();
            }
        }
    }

    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnno {}

    public static void main(String args[]) throws Throwable {
        new BogusRTTAForUnusedVarTest().run();
    }

    void run() throws Throwable {
        checkRTTA();
    }

    void checkRTTA() throws Throwable {
        File testClasses = new File(System.getProperty("test.classes"));
        File file = new File(testClasses,
                BogusRTTAForUnusedVarTest.class.getName() + "$Foo.class");
        ClassFile classFile = ClassFile.read(file);
        for (Method m : classFile.methods) {
            if (m.getName(classFile.constant_pool).equals("something")) {
                for (Attribute a : m.attributes) {
                    if (a.getName(classFile.constant_pool).equals("Code")) {
                        Code_attribute code = (Code_attribute)a;
                        for (Attribute codeAttrs : code.attributes) {
                            if (codeAttrs.getName(classFile.constant_pool).equals("RuntimeVisibleTypeAnnotations")) {
                                throw new AssertionError("no RuntimeVisibleTypeAnnotations attribute should have been generated in this case");
                            }
                        }
                    }
                }
            }
        }
    }
}
