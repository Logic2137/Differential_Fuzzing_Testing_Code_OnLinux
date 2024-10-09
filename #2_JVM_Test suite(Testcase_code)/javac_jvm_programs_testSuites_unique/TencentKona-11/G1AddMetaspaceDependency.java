



import java.io.InputStream;

public class G1AddMetaspaceDependency {

  static byte[] getClassBytes(String name) {
    byte[] b = null;
    try (InputStream is = ClassLoader.getSystemResourceAsStream(name)) {
      byte[] tmp = new byte[is.available()];
      is.read(tmp);
      b = tmp;
    } finally {
      if (b == null) {
        throw new RuntimeException("Unable to load class file");
      }
      return b;
    }
  }

  static final String a_name = G1AddMetaspaceDependency.class.getName() + "$A";
  static final String b_name = G1AddMetaspaceDependency.class.getName() + "$B";

  public static void main(String... args) throws Exception {
    final byte[] a_bytes = getClassBytes(a_name + ".class");
    final byte[] b_bytes = getClassBytes(b_name + ".class");

    for (int i = 0; i < 1000; i += 1) {
      runTest(a_bytes, b_bytes);
    }
  }

  static class Loader extends ClassLoader {
    private final String myClass;
    private final byte[] myBytes;
    private final String friendClass;
    private final ClassLoader friendLoader;

    Loader(String myClass, byte[] myBytes,
           String friendClass, ClassLoader friendLoader) {
      this.myClass = myClass;
      this.myBytes = myBytes;
      this.friendClass = friendClass;
      this.friendLoader = friendLoader;
    }

    Loader(String myClass, byte[] myBytes) {
      this(myClass, myBytes, null, null);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
      Class<?> c = findLoadedClass(name);
      if (c != null) {
        return c;
      }

      if (name.equals(friendClass)) {
        return friendLoader.loadClass(name);
      }

      if (name.equals(myClass)) {
        c = defineClass(name, myBytes, 0, myBytes.length);
        resolveClass(c);
        return c;
      }

      return findSystemClass(name);
    }

  }

  private static void runTest(final byte[] a_bytes, final byte[] b_bytes) throws Exception {
    Loader a_loader = new Loader(a_name, a_bytes);
    Loader b_loader = new Loader(b_name, b_bytes, a_name, a_loader);
    Loader c_loader = new Loader(b_name, b_bytes, a_name, a_loader);
    Loader d_loader = new Loader(b_name, b_bytes, a_name, a_loader);
    Loader e_loader = new Loader(b_name, b_bytes, a_name, a_loader);
    Loader f_loader = new Loader(b_name, b_bytes, a_name, a_loader);
    Loader g_loader = new Loader(b_name, b_bytes, a_name, a_loader);

    Class<?> c;
    c = b_loader.loadClass(b_name);
    c = c_loader.loadClass(b_name);
    c = d_loader.loadClass(b_name);
    c = e_loader.loadClass(b_name);
    c = f_loader.loadClass(b_name);
    c = g_loader.loadClass(b_name);
  }
  public class A {
  }
  class B extends A {
  }
}
