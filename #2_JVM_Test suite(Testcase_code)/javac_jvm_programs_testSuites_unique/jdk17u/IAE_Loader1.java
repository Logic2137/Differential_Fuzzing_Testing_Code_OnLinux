
package test;

import java.util.*;
import java.io.*;

public class IAE_Loader1 extends ClassLoader {

    private final Set<String> names = new HashSet<>();

    public IAE_Loader1(String name, String[] names) {
        super(name, ClassLoader.getSystemClassLoader());
        for (String n : names) this.names.add(n);
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (!names.contains(name)) {
            return super.loadClass(name, resolve);
        }
        String filename = name.replace('.', '/') + ".class";
        Class<?> result = null;
        try (InputStream data = getResourceAsStream(filename)) {
            if (data == null) {
                throw new ClassNotFoundException();
            }
            try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                byte[] bytes = data.readAllBytes();
                result = defineClass(name, bytes, 0, bytes.length);
            }
        } catch (IOException e) {
            throw new ClassNotFoundException("Error reading class file", e);
        }
        if (resolve)
            resolveClass(result);
        return result;
    }
}
