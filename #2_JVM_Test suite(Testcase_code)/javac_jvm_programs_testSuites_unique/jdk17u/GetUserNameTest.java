import java.awt.print.PrinterJob;

public class GetUserNameTest {

    public static void main(String[] args) {
        System.setSecurityManager(new SecurityManager());
        PrinterJob pj = PrinterJob.getPrinterJob();
        boolean secExcpn = false;
        try {
            System.out.println(pj.getUserName());
        } catch (SecurityException ex) {
            secExcpn = true;
            System.out.println("SecurityException thrown as user.name permission " + "not given");
        }
        if (!secExcpn) {
            throw new RuntimeException("SecurityException not thrown");
        }
    }
}
