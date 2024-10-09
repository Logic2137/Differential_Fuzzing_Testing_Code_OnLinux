

package jdk.test.lib;

import java.security.SecureClassLoader;


public class ByteCodeLoader extends SecureClassLoader {
    private final String className;
    private final byte[] byteCode;
    private volatile Class<?> holder;

    
    public ByteCodeLoader(String className, byte[] byteCode) {
        this.className = className;
        this.byteCode = byteCode;
    }

    
    public ByteCodeLoader(String className, byte[] byteCode, ClassLoader parent) {
        super(parent);
        this.className = className;
        this.byteCode = byteCode;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (!name.equals(className)) {
            return super.loadClass(name);
        }
        if (holder == null) {
            synchronized(this) {
                if (holder == null) {
                    holder = findClass(name);
                }
            }
        }
        return holder;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (!name.equals(className)) {
            throw new ClassNotFoundException(name);
        }

        return defineClass(name, byteCode, 0, byteCode.length);
    }

    
    public static Class<?> load(String className, byte[] byteCode) throws ClassNotFoundException {
        return new ByteCodeLoader(className, byteCode).loadClass(className);
    }
}
