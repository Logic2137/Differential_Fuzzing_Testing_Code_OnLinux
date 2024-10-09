import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class AnnotationTypeRuntimeAssumptionTest {

    @Retention(RUNTIME)
    @AnnB
    public @interface AnnA_v1 {
    }

    @Retention(CLASS)
    @AnnB
    public @interface AnnA_v2 {
    }

    @Retention(RUNTIME)
    @AnnA_v1
    public @interface AnnB {
    }

    @AnnA_v1
    public static class TestTask implements Runnable {

        @Override
        public void run() {
            AnnA_v1 ann1 = TestTask.class.getDeclaredAnnotation(AnnA_v1.class);
            if (ann1 != null) {
                throw new IllegalStateException("@" + ann1.annotationType().getSimpleName() + " found on: " + TestTask.class.getName() + " should not be visible at runtime");
            }
            AnnA_v1 ann2 = AnnB.class.getDeclaredAnnotation(AnnA_v1.class);
            if (ann2 != null) {
                throw new IllegalStateException("@" + ann2.annotationType().getSimpleName() + " found on: " + AnnB.class.getName() + " should not be visible at runtime");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ClassLoader altLoader = new AltClassLoader(AnnotationTypeRuntimeAssumptionTest.class.getClassLoader());
        Runnable altTask = (Runnable) Class.forName(TestTask.class.getName(), true, altLoader).newInstance();
        altTask.run();
    }

    static class AltClassLoader extends ClassLoader {

        AltClassLoader(ClassLoader parent) {
            super(parent);
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
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
            } else {
                return super.loadClass(name, resolve);
            }
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if (name.endsWith("_v1")) {
                String altName = name.substring(0, name.length() - 3) + "_v2";
                String altPath = altName.replace('.', '/').concat(".class");
                try (InputStream is = getResourceAsStream(altPath)) {
                    if (is != null) {
                        byte[] bytes = is.readAllBytes();
                        for (int i = 0; i < bytes.length - 2; i++) {
                            if (bytes[i] == '_' && bytes[i + 1] == 'v' && bytes[i + 2] == '2') {
                                bytes[i + 2] = '1';
                            }
                        }
                        return defineClass(name, bytes, 0, bytes.length);
                    } else {
                        throw new ClassNotFoundException(name);
                    }
                } catch (IOException e) {
                    throw new ClassNotFoundException(name, e);
                }
            } else {
                String path = name.replace('.', '/').concat(".class");
                try (InputStream is = getResourceAsStream(path)) {
                    if (is != null) {
                        byte[] bytes = is.readAllBytes();
                        return defineClass(name, bytes, 0, bytes.length);
                    } else {
                        throw new ClassNotFoundException(name);
                    }
                } catch (IOException e) {
                    throw new ClassNotFoundException(name, e);
                }
            }
        }
    }
}
