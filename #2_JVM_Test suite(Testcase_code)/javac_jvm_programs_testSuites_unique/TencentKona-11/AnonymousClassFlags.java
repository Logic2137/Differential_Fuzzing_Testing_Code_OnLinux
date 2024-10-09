



import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.tools.classfile.*;
import static com.sun.tools.classfile.AccessFlags.*;

public class AnonymousClassFlags {
    public static void main(String[] args) throws Exception {
        new AnonymousClassFlags().test(System.getProperty("test.classes", "."));
    }

    
    private static Map<String, Integer> anonClasses = new LinkedHashMap<>();

    

    static Object o1 = new Object() {
        { anonClasses.put(getClass().getName(), 0); }
    };

    static void staticMethod() {
        Object o2 = new Object() {
            { anonClasses.put(getClass().getName(), 0); }
        };
    }

    static {
        staticMethod();

        Object o3 = new Object() {
            { anonClasses.put(getClass().getName(), 0); }
        };
    }

    Object o4 = new Object() {
        { anonClasses.put(getClass().getName(), 0); }
    };

    void instanceMethod() {
        Object o5 = new Object() {
            { anonClasses.put(getClass().getName(), 0); }
        };
    }

    {
        instanceMethod();

        Object o6 = new Object() {
            { anonClasses.put(getClass().getName(), 0); }
        };
    }

    

    void test(String classesDir) throws Exception {
        staticMethod();
        instanceMethod();

        Path outerFile = Paths.get(classesDir, getClass().getName() + ".class");
        ClassFile outerClass = ClassFile.read(outerFile);
        for (Map.Entry<String,Integer> entry : anonClasses.entrySet()) {
            Path innerFile = Paths.get(classesDir, entry.getKey() + ".class");
            ClassFile innerClass = ClassFile.read(innerFile);
            String name = entry.getKey();
            int expected = entry.getValue();
            assertInnerFlags(outerClass, name, expected);
            assertClassFlags(innerClass, name, expected);
            assertInnerFlags(innerClass, name, expected);
        }
    }

    static void assertClassFlags(ClassFile classFile, String name, int expected) {
        int mask = ACC_PUBLIC | ACC_FINAL | ACC_INTERFACE | ACC_ABSTRACT |
                   ACC_SYNTHETIC | ACC_ANNOTATION | ACC_ENUM;
        int classExpected = (expected & mask) | ACC_SUPER;
        int classActual = classFile.access_flags.flags;
        if (classActual != classExpected) {
            throw new AssertionError("Incorrect access_flags for class " + name +
                                     ": expected=" + classExpected + ", actual=" + classActual);
        }

    }

    static void assertInnerFlags(ClassFile classFile, String name, int expected) throws ConstantPoolException {
        int innerActual = lookupInnerFlags(classFile, name).flags;
        if (innerActual != expected) {
            throw new AssertionError("Incorrect inner_class_access_flags for class " + name +
                                     " in class " + classFile.getName() +
                                     ": expected=" + expected + ", actual=" + innerActual);
        }
    }

    private static AccessFlags lookupInnerFlags(ClassFile classFile, String innerName) throws ConstantPoolException {
        InnerClasses_attribute inners = (InnerClasses_attribute) classFile.getAttribute("InnerClasses");
        if (inners == null) {
            throw new AssertionError("InnerClasses attribute missing in class " + classFile.getName());
        }
        for (InnerClasses_attribute.Info info : inners.classes) {
            String entryName = info.getInnerClassInfo(classFile.constant_pool).getName();
            if (innerName.equals(entryName)) {
                return info.inner_class_access_flags;
            }
        }
        throw new AssertionError("No InnerClasses entry in class " + classFile.getName() + " for class " + innerName);
    }

}
