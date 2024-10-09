



import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class RegisterNullService {
    public static void main (String [] args) throws RuntimeException {

        boolean registered = PrintServiceLookup.registerService(null);
        if (registered) {
           throw new RuntimeException("Null service was registered");
        }
        PrintService[] services =
            PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < services.length; i++) {
            if (services[i] == null) {
                throw new RuntimeException("Null service found.");
            }
        }
    }
}
