



import java.lang.reflect.*;
import java.io.*;

import javax.tools.*;
import java.nio.charset.StandardCharsets;
import com.sun.tools.javac.api.JavacTaskImpl;

public class ClientCodeWrappersShouldOverrideAllMethodsTest {
    public static void main(String[] args) {
        ClientCodeWrappersShouldOverrideAllMethodsTest clientCodeWrappersShouldOverrideAllMethodsTest = new ClientCodeWrappersShouldOverrideAllMethodsTest();
        clientCodeWrappersShouldOverrideAllMethodsTest.testWrappersForJavaFileManager();
        clientCodeWrappersShouldOverrideAllMethodsTest.testWrappersForStandardJavaFileManager();
    }

    void testWrappersForJavaFileManager() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);
        UserFileManager fileManager = new UserFileManager(standardFileManager);
        JavacTaskImpl task = (JavacTaskImpl)compiler.getTask(null, fileManager, null, null, null, null);
        JavaFileManager wrappedFM = task.getContext().get(JavaFileManager.class);
        checkAllMethodsOverridenInWrapperClass(wrappedFM.getClass(), JavaFileManager.class);
    }

    void testWrappersForStandardJavaFileManager() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);
        UserStandardJavaFileManager fileManager = new UserStandardJavaFileManager(standardFileManager);
        JavacTaskImpl task = (JavacTaskImpl)compiler.getTask(null, fileManager, null, null, null, null);
        JavaFileManager wrappedFM = task.getContext().get(JavaFileManager.class);
        checkAllMethodsOverridenInWrapperClass(wrappedFM.getClass(), StandardJavaFileManager.class);
    }

    void checkAllMethodsOverridenInWrapperClass(Class<?> subClass, Class<?> superClass) {
        Method[] allMethods = subClass.getMethods();
        for (Method m : allMethods) {
            if (m.getDeclaringClass() == superClass) {
                throw new AssertionError(String.format("method %s not overriden by javac provided wrapper class", m.getName()));
            }
        }
    }

    static class UserFileManager extends ForwardingJavaFileManager<JavaFileManager> {
        UserFileManager(JavaFileManager delegate) {
            super(delegate);
        }
    }

    static class UserStandardJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> implements StandardJavaFileManager {
        StandardJavaFileManager delegate;

        UserStandardJavaFileManager(StandardJavaFileManager delegate) {
            super(delegate);
            this.delegate = delegate;
        }

        public Iterable<? extends File> getLocation(Location location) { return delegate.getLocation(location);}

        public void setLocation(Location location, Iterable<? extends File> files) throws IOException {
            delegate.setLocation(location, files);
        }

        public Iterable<? extends JavaFileObject> getJavaFileObjects(String... names) {
            return delegate.getJavaFileObjects(names);
        }

        public Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> names) {
            return delegate.getJavaFileObjectsFromStrings(names);
        }

        public Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> files) {
            return delegate.getJavaFileObjectsFromFiles(files);
        }

        public Iterable<? extends JavaFileObject> getJavaFileObjects(File... files) {
            return delegate.getJavaFileObjects(files);
        }
    }
}
