

import java.util.*;
import java.io.*;

public class PreemptingClassLoader extends ClassLoader {

    private final Set<String> names = new HashSet<>();
    boolean checkLoaded = true;

    public PreemptingClassLoader(String... names) {
        for (String n : names) this.names.add(n);
    }

    public PreemptingClassLoader(String name, String[] names) {
        super(name, ClassLoader.getSystemClassLoader());
        for (String n : names) this.names.add(n);
    }

    public PreemptingClassLoader(String name, String[] names, boolean cL) {
        super(name, ClassLoader.getSystemClassLoader());
        for (String n : names) this.names.add(n);
        checkLoaded = cL;
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (!names.contains(name)) return super.loadClass(name, resolve);
        Class<?> result = checkLoaded ? findLoadedClass(name) : null;
        if (result == null) {
            String filename = name.replace('.', '/') + ".class";
            try (InputStream data = getResourceAsStream(filename)) {
                if (data == null) throw new ClassNotFoundException();
                try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                    int b;
                    do {
                        b = data.read();
                        if (b >= 0) buffer.write(b);
                    } while (b >= 0);
                    byte[] bytes = buffer.toByteArray();
                    result = defineClass(name, bytes, 0, bytes.length);
                }
            } catch (IOException e) {
                throw new ClassNotFoundException("Error reading class file", e);
            }
        }
        if (resolve) resolveClass(result);
        return result;
    }

}
