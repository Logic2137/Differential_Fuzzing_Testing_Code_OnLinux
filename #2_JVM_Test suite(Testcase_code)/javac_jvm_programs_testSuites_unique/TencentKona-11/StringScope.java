



import java.net.*;
import java.util.Enumeration;

public class StringScope {

    public static void main(String args[]) throws Exception {
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface iface = e.nextElement();
            Enumeration<InetAddress> iadrs = iface.getInetAddresses();
            while (iadrs.hasMoreElements()) {
                InetAddress iadr = iadrs.nextElement();
                if (iadr instanceof Inet6Address) {
                    Inet6Address i6adr = (Inet6Address) iadr;
                    NetworkInterface nif = i6adr.getScopedInterface();
                    if (nif == null)
                        continue;

                    String nifName = nif.getName();
                    String i6adrHostAddress = i6adr.getHostAddress();
                    int index = i6adrHostAddress.indexOf('%');
                    String i6adrScopeName = i6adrHostAddress.substring(index+1);

                    if (!nifName.equals(i6adrScopeName))
                        throw new RuntimeException("Expected nifName ["
                                      + nifName + "], to equal i6adrScopeName ["
                                      + i6adrScopeName + "] ");
                }
            }
        }
    }
}

