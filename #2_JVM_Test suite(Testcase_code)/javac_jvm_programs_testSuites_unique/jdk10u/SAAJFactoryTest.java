

import javax.xml.soap.MessageFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class SAAJFactoryTest {

    
    static String scenario;

    
    static Path providersDir = Paths.get(System.getProperty("test.classes"), "META-INF", "services");
    static Path providersFile = providersDir.resolve("javax.xml.soap.MessageFactory");

    
    static Path jdkDir = Paths.get(System.getProperty("java.home"), "conf");
    static Path jdkFile = jdkDir.resolve("jaxm.properties");

    
    static String policy = System.getProperty("test.src", ".") + File.separator + "test.policy";


    protected MessageFactory factory() throws Throwable {
        try {
            MessageFactory factory = MessageFactory.newInstance();
            System.out.println("     TEST: factory class = [" + factory.getClass().getName() + "]\n");
            return factory;
        } catch (Throwable t) {
            System.out.println("     TEST: Throwable [" + t.getClass().getName() + "] thrown.\n");
            t.printStackTrace();
            throw t;
        }
    }

    protected void test(String[] args) {
        if (args.length < 5) throw new IllegalArgumentException("Incorrect test setup. Required 5 arguments: \n" +
                "   1. expected factory class (if any)\n" +
                "   2. expected \n" +
                "   3. scenario name\n" +
                "   4. jdk/conf configured provider class name\n" +
                "   5. service loader provider class name");

        scenario = args[2]; 
        prepare(args[3], args[4]); 

        try {
            MessageFactory factory = factory();
            assertTrue(factory != null, "No factory found.");
            String className = factory.getClass().getName();
            assertTrue(args[0].equals(className), "Incorrect factory: [" + className +
                    "], Expected: [" + args[0] + "]");

        } catch (Throwable throwable) {
            String expectedExceptionClass = args[1];
            String throwableClass = throwable.getClass().getName();
            boolean correctException = throwableClass.equals(expectedExceptionClass);
            if (!correctException) {
                throwable.printStackTrace();
            }
            assertTrue(correctException, "Got unexpected exception: [" +
                    throwableClass + "], expected: [" + expectedExceptionClass + "]");
        } finally {
            cleanResource(providersFile);
            cleanResource(providersDir);

            
            
        }
    }

    private void cleanResource(Path resource) {
        try {
            Files.deleteIfExists(resource);
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }

    private void prepare(String propertiesClassName, String providerClassName) {

        try {
            log("providerClassName = " + providerClassName);
            log("propertiesClassName = " + propertiesClassName);

            setupFile(providersFile, providersDir, providerClassName);

            
            

            log(" SETUP OK.");

        } catch (IOException e) {
            log(" SETUP FAILED.");
            e.printStackTrace();
        }
    }

    private void setupFile(Path file, Path dir, String value) throws IOException {
        cleanResource(file);
        if (!"-".equals(value)) {
            log("writing configuration [" + value + "] into file [" + file.toAbsolutePath() + "]");
            Files.createDirectories(dir);
            Files.write(
                    file,
                    value.getBytes(),
                    StandardOpenOption.CREATE);
        }
    }

    private static void assertTrue(boolean condition, String msg) {
        if (!condition) {
            log(" FAILED -  ERROR: " + msg);
            throw new RuntimeException("[" + scenario + "] " + msg);
        } else {
            log(" PASSED.");
        }
    }

    private static void log(String msg) {
        System.out.println("[" + scenario + "] " + msg);
    }


    public static void main(String[] args) {
        
        new SAAJFactoryTest().test(args);

        System.out.println("Policy file: " + policy);
        System.setProperty("java.security.policy", policy);

        System.out.println("Install security manager...");
        System.setSecurityManager(new SecurityManager());

        
        new SAAJFactoryTest().test(args);
    }

}
