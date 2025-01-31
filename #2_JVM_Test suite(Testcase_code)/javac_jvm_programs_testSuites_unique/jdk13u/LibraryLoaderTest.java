import java.io.File;
import java.util.Set;
import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class LibraryLoaderTest {

    private static final String[][] mletInfo = { { "testDomain:type=MLet,index=0", "UseNativeLib0.html" }, { "testDomain:type=MLet,index=1", "UseNativeLib1.html" } };

    public static void main(String[] args) throws Exception {
        String osName = System.getProperty("os.name");
        System.out.println("os.name=" + osName);
        String osArch = System.getProperty("os.arch");
        System.out.println("os.name=" + osArch);
        if ((!(osName.equals("SunOS") && osArch.equals("sparc"))) && (!(osName.startsWith("Windows") && osArch.equals("x86")))) {
            System.out.println("This test runs only on Solaris/SPARC and Windows/x86 platforms");
            System.out.println("Bye! Bye!");
            return;
        }
        String libPath = System.getProperty("java.library.path");
        System.out.println("java.library.path=" + libPath);
        String testSrc = System.getProperty("test.src");
        System.out.println("test.src=" + testSrc);
        String workingDir = System.getProperty("user.dir");
        System.out.println("user.dir=" + workingDir);
        String urlCodebase;
        if (testSrc.startsWith("/")) {
            urlCodebase = "file:" + testSrc.replace(File.separatorChar, '/') + "/";
        } else {
            urlCodebase = "file:/" + testSrc.replace(File.separatorChar, '/') + "/";
        }
        MBeanServer server = MBeanServerFactory.newMBeanServer();
        for (int i = 0; i < mletInfo.length; i++) {
            ObjectName mlet = new ObjectName(mletInfo[i][0]);
            server.createMBean("javax.management.loading.MLet", mlet);
            System.out.println("MLet = " + mlet);
            String libraryDirectory = (String) server.getAttribute(mlet, "LibraryDirectory");
            System.out.println("Old Library Directory = " + libraryDirectory);
            Attribute attribute = new Attribute("LibraryDirectory", workingDir);
            server.setAttribute(mlet, attribute);
            libraryDirectory = (String) server.getAttribute(mlet, "LibraryDirectory");
            System.out.println("New Library Directory = " + libraryDirectory);
            String mletURL = urlCodebase + mletInfo[i][1];
            System.out.println("MLet URL = " + mletURL);
            Object[] params = new Object[] { mletURL };
            String[] signature = new String[] { "java.lang.String" };
            Object[] res = ((Set<?>) server.invoke(mlet, "getMBeansFromURL", params, signature)).toArray();
            for (int j = 0; j < res.length; j++) {
                if (res[j] instanceof Throwable) {
                    ((Throwable) res[j]).printStackTrace(System.out);
                    throw new Exception("Failed to load the MBean #" + j, (Throwable) res[j]);
                }
                Object result = null;
                try {
                    ObjectName mbean = ((ObjectInstance) res[j]).getObjectName();
                    result = server.getAttribute(mbean, "Random");
                    System.out.println("MBean #" + j + " = " + mbean);
                    System.out.println("Random number = " + result);
                } catch (ReflectionException e) {
                    e.getTargetException().printStackTrace(System.out);
                    throw new Exception("A ReflectionException " + "occured when attempting to invoke " + "a native library based operation.", e.getTargetException());
                }
            }
        }
    }
}
