import java.lang.management.ManagementFactory;

public class DefaultManagementProviderTest {

    public static void main(String[] argv) {
        ManagementFactory.getPlatformMBeanServer();
        System.out.println("Test case passed");
    }
}
