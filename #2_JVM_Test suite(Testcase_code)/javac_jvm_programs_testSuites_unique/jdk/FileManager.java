

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import com.sun.tools.javac.api.WrappingJavaFileManager;


public class FileManager
        extends WrappingJavaFileManager<StandardJavaFileManager>
        implements StandardJavaFileManager {
    private static final String CANT_READ = "cantRead:";
    private static final String CANT_WRITE = "cantWrite:";

    private Pattern cantRead;
    private Pattern cantWrite;

    public FileManager(StandardJavaFileManager fm, List<String> opts) {
        super(fm);
        for (String opt: opts) {
            if (opt.startsWith(CANT_READ))
                cantRead = Pattern.compile(opt.substring(CANT_READ.length()));
            else if (opt.startsWith(CANT_WRITE))
                cantWrite = Pattern.compile(opt.substring(CANT_WRITE.length()));
            else
                throw new IllegalArgumentException(opt);
        }
    }

    @Override
    protected JavaFileObject wrap(JavaFileObject fo) {
        return (fo == null) ? null : new WrappedFileObject(fo);
    }

    @Override
    protected JavaFileObject unwrap(JavaFileObject fo) {
        if (fo instanceof WrappedFileObject)
            return ((WrappedFileObject) fo).delegate;
        else
            return fo;
    }

    public Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> files) {
        return wrap2(fileManager.getJavaFileObjectsFromFiles(files));
    }

    public Iterable<? extends JavaFileObject> getJavaFileObjects(File... files) {
        return wrap2(fileManager.getJavaFileObjects(files));
    }

    public Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> names) {
        return wrap2(fileManager.getJavaFileObjectsFromStrings(names));
    }

    public Iterable<? extends JavaFileObject> getJavaFileObjects(String... names) {
        return wrap2(fileManager.getJavaFileObjects(names));
    }

    
    protected Iterable<JavaFileObject> wrap2(Iterable<? extends JavaFileObject> fileObjects) {
        List<JavaFileObject> mapped = new ArrayList<JavaFileObject>();
        for (JavaFileObject fileObject : fileObjects)
            mapped.add(wrap(fileObject));
        return Collections.unmodifiableList(mapped);
    }

    public void setLocation(Location location, Iterable<? extends File> path) throws IOException {
        fileManager.setLocation(location, path);
    }

    public Iterable<? extends File> getLocation(Location location) {
        return fileManager.getLocation(location);
    }

    class WrappedFileObject implements JavaFileObject {
        WrappedFileObject(JavaFileObject fileObject) {
            delegate = Objects.requireNonNull(fileObject);
        }

        public Kind getKind() {
            return delegate.getKind();
        }

        public boolean isNameCompatible(String simpleName, Kind kind) {
            return delegate.isNameCompatible(simpleName, kind);
        }

        public NestingKind getNestingKind() {
            return delegate.getNestingKind();
        }

        public Modifier getAccessLevel() {
            return delegate.getAccessLevel();
        }

        public URI toUri() {
            return delegate.toUri();
        }

        public String getName() {
            return delegate.getName();
        }

        public InputStream openInputStream() throws IOException {
            checkRead();
            return delegate.openInputStream();
        }

        public OutputStream openOutputStream() throws IOException {
            checkWrite();
            return delegate.openOutputStream();
        }

        public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
            checkRead();
            return delegate.openReader(ignoreEncodingErrors);
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            checkRead();
            return delegate.getCharContent(ignoreEncodingErrors);
        }

        public Writer openWriter() throws IOException {
            checkWrite();
            return delegate.openWriter();
        }

        public long getLastModified() {
            return delegate.getLastModified();
        }

        public boolean delete() {
            return delegate.delete();
        }

        void checkRead() throws IOException {
            String canonName = getName().replace(File.separatorChar, '/');
            if (cantRead != null && cantRead.matcher(canonName).matches())
                throw new IOException("FileManager: Can't read");
        }

        void checkWrite() throws IOException {
            String canonName = getName().replace(File.separatorChar, '/');
            if (cantWrite != null && cantWrite.matcher(canonName).matches())
                throw new IOException("FileManager: Can't write");
        }

        JavaFileObject delegate;
    }
}
