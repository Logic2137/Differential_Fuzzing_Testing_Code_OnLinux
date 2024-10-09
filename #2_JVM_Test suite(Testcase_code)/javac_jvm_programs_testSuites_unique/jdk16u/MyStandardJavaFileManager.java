

import java.io.File;
import java.io.IOException;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;


class MyStandardJavaFileManager
        extends ForwardingJavaFileManager<StandardJavaFileManager>
        implements StandardJavaFileManager {
    MyStandardJavaFileManager(StandardJavaFileManager delegate) {
        super(delegate);
    }

    @Override
    public Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> files) {
        return fileManager.getJavaFileObjectsFromFiles(files);
    }

    @Override
    public Iterable<? extends JavaFileObject> getJavaFileObjects(File... files) {
        return fileManager.getJavaFileObjects(files);
    }

    @Override
    public Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> names) {
        return fileManager.getJavaFileObjectsFromStrings(names);
    }

    @Override
    public Iterable<? extends JavaFileObject> getJavaFileObjects(String... names) {
        return fileManager.getJavaFileObjects(names);
    }

    @Override
    public void setLocation(JavaFileManager.Location location, Iterable<? extends File> files) throws IOException {
        fileManager.setLocation(location, files);
    }

    @Override
    public Iterable<? extends File> getLocation(JavaFileManager.Location location) {
        return fileManager.getLocation(location);
    }
}
