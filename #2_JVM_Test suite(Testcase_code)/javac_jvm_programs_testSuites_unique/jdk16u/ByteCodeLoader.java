

package jdk.test.lib;

import java.security.SecureClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ByteCodeLoader extends SecureClassLoader {
    private final Map<String,byte[]> classBytesMap;
    private final Map<String,Class<?>> cache;

    public ByteCodeLoader(Map<String,byte[]> classBytesMap, ClassLoader parent) {
        super(parent);
        this.classBytesMap = classBytesMap;
        cache = new ConcurrentHashMap<>();
    }

    
    public ByteCodeLoader(String className, byte[] byteCode) {
        super();
        classBytesMap = Map.of(className, byteCode);
        cache = new ConcurrentHashMap<>();
    }

    
    public ByteCodeLoader(String className, byte[] byteCode, ClassLoader parent) {
        this(Map.of(className, byteCode), parent);
    }

    private static final Object lock = new Object();

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (classBytesMap.get(name) == null) {
            return super.loadClass(name);
        }
        Class<?> cls = cache.get(name);
        if (cls != null) {
            return cls;
        }
        synchronized (lock) {
            cls = cache.get(name);
            if (cls == null) {
                cls = findClass(name);
                cache.put(name, cls);
            }
        }
        return cls;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] byteCode = classBytesMap.get(name);
        if (byteCode == null) {
            throw new ClassNotFoundException(name);
        }
        return defineClass(name, byteCode, 0, byteCode.length);
    }

    
    public static Class<?> load(String className, byte[] byteCode) throws ClassNotFoundException {
        return new ByteCodeLoader(className, byteCode).loadClass(className);
    }
}
