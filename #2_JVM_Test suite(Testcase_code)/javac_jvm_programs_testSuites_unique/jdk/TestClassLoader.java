



public class TestClassLoader extends ClassLoader {
    static boolean called = false;
    ClassLoader parent;
    public TestClassLoader(ClassLoader parent) {
        super(parent);
        this.parent = parent;
    }

    public Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException
    {
        called = true;
        System.out.println("TestClassLoader: loadClass(\"" + name + "\", " + resolve + ")");
        return (super.loadClass(name, resolve));
    }
}
