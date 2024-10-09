

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.lang.System.LoggerFinder;


public class SystemClassLoader extends ClassLoader {

    final public boolean hidesProvider;

    public SystemClassLoader() {
        hidesProvider = Boolean.getBoolean("test.logger.hidesProvider");
    }
    public SystemClassLoader(ClassLoader parent) {
        super(parent);
        hidesProvider = Boolean.getBoolean("test.logger.hidesProvider");
    }

    boolean accept(String name) {
        final boolean res = !name.endsWith(LoggerFinder.class.getName());
        if (res == false) {
            System.out.println("Hiding " + name);
        }
        return res;
    }

    @Override
    public URL getResource(String name) {
        if (hidesProvider && !accept(name)) {
            return null;
        } else {
            return super.getResource(name);
        }
    }

    class Enumerator implements Enumeration<URL> {
        final Enumeration<URL> enumeration;
        volatile URL next;
        Enumerator(Enumeration<URL> enumeration) {
            this.enumeration = enumeration;
        }

        @Override
        public boolean hasMoreElements() {
            if (next != null) return true;
            if (!enumeration.hasMoreElements()) return false;
            if (hidesProvider == false) return true;
            next = enumeration.nextElement();
            if (accept(next.getPath())) return true;
            next = null;
            return hasMoreElements();
        }

        @Override
        public URL nextElement() {
            final URL res = next == null ? enumeration.nextElement() : next;
            next = null;
            if (hidesProvider == false || accept(res.getPath())) return res;
            return nextElement();
        }
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        final Enumeration<URL> enumeration = super.getResources(name);
        return hidesProvider ? new Enumerator(enumeration) : enumeration;
    }



}
