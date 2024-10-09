



import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class StaticInterfaceMethodInWayOfDefault {
    public interface A_v1 {
    }

    public interface A_v2 {
        default void m() {
            System.err.println("A.m() called");
        }
    }

    public interface B  extends A_v1 {
        static void m() {
            System.err.println("B.m() called");
        }
    }

    public interface C_v1 extends B {
        default void m() {
            System.err.println("C.m() called");
        }
    }

    public interface C_v2 extends B {
    }

    public static class TestTask implements Callable<String> {
        @Override
        public String call() {
            try {
                Method m = C_v1.class.getMethod("m", (Class<?>[])null);
                return  m.getDeclaringClass().getSimpleName();
            } catch (NoSuchMethodException e) {
                System.err.println("Couldn't find method");
                return "ERROR";
            }
        }
    }

    public static void main(String[] args) throws Exception {
        int errors = 0;
        Callable<String> v1Task = new TestTask();

        ClassLoader v2Loader = new V2ClassLoader(
            StaticInterfaceMethodInWayOfDefault.class.getClassLoader());
        Callable<String> v2Task = (Callable<String>) Class.forName(
            TestTask.class.getName(),
            true,
            v2Loader).newInstance();

        System.err.println("Running using _v1 classes:");
        String res = v1Task.call();
        if(!res.equals("C_v1")) {
            System.err.println("Got wrong method, expecting C_v1, got: " + res);
            errors++;
        }

        System.err.println("Running using _v2 classes:");
        res = v2Task.call();
        if(!res.equals("A_v1")) {
            System.err.println("Got wrong method, expecting A_v1, got: " + res);
            errors++;
        }

        if (errors != 0)
            throw new RuntimeException("Errors found, check log for details");
    }

    
    static class V2ClassLoader extends ClassLoader {
        V2ClassLoader(ClassLoader parent) {
            super(parent);
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
            if (name.indexOf('.') < 0) { 
                synchronized (getClassLoadingLock(name)) {
                    
                    Class<?> c = findLoadedClass(name);
                    if (c == null) {
                        c = findClass(name);
                    }
                    if (resolve) {
                        resolveClass(c);
                    }
                    return c;
                }
            }
            else { 
                return super.loadClass(name, resolve);
            }
        }

        @Override
        protected Class<?> findClass(String name)
            throws ClassNotFoundException {
            
            if (name.endsWith("_v1")) {
                String altName = name.substring(0, name.length() - 3) + "_v2";
                String altPath = altName.replace('.', '/').concat(".class");
                try (InputStream is = getResourceAsStream(altPath)) {
                    if (is != null) {
                        byte[] bytes = is.readAllBytes();
                        
                        for (int i = 0; i < bytes.length - 2; i++) {
                            if (bytes[i] == '_' &&
                                bytes[i + 1] == 'v' &&
                                bytes[i + 2] == '2') {
                                bytes[i + 2] = '1';
                            }
                        }
                        return defineClass(name, bytes, 0, bytes.length);
                    }
                    else {
                        throw new ClassNotFoundException(name);
                    }
                }
                catch (IOException e) {
                    throw new ClassNotFoundException(name, e);
                }
            }
            else { 
                String path = name.replace('.', '/').concat(".class");
                try (InputStream is = getResourceAsStream(path)) {
                    if (is != null) {
                        byte[] bytes = is.readAllBytes();
                        return defineClass(name, bytes, 0, bytes.length);
                    }
                    else {
                        throw new ClassNotFoundException(name);
                    }
                }
                catch (IOException e) {
                    throw new ClassNotFoundException(name, e);
                }
            }
        }
    }
}
