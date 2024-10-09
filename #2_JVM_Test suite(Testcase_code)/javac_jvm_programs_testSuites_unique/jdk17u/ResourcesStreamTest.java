import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourcesStreamTest {

    public static void main(String[] args) throws Exception {
        testSuccess();
        testFailure();
    }

    public static void testSuccess() throws Exception {
        try {
            ClassLoader cl = new FailingClassLoader();
            Stream<URL> stream = cl.resources("the name");
            stream.forEach(System.out::println);
            throw new Exception("expected UncheckedIOException not thrown");
        } catch (UncheckedIOException uio) {
            String causeMessage = uio.getCause().getMessage();
            if (!"the name".equals(causeMessage))
                throw new Exception("unexpected cause message: " + causeMessage);
        }
    }

    public static void testFailure() throws Exception {
        ClassLoader cl = new SuccessClassLoader();
        long count = cl.resources("the name").count();
        if (count != 1)
            throw new Exception("expected resource is null or empty");
        cl.resources("the name").filter(url -> "file:/somefile".equals(url.toExternalForm())).findFirst().orElseThrow(() -> new Exception("correct URL not found"));
    }

    public static class SuccessClassLoader extends ClassLoader {

        @Override
        public Enumeration<URL> getResources(String name) throws IOException {
            URL url = new URL("file:/somefile");
            return Collections.enumeration(Collections.singleton(url));
        }
    }

    public static class FailingClassLoader extends ClassLoader {

        @Override
        public Enumeration<URL> getResources(String name) throws IOException {
            throw new IOException(name);
        }
    }
}
