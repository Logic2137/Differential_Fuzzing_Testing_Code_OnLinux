import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public final class TypoInBeanDescription {

    private static final String[] typos = { "&amp;", "&lt;", "&gt;", "&quot;" };

    public static void main(final String[] args) throws IOException {
        FileSystem fs = FileSystems.getFileSystem(URI.create("jrt:/"));
        Files.walkFileTree(fs.getPath("/modules/java.desktop"), new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                file = file.subpath(2, file.getNameCount());
                String name = file.toString();
                if (name.endsWith(".class")) {
                    name = name.substring(0, name.indexOf(".")).replace('/', '.');
                    final Class<?> type;
                    try {
                        type = Class.forName(name);
                    } catch (Throwable e) {
                        return FileVisitResult.CONTINUE;
                    }
                    final BeanInfo beanInfo;
                    try {
                        beanInfo = Introspector.getBeanInfo(type);
                    } catch (IntrospectionException e) {
                        return FileVisitResult.CONTINUE;
                    }
                    test(beanInfo);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void test(final BeanInfo beanInfo) {
        for (var pd : beanInfo.getPropertyDescriptors()) {
            String d = pd.getShortDescription();
            String n = pd.getName();
            String dn = pd.getDisplayName();
            for (String typo : typos) {
                if (d.contains(typo) || n.contains(typo) || dn.contains(typo)) {
                    throw new RuntimeException("Wrong name: " + beanInfo.getBeanDescriptor());
                }
            }
        }
    }
}
