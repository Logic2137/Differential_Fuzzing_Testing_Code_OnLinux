
package nsk.share;

public class DummyClassLoader extends ClassLoader {

    protected DummyClassLoader() {
    }

    public DummyClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }
}
