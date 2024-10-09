

import java.util.Arrays;

import javax.print.PrintServiceLookup;


public final class PrintServicesSecurityManager {

    public static void main(String[] args) throws InterruptedException {
        System.setSecurityManager(new SecurityManager());
        test();
        Thread.sleep(3000); 
        test();
    }

    private static void test() {
        Object[] services = PrintServiceLookup.lookupPrintServices(null, null);
        if (services.length != 0) {
            System.err.println("services = " + Arrays.toString(services));
            throw new RuntimeException("The array of Services must be empty");
        }
    }
}
