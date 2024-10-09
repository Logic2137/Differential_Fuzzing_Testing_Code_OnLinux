



import javax.tools.*;

public class FileManagerGetServiceLoaderTest {

    public static void main(String... args) throws Exception {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fm = compiler.getStandardFileManager(null, null, null);

        
        java.util.ServiceLoader<?> loader = fm.getServiceLoader(StandardLocation.CLASS_PATH,
                                     FileManagerGetServiceLoaderTest.class);
        if (loader == null) {
            throw new AssertionError("Could not obtain service loader");
        }
    }
}
