import java.io.File;
import java.util.Locale;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.loading.MLet;

public class MletParserLocaleTest {

    public static void main(String[] args) throws Exception {
        boolean error = false;
        System.out.println("Create the MBean server");
        MBeanServer mbs = MBeanServerFactory.createMBeanServer();
        Locale loc = Locale.getDefault();
        System.out.println("Create the MLet");
        MLet mlet = new MLet();
        System.out.println("Register the MLet MBean");
        ObjectName mletObjectName = new ObjectName("Test:type=MLet");
        mbs.registerMBean(mlet, mletObjectName);
        System.out.println("Call mlet.getMBeansFromURL(<url>)");
        String testSrc = System.getProperty("test.src");
        System.out.println("test.src = " + testSrc);
        String urlCodebase;
        if (testSrc.startsWith("/")) {
            urlCodebase = "file:" + testSrc.replace(File.separatorChar, '/') + "/";
        } else {
            urlCodebase = "file:/" + testSrc.replace(File.separatorChar, '/') + "/";
        }
        String mletFile = urlCodebase + args[0];
        System.out.println("MLet File = " + mletFile);
        try {
            Locale.setDefault(new Locale("tr", "TR"));
            mlet.getMBeansFromURL(mletFile);
            System.out.println("Test Passes");
        } catch (Exception e) {
            error = true;
            e.printStackTrace(System.out);
        } finally {
            Locale.setDefault(loc);
        }
        System.out.println("Unregister the MLet MBean");
        mbs.unregisterMBean(mletObjectName);
        System.out.println("Release the MBean server");
        MBeanServerFactory.releaseMBeanServer(mbs);
        System.out.println("Bye! Bye!");
        if (error)
            System.exit(1);
    }
}
