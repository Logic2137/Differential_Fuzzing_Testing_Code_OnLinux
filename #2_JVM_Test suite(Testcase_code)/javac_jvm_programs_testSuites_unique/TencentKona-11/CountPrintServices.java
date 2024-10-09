

import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;


public class CountPrintServices {

  public static void main(String[] args) throws Exception {
    String os = System.getProperty("os.name").toLowerCase();
    System.out.println("OS is " + os);
    if (!os.equals("linux")) {
        System.out.println("Linux specific test. No need to continue");
        return;
    }
    PrintService services[] =
        PrintServiceLookup.lookupPrintServices(null, null);
    if (services.length > 0) {
       System.out.println("Services found. No need to test further.");
       return;
    }
    String[] lpcmd = { "lpstat", "-a" };
    Process proc = Runtime.getRuntime().exec(lpcmd);
    proc.waitFor();
    InputStreamReader ir = new InputStreamReader(proc.getInputStream());
    BufferedReader br = new BufferedReader(ir);
    int count = 0;
    String printer;
    while ((printer = br.readLine()) != null) {
       System.out.println("lpstat:: " + printer);
       count++;
    }
    if (count > 0) {
        throw new RuntimeException("Services exist, but not found by JDK.");
    }
 }
}

