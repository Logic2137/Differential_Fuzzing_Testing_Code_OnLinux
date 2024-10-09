import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.security.AllPermission;
import java.security.Permissions;
import java.security.ProtectionDomain;

public class CustomSystemClassLoader extends ClassLoader {

    Class<?> finderClass = null;

    public CustomSystemClassLoader() {
        super();
    }

    public CustomSystemClassLoader(ClassLoader parent) {
        super(parent);
    }

    private Class<?> defineFinderClass(String name) throws ClassNotFoundException {
        final Object obj = getClassLoadingLock(name);
        synchronized (obj) {
            if (finderClass != null)
                return finderClass;
            URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation();
            File file = new File(url.getPath(), name + ".class");
            if (file.canRead()) {
                try {
                    byte[] b = Files.readAllBytes(file.toPath());
                    Permissions perms = new Permissions();
                    perms.add(new AllPermission());
                    finderClass = defineClass(name, b, 0, b.length, new ProtectionDomain(this.getClass().getProtectionDomain().getCodeSource(), perms));
                    System.out.println("Loaded " + name);
                    return finderClass;
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    throw new ClassNotFoundException(name, ex);
                }
            } else {
                throw new ClassNotFoundException(name, new IOException(file.toPath() + ": can't read"));
            }
        }
    }

    @Override
    public synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (name.equals("BaseLoggerFinder")) {
            Class<?> c = defineFinderClass(name);
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
        return super.loadClass(name, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.equals("BaseLoggerFinder")) {
            return defineFinderClass(name);
        }
        return super.findClass(name);
    }
}
