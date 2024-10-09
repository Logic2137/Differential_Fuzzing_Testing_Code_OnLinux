

package jdk.jfr.event.oldobject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public final class TestClassLoader extends ClassLoader {

    public static final class TestClass0000000 {
        public static final byte[] oneByte = new byte[1];
    }

    static byte[] classByteCode = readTestClassBytes();
    private static int classIdCounter;

    TestClassLoader() {
        super(TestClassLoader.class.getClassLoader());
    }

    public List<Class<?>> loadClasses(int count) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String className = "jdk.jfr.event.oldobject.TestClassLoader$";
            className += "TestClass" + String.format("%07d", classIdCounter++);
            Class<?> clazz = Class.forName(className, true, this);
            classes.add(clazz);
        }
        return classes;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        
        if (!name.contains("TestClass")) {
            return super.loadClass(name, resolve);
        }
        String[] classNameParts = name.split("\\$");
        String newName = classNameParts[1];
        String oldName = "TestClass0000000";
        if (oldName.length() != newName.length()) {
            throw new AssertionError("String lengths don't match. Unable to replace class name");
        }
        byte[] newBytes = classByteCode.clone();
        replaceBytes(newBytes, oldName.getBytes(), newName.getBytes());
        Class<?> c = defineClass(name, newBytes, 0, newBytes.length);
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }

    static void replaceBytes(byte[] bytes, byte[] find, byte[] replacement) {
        for (int index = 0; index < bytes.length - find.length; index++) {
            if (matches(bytes, index, find)) {
                replace(bytes, index, replacement);
            }
        }
    }

    private static void replace(byte[] bytes, int startIndex, byte[] replacement) {
        for (int index = 0; index < replacement.length; index++) {
            bytes[startIndex + index] = replacement[index];
        }
    }

    private static boolean matches(byte[] bytes, int startIndex, byte[] matching) {
        for (int i = 0; i < matching.length; i++) {
            if (bytes[startIndex + i] != matching[i]) {
                return false;
            }
        }
        return true;
    }

    private static byte[] readTestClassBytes() {
        try {
            String classFileName = "jdk/jfr/event/oldobject/TestClassLoader$TestClass0000000.class";
            InputStream is = TestClassLoader.class.getClassLoader().getResourceAsStream(classFileName);
            if (is == null) {
                throw new RuntimeException("Culd not find class file " + classFileName);
            }
            byte[] b = is.readAllBytes();
            is.close();
            return b;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException(ioe);
        }
    }
}
